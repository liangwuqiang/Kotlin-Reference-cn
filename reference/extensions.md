---
type: doc
layout: reference
category: "Syntax"
title: "Extensions"
---

# Extensions 扩展

Kotlin, similar to C# and Gosu, 
Kotlin, 与 C# 和 Gosu 相似，

provides the ability to extend a class with new functionality without having to inherit from the class 
提供了使用新的函数扩展一个类的能力，而不用从类中继承

or use any type of design pattern such as Decorator.
或者使用诸如装饰者之类的设计模式的任何类型， //# decorator 装饰者

，也不使用类似装饰器这样的设计模式的情况下对指定类进行扩展。


This is done via special declarations called _extensions_. Kotlin supports _extension functions_ and _extension properties_.
   通过叫做 _扩展_ 的特殊声明来实现 。Kotlin支持 _扩展函数_ 和 _扩展属性_

## Extension Functions 扩展函数

To declare an extension function, 
为了声明一个扩展函数，

we need to prefix its name with a _receiver type_, 
我们需要给名字前缀一个 _接受者类型_ ,

i.e. the type being extended.
例如，被扩展的类型。

The following adds a `swap` function to `MutableList<Int>`:
下面添加一个 `swap` 函数到 `MutableList<Int>`类中 :

``` kotlin
fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}
```

The *this*{: .keyword } keyword inside an extension function corresponds to the receiver object 
扩展函数内部的 *this* 关键字 对应于接收对象

(the one that is passed before the dot).
 （就是在圆点之前的那部分）
 
Now, we can call such a function on any `MutableList<Int>`:
现在，我们可以调用这个函数，在任何使用`MutableList<Int>`的地方:

``` kotlin
val l = mutableListOf(1, 2, 3)
l.swap(0, 2) // 'this' inside 'swap()' will hold the value of 'l'
```

Of course, this function makes sense for any `MutableList<T>`, and we can make it generic:
当然， 这个函数感觉上和使用任何`MutableList<T>`一样， 我们可以使它变得通用：

``` kotlin
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}
```

We declare the generic type parameter before the function name for it 
我们 在函数名之前 声明通用的类型参数

to be available in the receiver type expression. 
使它成为可见的， 在接收类型表达式中

See [Generic functions](generics.html).
见 [通用的函数](generics.html)。

-------------------------------------------------
## Extensions are resolved **statically**

Extensions do not actually modify classes they extend. By defining an extension, you do not insert new members into a class,
but merely make new functions callable with the dot-notation on variables of this type.

We would like to emphasize that extension functions are dispatched **statically**, i.e. they are not virtual by receiver type.
This means that the extension function being called is determined by the type of the expression on which the function is invoked,
not by the type of the result of evaluating that expression at runtime. For example:

``` kotlin
open class C

class D: C()

fun C.foo() = "c"

fun D.foo() = "d"

fun printFoo(c: C) {
    println(c.foo())
}

printFoo(D())
```

This example will print "c", because the extension function being called depends only on the declared type of the
parameter `c`, which is the `C` class.

If a class has a member function, and an extension function is defined which has the same receiver type, the same name
and is applicable to given arguments, the **member always wins**.
For example:

``` kotlin
class C {
    fun foo() { println("member") }
}

fun C.foo() { println("extension") }
```

If we call `c.foo()` of any `c` of type `C`, it will print "member", not "extension".

However, it's perfectly OK for extension functions to overload member functions which have the same name but a different signature:

``` kotlin
class C {
    fun foo() { println("member") }
}

fun C.foo(i: Int) { println("extension") }
```

The call to `C().foo(1)` will print "extension".


## Nullable Receiver

Note that extensions can be defined with a nullable receiver type. Such extensions can be called on an object variable
even if its value is null, and can check for `this == null` inside the body. This is what allows you
to call toString() in Kotlin without checking for null: the check happens inside the extension function.

``` kotlin
fun Any?.toString(): String {
    if (this == null) return "null"
    // after the null check, 'this' is autocast to a non-null type, so the toString() below
    // resolves to the member function of the Any class
    return toString()
}
```

