---
type: doc
layout: reference
category: "Syntax"
title: "Inline Functions"
---

# Inline Functions 内联函数

Using [higher-order functions](lambdas.html) imposes certain runtime penalties: each function is an object, and it captures a closure,
i.e. those variables that are accessed in the body of the function.
Memory allocations (both for function objects and classes) and virtual calls introduce runtime overhead.
使用[高阶函数](http://kotlinlang.org/docs/reference/lambdas.html)带来了相应的运行时麻烦：每个函数都是一个对象，它捕获闭包，即这些变量可以在函数体内被访问。内存的分配，虚拟调用的运行都会带来开销

But it appears that in many cases this kind of overhead can be eliminated by inlining the lambda expressions.
The functions shown below are good examples of this situation. I.e., the `lock()` function could be easily inlined at call-sites.
Consider the following case:
但在大多数这种开销是可以通过内联文本函数避免。下面就是一个很好的例子。`lock()` 函数可以很容易的在内联点调用。思考一下下面的例子：

``` kotlin
lock(l) { foo() }
```

Instead of creating a function object for the parameter and generating a call, the compiler could emit the following code
(Instead of creating a function object for the parameter and generating a call)，编译器可以忽略下面的代码：

``` kotlin
l.lock()
try {
    foo()
}
finally {
    l.unlock()
}
```

Isn't it what we wanted from the very beginning?
这好像不是我们开始想要的

To make the compiler do this, we need to mark the `lock()` function with the `inline` modifier:
想要让编译器不这样做的话，我们需要用 `inline` 标记 `lock()` 函数：

``` kotlin
inline fun lock<T>(lock: Lock, body: () -> T): T {
    // ...
}
```

The `inline` modifier affects both the function itself and the lambdas passed to it: all of those will be inlined
into the call site.
`inline` 标记即影响函数本身也影响传递进来的 lambda 函数：所有的这些都将被关联到调用点。

Inlining may cause the generated code to grow, but if we do it in a reasonable way (do not inline big functions)
it will pay off in performance, especially at "megamorphic" call-sites inside loops.
内联可能会引起生成代码增长，但我们可以合理的解决它(不要内联太大的函数)

## noinline

In case you want only some of the lambdas passed to an inline function to be inlined, you can mark some of your function
parameters with the `noinline` modifier:
为了你想要一些 lambda 表达式传递给内联函数时是内联的，你可以给你的一些函数参数标记 `@noinline` 注解：

``` kotlin
inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) {
    // ...
}
```

Inlinable lambdas can only be called inside the inline functions or passed as inlinable arguments,
but `noinline` ones can be manipulated in any way we like: stored in fields, passed around etc.
内联的 lambda 只能在内联函数中调用，或者作为内联参数，但 `@noinline` 标记的可以通过任何我们喜欢的方式操控：存储在字段，( passed around etc)

Note that if an inline function has no inlinable function parameters and no
[reified type parameters](#reified-type-parameters), the compiler will issue a warning, since inlining such functions is
 very unlikely to be beneficial (you can suppress the warning if you are sure the inlining is needed).
注意如果内联函数没有内联的函数参数并且没有具体类型的参数，编译器会报警告，这样内联函数就没有什么优点的(如果你认为内联是必须的你可以忽略警告)

## Non-local returns 返回到非局部

In Kotlin, we can only use a normal, unqualified `return` to exit a named function or an anonymous function.
This means that to exit a lambda, we have to use a [label](returns.html#return-at-labels), and a bare `return` is forbidden
inside a lambda, because a lambda can not make the enclosing function return:
在 kotlin 中，我们可以不加条件的使用 `return` 去退出一个命名函数或表达式函数。这意味这退出一个 lambda 函数，我们不得不使用[标签](http://kotlinlang.org/docs/reference/returns.html#return-at-labels)，而且空白的 `return` 在 lambda 函数中是禁止的，因为 lambda 函数不可以造一个闭合函数返回：

``` kotlin
fun foo() {
    ordinaryFunction {
        return // ERROR: can not make `foo` return here
    }
}
```

But if the function the lambda is passed to is inlined, the return can be inlined as well, so it is allowed:
但如果 lambda 函数是内联传递的，则返回也是可以内联的，因此允许下面这样：
注意有些内联函数可以调用传递进来的 lambda 函数，但不是在函数体，而是在另一个执行的上下文中，比如局部对象或者一个嵌套函数。在这样的情形中，非局部的控制流也不允许在lambda 函数中。为了表明，lambda 参数需要有 `InlineOptions.ONLY_LOCAL_RETURN` 注解：

``` kotlin
fun foo() {
    inlineFunction {
        return // OK: the lambda is inlined
    }
}
```

Such returns (located in a lambda, but exiting the enclosing function) are called *non-local* returns. We are used to
this sort of constructs in loops, which inline functions often enclose:

``` kotlin
fun hasZeros(ints: List<Int>): Boolean {
    ints.forEach {
        if (it == 0) return true // returns from hasZeros
    }
    return false
}
```

Note that some inline functions may call the lambdas passed to them as parameters not directly from the function body,
but from another execution context, such as a local object or a nested function. In such cases, non-local control flow
is also not allowed in the lambdas. To indicate that, the lambda parameter needs to be marked with
the `crossinline` modifier:

``` kotlin
inline fun f(crossinline body: () -> Unit) {
    val f = object: Runnable {
        override fun run() = body()
    }
    // ...
}
```


> `break` and `continue` are not yet available in inlined lambdas, but we are planning to support them too

## Reified type parameters 实例化参数类型

Sometimes we need to access a type passed to us as a parameter:
有时候我们需要访问传递过来的类型作为参数：

``` kotlin
fun <T> TreeNode.findParentOfType(clazz: Class<T>): T? {
    var p = parent
    while (p != null && !clazz.isInstance(p)) {
        p = p.parent
    }
    @Suppress("UNCHECKED_CAST")
    return p as T?
}
```

Here, we walk up a tree and use reflection to check if a node has a certain type.
It’s all fine, but the call site is not very pretty:
现在，我们创立了一颗树，并用反射检查它是否是某个特定类型。一切看起来很好，但调用点就很繁琐了：

``` kotlin
treeNode.findParentOfType(MyTreeNode::class.java)
```

What we actually want is simply pass a type to this function, i.e. call it like this:
我们想要的仅仅是给这个函数传递一个类型，即像下面这样：
``` kotlin
treeNode.findParentOfType<MyTreeNode>()
```

To enable this, inline functions support *reified type parameters*, so we can write something like this:
为了达到这个目的，内联函数支持具体化的类型参数，因此我们可以写成这样：

``` kotlin
inline fun <reified T> TreeNode.findParentOfType(): T? {
    var p = parent
    while (p != null && p !is T) {
        p = p.parent
    }
    return p as T?
}
```

We qualified the type parameter with the `reified` modifier, now it’s accessible inside the function,
almost as if it were a normal class. Since the function is inlined, no reflection is needed, normal operators like `!is`
and `as` are working now. Also, we can call it as mentioned above: `myTree.findParentOfType<MyTreeNodeType>()`.
我们用 refied 修饰符检查类型参数，既然它可以在函数内部访问了，也就基本上接近普通函数了。因为函数是内联的，所以不许要反射，像 `!is` ｀as｀这样的操作都可以使用。同时，我们也可以像上面那样调用它了 `myTree.findParentOfType<MyTreeNodeType>()`

Though reflection may not be needed in many cases, we can still use it with a reified type parameter:
尽管在很多情况下会使用反射，我们仍然可以使用实例化的类型参数 `javaClass()` 来访问它：


``` kotlin
inline fun <reified T> membersOf() = T::class.members

fun main(s: Array<String>) {
    println(membersOf<StringBuilder>().joinToString("\n"))
}
```

Normal functions (not marked as inline) can not have reified parameters.
A type that does not have a run-time representation (e.g. a non-reified type parameter or a fictitious type like `Nothing`)
can not be used as an argument for a reified type parameter.
普通的函数(没有标记为内联的)不能有实例化参数。

For a low-level description, see the [spec document](https://github.com/JetBrains/kotlin/blob/master/spec-docs/reified-type-parameters.md).
更底层的解释请看[spec document](https://github.com/JetBrains/kotlin/blob/master/spec-docs/reified-type-parameters.md)

## Inline properties (since 1.1)

The `inline` modifier can be used on accessors of properties that don't have a backing field.
You can annotate individual property accessors:

``` kotlin
val foo: Foo
    inline get() = Foo()

var bar: Bar
    get() = ...
    inline set(v) { ... }
```

You can also annotate an entire property, which marks both of its accessors as inline:

``` kotlin
inline var bar: Bar
    get() = ...
    set(v) { ... }
```

At the call site, inline accessors are inlined as regular inline functions.
