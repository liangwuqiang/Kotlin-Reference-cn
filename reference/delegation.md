---
type: doc
layout: reference
category: "Syntax"
title: "Delegation"
---

# Delegation 代理

## Class Delegation 类代理

The [Delegation pattern](https://en.wikipedia.org/wiki/Delegation_pattern) has proven to be a good alternative to implementation inheritance,
and Kotlin supports it natively requiring zero boilerplate code.
A class `Derived` can inherit from an interface `Base` and delegate all of its public methods to a specified object:
[代理模式](https://en.wikipedia.org/wiki/Delegation_pattern) 给实现继承提供了很好的代替方式， Kotlin 在语法上支持这一点，所以并不需要什么样板代码。`Derived` 类可以继承 `Base` 接口并且指定一个对象代理它全部的公共方法：

``` kotlin
interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}

class Derived(b: Base) : Base by b

fun main(args: Array<String>) {
    val b = BaseImpl(10)
    Derived(b).print() // prints 10
}
```

The *by*{: .keyword }-clause in the supertype list for `Derived` indicates that `b` will be stored internally in objects of `Derived`
and the compiler will generate all the methods of `Base` that forward to `b`.
在 `Derived` 的父类列表中的 by 从句会将 `b` 存储在 `Derived` 内部对象，并且编译器会生成 `Base` 的所有方法并转给 `b`。
