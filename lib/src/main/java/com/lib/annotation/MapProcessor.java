package com.lib.annotation;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

//@AutoService(Processor.class)
public class MapProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        System.out.println("start");

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> sets = new LinkedHashSet<>();
        sets.add("com.lib.annotation.MapType");
        return sets;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        HashMap<String, HashMap<String, ClassName>> map = new HashMap<>();
        System.out.println("=========== process start ===========");
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(MapType.class)) {
            if (annotatedElement.getKind() == ElementKind.CLASS) {
                MapType annotation = annotatedElement.getAnnotation(MapType.class);
                String group = annotation.group();
                String name = annotation.name();
                String[] array = annotation.array();
                if (name.length() == 0 && array.length == 0) {
                    //写出异常日志
                    log(Diagnostic.Kind.ERROR, "type name and array are Empty.");
//                    System.out.println("type name is Empty.");
                    continue;
                }
                TypeElement typeElement = (TypeElement) annotatedElement;
                ClassName className = ClassName.get(typeElement);
                log("className >> " + className);

                HashMap<String, ClassName> items = map.get(group);
                if (items == null) {
                    items = new HashMap<>();
                    map.put(group, items);
                }
                if (name.length() > 0) {
                    items.put(name, className);
                }
                if (array.length > 0) {
                    for (String s : array) {
                        items.put(s, className);
                    }
                }
            }
        }

        //构建文件
        try {

            ClassName hashMapName = ClassName.get(HashMap.class);
            ClassName stringName = ClassName.get(String.class);
            ClassName className = ClassName.get(Class.class);

            ParameterizedTypeName itemMap = ParameterizedTypeName.get(hashMapName, stringName, className);
            ParameterizedTypeName hashMap = ParameterizedTypeName.get(hashMapName, stringName, itemMap);

            FieldSpec fieldMap = FieldSpec.builder(hashMap, "map", Modifier.STATIC)
                    .initializer("new $T()", hashMap)
                    .build();

            StringBuilder body = new StringBuilder();
            CodeBlock.Builder codeBuilder = CodeBlock.builder()
                    .addStatement("$T item", itemMap);

            for (Map.Entry<String, HashMap<String, ClassName>> item : map.entrySet()) {
                body.setLength(0);

                String group = item.getKey();
                System.out.println(" >> group : " + group + " << ");
                codeBuilder.addStatement("item = new $T()", itemMap);
                for (Map.Entry<String, ClassName> m : item.getValue().entrySet()) {
                    System.out.println("[ " + m.getKey() + ": " + m.getValue());
                    body.append("item.put(\"").append(m.getKey()).append("\",").append(m.getValue()).append(".class);\n");
                }
                codeBuilder.add(body.toString())
                        .add("map.put(\"").add(group).addStatement("\", item)");
            }

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("TypeMap$Data")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(fieldMap)
                    .addStaticBlock(codeBuilder.build());

            JavaFile javaFile = JavaFile.builder(this.getClass().getPackage().getName(), classBuilder.build()).build();
            javaFile.writeTo(filer);
        } catch (IOException e) {
            if (e instanceof FilerException) {
                //
            } else
                e.printStackTrace();
        }
        System.out.println("========= end =========");

        return true;
    }

    private void log(String msg) {
        log(Diagnostic.Kind.OTHER, msg);
    }

    private void log(Diagnostic.Kind kind, String msg) {
        messager.printMessage(kind, msg);
    }
}
