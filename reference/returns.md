（翻译不好，理解费劲，以后再修改）

---
type: doc
layout: reference
category: "Syntax"
title: "Returns and Jumps"
---

# Returns and Jumps 返回和跳转

Kotlin has three structural jump expressions:

Kotlin有三种结构跳转表达式：

* *return*{: .keyword }. By default returns from the nearest enclosing function or [anonymous function](lambdas.md#anonymous-functions).
* return. 默认情况下，从所在封闭的函数或 [匿名函数](lambdas.md#anonymous-functions)中跳转出来。
* *break*{: .keyword }. Terminates the nearest enclosing loop.
* break. 从所在封闭的循环中跳转出来。
* *continue*{: .keyword }. Proceeds to the next step of the nearest enclosing loop.
* continue. 从所在封闭循环中，忽略后面的代码，直接执行下一次循环。

All of these expressions can be used as part of larger expressions:

所有这些表达式可以作为更大表达式的一部分：

``` kotlin
val s = person.name ?: return
```

The type of these expressions is the [Nothing type](exceptions.md#the-nothing-type).

这些表达式的类型是 [Nothing type](exceptions.md#the-nothing-type).

## Break and Continue Labels Break和Continue标签

Any expression in Kotlin may be marked with a *label*{: .keyword }.

Kotlin中的一些表达式可以用label来标记。

Labels have the form of an identifier followed by the `@` sign, 

标签的形式是，标识符后面跟随`@`标记

for example: `abc@`, `fooBar@` are valid labels (see the [grammar](grammar.md#labelReference)).

例如： `abc@`, `fooBar@` 都是有效的标签 (参见 [语法](grammar.md#labelReference)).

To label an expression, we just put a label in front of it

给表达式做标签，我们只要把标签放到表达式前面即可

``` kotlin
loop@ for (i in 1..100) {
    // ...
}
```

Now, we can qualify a *break*{: .keyword } or a *continue*{: .keyword } with a label:

现在，我们就可以利用标签来进行break或者continue了：

``` kotlin
loop@ for (i in 1..100) {
    for (j in 1..100) {
        if (...) break@loop
    }
}
```

A *break*{: .keyword } qualified with a label jumps to the execution point right after the loop marked with that label.

break具备使用标签跳转到执行点的能力，右边跟随带有标签名的循环标记。

A *continue*{: .keyword } proceeds to the next iteration of that loop.

continue进行那个循环的下次迭代。

## Return at Labels 在标签处返回

With function literals, local functions and object expression, functions can be nested in Kotlin. 

具有函数常量，局部函数和对象表达式，在Kotlin中，函数可以嵌套使用 

Qualified *return*{: .keyword }s allow us to return from an outer function.
 
 Qualified return允许我们从一个外部函数中返回。
 
The most important use case is returning from a lambda expression. Recall that when we write this:

最重要的使用实例是，从lambda表达式中返回。回想一下我们写过的这个代码：

``` kotlin
fun foo() {
    ints.forEach {
        if (it == 0) return
        print(it)
    }
}
```

The *return*{: .keyword }-expression returns from the nearest enclosing function, i.e. `foo`.

return表达式从所在封闭的函数中返回，比如`foo`。

(Note that such non-local returns are supported only for lambda expressions passed to [inline functions](inline-functions.md).)

(注意，这样的非局部返回仅在lambda表达式交给[内联函数](inline-functions.md)被支持。)

If we need to return from a lambda expression, we have to label it and qualify the *return*{: .keyword }:

如果我们需要从lambda表达式中返回，我们不得不标签出来并限制return

``` kotlin
fun foo() {
    ints.forEach lit@ {
        if (it == 0) return@lit
        print(it)
    }
}
```

Now, it returns only from the lambda expression. Oftentimes it is more convenient to use implicits labels:

现在，它仅从lambda表达式中返回。它通常使用隐式标签更方便：

such a label has the same name as the function to which the lambda is passed.

这样的标签和传递lambda的函数同名。

``` kotlin
fun foo() {
    ints.forEach {
        if (it == 0) return@forEach
        print(it)
    }
}
```

Alternatively, we can replace the lambda expression with an [anonymous function](lambdas.md#anonymous-functions).

或者, 我们可以用[匿名函数](lambdas.md#anonymous-functions)来替换lambda表达式。

A *return*{: .keyword } statement in an anomymous function will return from the anonymous function itself.

在匿名函数中的return语句将从匿名函数本身返回。

``` kotlin
fun foo() {
    ints.forEach(fun(value: Int) {
        if (value == 0) return
        print(value)
    })
}
```

When returning a value, the parser gives preference to the qualified return, i.e.

当返回一个值时，解析器把首选项给有资格的返回（不理解），比如：

``` kotlin
return@a 1
```

means "return `1` at label `@a`" and not "return a labeled expression `(@a 1)`".

意思是在标签`@a`"处返回`1`，而不是返回标签表达式`(@a 1)`。