## Extension Properties

Similarly to functions, Kotlin supports extension properties:

``` kotlin
val <T> List<T>.lastIndex: Int
    get() = size - 1
```

Note that, since extensions do not actually insert members into classes, there's no efficient way for an extension 
property to have a [backing field](properties.html#backing-fields). This is why **initializers are not allowed for 
extension properties**. Their behavior can only be defined by explicitly providing getters/setters.

Example:

``` kotlin
val Foo.bar = 1 // error: initializers are not allowed for extension properties
```


## Companion Object Extensions

If a class has a [companion object](object-declarations.html#companion-objects) defined, you can also define extension
functions and properties for the companion object:

``` kotlin
class MyClass {
    companion object { }  // will be called "Companion"
}

fun MyClass.Companion.foo() {
    // ...
}
```

Just like regular members of the companion object, they can be called using only the class name as the qualifier:

``` kotlin
MyClass.foo()
```


## Scope of Extensions

Most of the time we define extensions on the top level, i.e. directly under packages:
 
``` kotlin
package foo.bar
 
fun Baz.goo() { ... } 
``` 

To use such an extension outside its declaring package, we need to import it at the call site:

``` kotlin
package com.example.usage

import foo.bar.goo // importing all extensions by name "goo"
                   // or
import foo.bar.*   // importing everything from "foo.bar"

fun usage(baz: Baz) {
    baz.goo()
)

```

See [Imports](packages.html#imports) for more information.

## Declaring Extensions as Members

Inside a class, you can declare extensions for another class. Inside such an extension, there are multiple _implicit receivers_ -
objects members of which can be accessed without a qualifier. The instance of the class in which the extension is declared is called
_dispatch receiver_, and the instance of the receiver type of the extension method is called _extension receiver_.

``` kotlin
class D {
    fun bar() { ... }
}

class C {
    fun baz() { ... }

    fun D.foo() {
        bar()   // calls D.bar
        baz()   // calls C.baz
    }

    fun caller(d: D) {
        d.foo()   // call the extension function
    }
}
```

In case of a name conflict between the members of the dispatch receiver and the extension receiver, the extension receiver takes
precedence. To refer to the member of the dispatch receiver you can use the [qualified `this` syntax](this-expressions.html#qualified).

``` kotlin
class C {
    fun D.foo() {
        toString()         // calls D.toString()
        this@C.toString()  // calls C.toString()
    }
```

Extensions declared as members can be declared as `open` and overridden in subclasses. This means that the dispatch of such
functions is virtual with regard to the dispatch receiver type, but static with regard to the extension receiver type.

``` kotlin
open class D {
}

class D1 : D() {
}

open class C {
    open fun D.foo() {
        println("D.foo in C")
    }

    open fun D1.foo() {
        println("D1.foo in C")
    }

    fun caller(d: D) {
        d.foo()   // call the extension function
    }
}

class C1 : C() {
    override fun D.foo() {
        println("D.foo in C1")
    }

    override fun D1.foo() {
        println("D1.foo in C1")
    }
}

C().caller(D())   // prints "D.foo in C"
C1().caller(D())  // prints "D.foo in C1" - dispatch receiver is resolved virtually
C().caller(D1())  // prints "D.foo in C" - extension receiver is resolved statically
```

 
## Motivation

In Java, we are used to classes named "\*Utils": `FileUtils`, `StringUtils` and so on. The famous `java.util.Collections` belongs to the same breed.
And the unpleasant part about these Utils-classes is that the code that uses them looks like this:

``` java
// Java
Collections.swap(list, Collections.binarySearch(list, Collections.max(otherList)), Collections.max(list))
```

Those class names are always getting in the way. We can use static imports and get this:

``` java
// Java
swap(list, binarySearch(list, max(otherList)), max(list))
```

This is a little better, but we have no or little help from the powerful code completion of the IDE. It would be so much better if we could say

``` java
// Java
list.swap(list.binarySearch(otherList.max()), list.max())
```

But we don't want to implement all the possible methods inside the class `List`, right? This is where extensions help us.
