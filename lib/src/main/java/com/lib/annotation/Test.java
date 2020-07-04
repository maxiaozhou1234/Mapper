package com.lib.annotation;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.util.HashMap;

import javax.lang.model.element.Modifier;

/**
 * Created by mxz on 2020/7/3.
 */
public class Test {
    public static void main(String[] args) {

        try {
            File file = new File("./lib/src/main/java/");

            ClassName hashMapName = ClassName.get(HashMap.class);
            ClassName stringName = ClassName.get(String.class);
            ClassName className = ClassName.get(Class.class);

            ParameterizedTypeName itemMap = ParameterizedTypeName.get(hashMapName, stringName, className);
            ParameterizedTypeName hashMap = ParameterizedTypeName.get(hashMapName, stringName, itemMap);

            FieldSpec map = FieldSpec.builder(hashMap, "map", Modifier.STATIC)
                    .initializer("new $T()", hashMap)
                    .build();

            StringBuilder body = new StringBuilder();
            CodeBlock.Builder codeBuilder = CodeBlock.builder()
                    .addStatement("$T item", itemMap);

            Class[] clz = new Class[]{Integer.class, String.class, Boolean.class};

            for (int i = 0; i < 6; i++) {
                codeBuilder.addStatement("item = new $T()", itemMap);
                body.setLength(0);
                for (int j = 0; j < 3; j++) {
                    body.append("item.put(\"key_").append(j).append("\",").append(clz[j].getName()).append(".class);\n");
                }
                codeBuilder.add(body.toString())
                        .add("map.put(\"item_").add(String.valueOf(i)).addStatement("\", item)");
            }

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("TypeMap$Data")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(map)
                    .addStaticBlock(codeBuilder.build());

            JavaFile javaFile = JavaFile.builder("com.lib.annotation", classBuilder.build()).build();
            javaFile.writeTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
