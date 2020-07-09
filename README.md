# MapCollection
HashMap&lt;String,HashMap&lt;String,Class> 注解，释放双手

### 为什么要用这个注解

通过业务的类型对应不同具体处理类，一般如下处理（不讨论设计模式，如策略模式等）：

1. 通过对类型判断，选择合适的处理类处理，也就是 if-else
2. 通过 Map 处理成键值对，但要需要自己构建 Map 关系

这就是这个简单注解使用的契机。
项目已经有十几条 if-else，但每次都需要再建 else-if，使用 Map 代替的话，又不想新建业务类后再添加一次到 Map，所以决定使用注解实现自动生成关系图，以上。

### 如何使用

这里是见注解库作为一个 module 添加在项目中，也可以直接 build 成 jar 添加入项目

1. android 项目 build.gradle 添加依赖，注解器
```
dependencies {
    implementation project(':mapper')
    annotationProcessor project(':mapper-compiler')
}
```
2. 使用注解
	* class TestA，存放分组 love，标记为 testA
```
@MapType(name = "testA", group = "love")
public class TestA {
    public TestA() {
        System.out.println(getClass().getName());
    }
}
```
	* class TestB，存放默认分组，标记为 testB，array 多个标签使用同个类
```
@MapType(name = "testB", array = {"a", "b", "c"})
public class TestB {
    public TestB() {
        System.out.println(getClass().getName());
    }
}
```
	* class TestC，存放默认分组，标记为 testC
```
@MapType(name = "testC")
public class TestC {
    public TestC() {
        System.out.println(getClass().getName());
    }
}
```
3. 调用
```
	Mapper.findItem("testA");//返回null，默认分组没有这个key
	Mapper.findItem("love", "testA");//返回对应 class
	Mapper.findItem("_default", "testB", false);//返回对应 class
	Mapper.findItem("kk", "testB", false);//返回null,kk 没有这个key，并且不在默认分组找
	Mapper.findItem("kk", "testB", true);//返回对应 class
	
	//方法说明
	public static Class findItem(String groupName, String typeName, boolean findToEnd) {
		//
	}
```