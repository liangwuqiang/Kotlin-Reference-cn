---
type: doc
layout: reference
category: "Syntax"
title: "Nested Classes"
---

# Nested Classes 嵌套类

Classes can be nested in other classes
类可以嵌套在其他类中

``` kotlin
class Outer {
    private val bar: Int = 1
    class Nested {
        fun foo() = 2
    }
}

val demo = Outer.Nested().foo() // == 2
```

## Inner classes 内部类

A class may be marked as *inner*{: .keyword } to be able to access members of outer class. Inner classes carry a reference to an object of an outer class:
类可以标记为 inner 这样就可以访问外部类的成员。内部类拥有外部类的一个对象引用：

``` kotlin
class Outer {
    private val bar: Int = 1
    inner class Inner {
        fun foo() = bar
    }
}

val demo = Outer().Inner().foo() // == 1
```

See [Qualified *this*{: .keyword } expressions](this-expressions.html) to learn about disambiguation of *this*{: .keyword } in inner classes.
参看[这里](http://kotlinlang.org/docs/reference/this-expressions.html)了解更多 this 在内部类的用法

## Anonymous inner classes 匿名内部类

Anonymous inner class instances are created using an [object expression](object-declarations.html#object-expressions):
匿名内部类的实例是通过 [对象表达式](ClassesAndObjects/ObjectExpressicAndDeclarations.md)  创建的：
                                                      
``` kotlin
window.addMouseListener(object: MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) {
        // ...
    }
                                                                                                            
    override fun mouseEntered(e: MouseEvent) {
        // ...
    }
})
```

If the object is an instance of a functional Java interface (i.e. a Java interface with a single abstract method),
you can create it using a lambda expression prefixed with the type of the interface:
如果对象是函数式的 java 接口的实例（比如只有一个抽象方法的 java 接口），你可以用一个带接口类型的 lambda 表达式创建它。

``` kotlin
val listener = ActionListener { println("clicked") }
```