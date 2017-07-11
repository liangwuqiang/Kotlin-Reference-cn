---
type: doc
layout: reference
category: "Syntax"
title: "Returns and Jumps"
---

# Returns and Jumps || 返回和跳转

Kotlin has three structural jump expressions:

Kotlin有三种结构跳转表达式：

* *return*{: .keyword }. By default returns from the nearest enclosing function or [anonymous function](lambdas.md#anonymous-functions).
* return. 默认情况下，从当前所在函数或 [匿名函数](lambdas.md#anonymous-functions)中跳出。

* *break*{: .keyword }. Terminates the nearest enclosing loop.
* break. 结束当前所在的循环。

* *continue*{: .keyword }. Proceeds to the next step of the nearest enclosing loop.
* continue. 从当前所在循环中，直接跳入下一次循环。

All of these expressions can be used as part of larger expressions:

所有这些表达式可以作为较大的表达式的一部分：

``` kotlin
val s = person.name ?: return   //赋值语句，如果没有取到值，则函数从这里直接返回，不往下执行
```

The type of these expressions is the [Nothing type](exceptions.md#the-nothing-type).

这些表达式的类型属于 [空类型](exceptions.md#the-nothing-type).

## Break and Continue Labels || Break和Continue标签

Any expression in Kotlin may be marked with a *label*{: .keyword }.

Kotlin中的一些表达式可以用label（标签）来标记。

Labels have the form of an identifier followed by the `@` sign, 

标签的形式是，标识符后面跟随`@`标记

for example: `abc@`, `fooBar@` are valid labels (see the [grammar](grammar.md#labelReference)).

例如： `abc@`, `fooBar@` 都是有效的标签 (参见 [语法](grammar.md#labelReference)).

To label an expression, we just put a label in front of it

给表达式做标签，我们只需要把标签放到表达式前面即可

``` kotlin
loop@ for (i in 1..100) {
    // ...
}
```

Now, we can qualify a *break*{: .keyword } or a *continue*{: .keyword } with a label:

现在，我们就可以用标签来限定break或continue了：

``` kotlin
loop@ for (i in 1..100) {
    for (j in 1..100) {
        if (...) break@loop
    }
}
```

A *break*{: .keyword } qualified with a label jumps to the execution point right after the loop marked with that label.

带有标签限定的break正好会跳转到带有那个标签的循环标记后面的地方继续运行。

A *continue*{: .keyword } proceeds to the next iteration of that loop.

continue会继续执行那个循环的下一回合。 //跳过后面的语句

## Return at Labels || 在标签处返回

With function literals, local functions and object expression, functions can be nested in Kotlin. 

在Kotlin中，通过函数名称，不管是局部函数还是对象表达式，它们之间可以被相互嵌套。

Qualified *return*{: .keyword }s allow us to return from an outer function.
 
被限定的return可以允许我们直接跳出外层的函数。
 
The most important use case is returning from a lambda expression. Recall that when we write this:

最重要的使用实例是，在lambda表达式内跳转。回想一下我们曾写过的这个代码：

``` kotlin
fun foo() {
    ints.forEach {
        if (it == 0) return
        print(it)
    }
}
```

The *return*{: .keyword }-expression returns from the nearest enclosing function, i.e. `foo`.

return表达式通常会使程序从最近的封闭函数中转出，比如`foo`。

(Note that such non-local returns are supported only for lambda expressions passed to [inline functions](inline-functions.md).)

(注意，那种非正常跳转只有在lambda表达式的[内联函数](inline-functions.md)中可以使用。)

If we need to return from a lambda expression, we have to label it and qualify the *return*{: .keyword }:

如果我们只需要在lambda表达式内跳转，我们就不得不用标签来限定return：

``` kotlin
fun foo() {
    ints.forEach lit@ {
        if (it == 0) return@lit
        print(it)
    }
}
```

Now, it returns only from the lambda expression. Oftentimes it is more convenient to use implicits labels:

现在，它只会在lambda表达式内跳转。通常使用隐式标签更方便：

such a label has the same name as the function to which the lambda is passed.

隐式标签和lambda调用的函数同名。

``` kotlin
fun foo() {
    ints.forEach {
        if (it == 0) return@forEach
        print(it)
    }
}
```

Alternatively, we can replace the lambda expression with an [anonymous function](lambdas.md#anonymous-functions).

或者, 我们可以用[匿名函数](lambdas.md#anonymous-functions)来替代使用lambda表达式的跳转。

A *return*{: .keyword } statement in an anomymous function will return from the anonymous function itself.

在匿名函数中的return语句将从匿名函数本身跳出。

``` kotlin
fun foo() {
    ints.forEach(fun(value: Int) {  //这个也许就是forEach的本来面目，前面的是简化写法
        if (value == 0) return
        print(value)
    })
}
```

When returning a value, the parser gives preference to the qualified return, i.e.

当有返回值时，程序优先执行被限定的return部分，比如：

``` kotlin
return@a 1
```

means "return `1` at label `@a`" and not "return a labeled expression `(@a 1)`".

意思是在标签`@a`"处返回`1`，而不是返回一个带标签的表达式`(@a 1)`。

（完 2017-07-11）