---
type: doc
layout: reference
category: "Syntax"
title: "Interfaces"
---

# Interfaces 接口

Interfaces in Kotlin are very similar to Java 8. 
Kotlin中的接口和Java8中的很相似。

They can contain declarations of abstract methods, 
它们可以包含抽象方法的声明，

as well as method implementations. 
以及方法的实现。

What makes them different from abstract classes is that interfaces cannot store state. 
它们和抽象类不同之处是 接口不能保存状态。

They can have properties but these need to be abstract or to provide accessor implementations.
它们可以有属性， 但是这些需要是抽象的 或者 提供访问器的实现。


An interface is defined using the keyword *interface*{: .keyword }
接口用关键字 *interface* 来定义：

``` kotlin
interface MyInterface {
    fun bar()
    fun foo() {
      // optional body 可选的方法体
    }
}
```

## Implementing Interfaces 接口的实现

A class or object can implement one or more interfaces
一个类或对象可以实现一个或多个接口

``` kotlin
class Child : MyInterface {
    override fun bar() {
        // body 方法体
    }
}
```

## Properties in Interfaces 接口中的属性

You can declare properties in interfaces. 
你可以在接口中声明属性。

A property declared in an interface can either be abstract, 
接口中的属性声明既可以是抽象的，

or it can provide implementations for accessors. 
或者它可以提供访问器的实现。

Properties declared in interfaces can't have backing fields, 
接口中的属性声明没有备用字段（field）,

and therefore accessors declared in interfaces can't reference them.
所以在接口中的访问器声明不能应用它们。

``` kotlin
interface MyInterface {
    val prop: Int // abstract 抽象

    val propertyWithImplementation: String
        get() = "foo"

    fun foo() {
        print(prop)
    }
}

class Child : MyInterface {
    override val prop: Int = 29
}
```

## Resolving overriding conflicts 解决复写冲突

When we declare many types in our supertype list, 
当我们在父类列表中声明许多类型时，

it may appear that we inherit more than one implementation of the same method. For example
可能会出现， 我们继承同一方法的多个实现， 例如：

``` kotlin
interface A {
    fun foo() { print("A") }
    fun bar()
}

interface B {
    fun foo() { print("B") }
    fun bar() { print("bar") }
}

class C : A {
    override fun bar() { print("bar") }
}

class D : A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }

    override fun bar() {
        super<B>.bar()
    }
}
```

Interfaces *A* and *B* both declare functions *foo()* and *bar()*. 
接口 *A* 和 *B* 都声明了函数（方法） *foo()* 和 *bar()*。

Both of them implement *foo()*, 
它们都实现了 *foo()* 方法，

but only *B* implements *bar()* 
但是，只有 *B* 实现了 *bar()* 方法

(*bar()* is not marked abstract in *A*,
(*bar()* 在 *A* 中并没有标记为抽象，

because this is the default for interfaces, 
因为这在接口中是默认的，

if the function has no body). 
如果函数没有函数体），

Now, if we derive a concrete class *C* from *A*, 
现在，如果我们 从 *A* 中 派生出一个具体的类 *C*， //# derive 派生 concrete 具体的

we, obviously, have to override *bar()* and provide an implementation.
显然地， 我们不得不复写 *bar()* 并提供一个实现。


However, if we derive *D* from *A* and *B*, 
然而， 如果我们从 *A* 和 *B* 中派生出 *D*,

we need to implement all the methods which we have inherited from multiple interfaces, 
我们需要实现所有 从多个接口中 所继承的 方法，  //# 单实现方法可以不用复写吧？ 


and to specify how exactly *D* should implement them. 
并且指定  *D* 如何恰当地实现它们。

This rule applies both to methods for which we've inherited 
a single implementation (*bar()*) and multiple implementations (*foo()*). 
这规则同时适用于 我们所继承的单实现方法 *bar()* 和多实现方法 *foo()*

（完 2017-07-14）