---
type: doc
layout: reference
category: "Syntax"
title: "Object Expressions and Declarations"
---

# Object Expressions and Declarations 对象表达式和声明

Sometimes we need to create an object of a slight modification of some class, without explicitly declaring a new subclass for it.
Java handles this case with *anonymous inner classes*.
有时候我们想要创建一个对当前类有一点小修改的对象，但不想重新声明一个子类。java 用匿名内部类的概念解决这个问题。Kotlin 用对象表达式和对象声明巧妙的实现了这一概念。

Kotlin slightly generalizes this concept with *object expressions* and *object declarations*.

## Object expressions 对象表达式

To create an object of an anonymous class that inherits from some type (or types), we write:
通过下面的方式可以创建继承自某种(或某些)匿名类的对象：

``` kotlin
window.addMouseListener(object : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {
        // ...
    }

    override fun mouseEntered(e: MouseEvent) {
        // ...
    }
})
```

If a supertype has a constructor, appropriate constructor parameters must be passed to it.
Many supertypes may be specified as a comma-separated list after the colon:
如果父类有构造函数，则必须传递相应的构造参数。多个父类可以用逗号隔开，跟在冒号后面：


``` kotlin
open class A(x: Int) {
    public open val y: Int = x
}

interface B {...}

val ab: A = object : A(1), B {
    override val y = 15
}
```

If, by any chance, we need "just an object", with no nontrivial supertypes, we can simply say:
有时候我们只是需要一个没有父类的对象，我们可以这样写：

``` kotlin
fun foo() {
    val adHoc = object {
        var x: Int = 0
        var y: Int = 0
    }
    print(adHoc.x + adHoc.y)
}
```

Note that anonymous objects can be used as types only in local and private declarations. If you use an anonymous object as a
return type of a public function or the type of a public property, the actual type of that function or property
will be the declared supertype of the anonymous object, or `Any` if you didn't declare any supertype. Members added
in the anonymous object will not be accessible.

``` kotlin
class C {
    // Private function, so the return type is the anonymous object type
    private fun foo() = object {
        val x: String = "x"
    }

    // Public function, so the return type is Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x        // Works
        val x2 = publicFoo().x  // ERROR: Unresolved reference 'x'
    }
}
```

Just like Java's anonymous inner classes, code in object expressions can access variables from the enclosing scope.
(Unlike Java, this is not restricted to final variables.)
像 java 的匿名内部类一样，对象表达式可以访问闭合范围内的变量 (和 java 不一样的是，这些变量不用是 final 修饰的)

``` kotlin
fun countClicks(window: JComponent) {
    var clickCount = 0
    var enterCount = 0

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            clickCount++
        }

        override fun mouseEntered(e: MouseEvent) {
            enterCount++
        }
    })
    // ...
}
```

## Object declarations 对象声明

[Singleton](http://en.wikipedia.org/wiki/Singleton_pattern) is a very useful pattern, and Kotlin (after Scala) makes it easy to declare singletons:
[单例模式](http://en.wikipedia.org/wiki/Singleton_pattern)是一种很有用的模式，Kotln 中声明它很方便：


``` kotlin
object DataProviderManager {
    fun registerDataProvider(provider: DataProvider) {
        // ...
    }

    val allDataProviders: Collection<DataProvider>
        get() = // ...
}
```

This is called an *object declaration*, and it always has a name following the *object*{: .keyword } keyword.
Just like a variable declaration, an object declaration is not an expression, and cannot be used on the right hand side of an assignment statement.
这叫做对象声明，跟在 object 关键字后面是对象名。和变量声明一样，对象声明并不是表达式，而且不能作为右值用在赋值语句。

To refer to the object, we use its name directly:
想要访问这个类，直接通过名字来使用这个类：

``` kotlin
DataProviderManager.registerDataProvider(...)
```

Such objects can have supertypes:
这样类型的对象可以有父类型：

``` kotlin
object DefaultListener : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {
        // ...
    }

    override fun mouseEntered(e: MouseEvent) {
        // ...
    }
}
```

**NOTE**: object declarations can't be local (i.e. be nested directly inside a function), but they can be nested into other object declarations or non-inner classes.
**注意**：对象声明不可以是局部的(比如不可以直接在函数内部声明)，但可以在其它对象的声明或非内部类中进行内嵌入


### Companion Objects 伴生对象

An object declaration inside a class can be marked with the *companion*{: .keyword } keyword:
在类声明内部可以用 companion 关键字标记对象声明：

``` kotlin
class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}
```

Members of the companion object can be called by using simply the class name as the qualifier:
伴随对象的成员可以通过类名做限定词直接使用：

``` kotlin
val instance = MyClass.create()
```

The name of the companion object can be omitted, in which case the name `Companion` will be used:
在使用了 `companion` 关键字时，伴随对象的名字可以省略：

``` kotlin
class MyClass {
    companion object {
    }
}

val x = MyClass.Companion
```

Note that, even though the members of companion objects look like static members in other languages, at runtime those
are still instance members of real objects, and can, for example, implement interfaces:
注意，尽管伴随对象的成员很像其它语言中的静态成员，但在运行时它们任然是真正对象的成员实例，比如可以实现接口：

``` kotlin
interface Factory<T> {
    fun create(): T
}


class MyClass {
    companion object : Factory<MyClass> {
        override fun create(): MyClass = MyClass()
    }
}
```

However, on the JVM you can have members of companion objects generated as real static methods and fields, if you use
the `@JvmStatic` annotation. See the [Java interoperability](java-to-kotlin-interop.html#static-fields) section
for more details.
如果你在 JVM 上使用 `@JvmStatic` 注解，你可以有多个伴随对象生成为真实的静态方法和属性。参看 [java interoperabillity](https://kotlinlang.org/docs/reference/java-interop.html#static-methods-and-fields)。


### Semantic difference between object expressions and declarations  对象表达式和声明的区别

There is one important semantic difference between object expressions and object declarations:
他俩之间只有一个特别重要的区别：

* object expressions are executed (and initialized) **immediately**, where they are used
>　对象表达式在我们使用的地方立即初始化并执行的

* object declarations are initialized **lazily**, when accessed for the first time
>　对象声明是懒加载的，是在我们第一次访问时初始化的。

* a companion object is initialized when the corresponding class is loaded (resolved), matching the semantics of a Java static initializer
>​    伴随对象是在对应的类加载时初始化的，和 Java 的静态初始是对应的。
