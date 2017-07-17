---
type: doc
layout: reference
category: "Syntax"
title: "Higher-Order Functions and Lambdas"
---

# Higher-Order Functions and Lambdas 高阶函数与 lambda 表达式

## Higher-Order Functions 高阶函数

A higher-order function is a function that takes functions as parameters, or returns a function.
A good example of such a function is `lock()` that takes a lock object and a function, acquires the lock, runs the function and releases the lock:
高阶函数就是可以接受函数作为参数并返回一个函数的函数。比如 `lock()` 就是一个很好的例子，它接收一个 lock 对象和一个函数，运行函数并释放 lock;

``` kotlin
fun <T> lock(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body()
    }
    finally {
        lock.unlock()
    }
}
```

Let's examine the code above: `body` has a [function type](#function-types): `() -> T`,
so it's supposed to be a function that takes no parameters and returns a value of type `T`.
It is invoked inside the *try*{: .keyword }-block, while protected by the `lock`, and its result is returned by the `lock()` function.
现在解释一下上面的代码吧：`body` 有一个函数类型 `() -> T`,把它设想为没有参数并返回 T 类型的函数。它引发了内部的 try 函数块，并被 `lock` 保护，结果是通过 `lock()` 函数返回的。

If we want to call `lock()`, we can pass another function to it as an argument (see [function references](reflection.html#function-references)):
如果我们想调用 `lock()` ，函数，我们可以传给它另一个函数做参数，参看[函数参考](http://kotlinlang.org/docs/reference/reflection.html#function-references)：

``` kotlin
fun toBeSynchronized() = sharedResource.operation()

val result = lock(lock, ::toBeSynchronized)
```

Another, often more convenient way is to pass a [lambda expression](#lambda-expressions-and-anonymous-functions):
其实最方便的办法是传递一个字面函数(通常是 lambda 表达式)：

``` kotlin
val result = lock(lock, { sharedResource.operation() })
```

Lambda expressions are described in more [detail below](#lambda-expressions-and-anonymous-functions), but for purposes of continuing this section, let's see a brief overview:
字面函数经常描述有更多[细节](http://kotlinlang.org/docs/reference/lambdas.html#function-literals-and-function-expressions)，但为了继续本节，我们看一下更简单的预览吧：

* A lambda expression is always surrounded by curly braces,
* Its parameters (if any) are declared before `->` (parameter types may be omitted),
* The body goes after `->` (when present).
> 字面函数被包在大括号里

> 参数在 `->` 前面声明(参数类型可以省略)

> 函数体在 `->` 之后

In Kotlin, there is a convention that if the last parameter to a function is a function, and you're passing a lambda expression as the corresponding argument, you can specify it outside of parentheses:
在 kotlin 中有一个约定，如果最后一个参数是函数，可以省略括号：

``` kotlin
lock (lock) {
    sharedResource.operation()
}
```

Another example of a higher-order function would be `map()`:
最后一个高阶函数的例子是 `map()` (of MapReduce):

``` kotlin
fun <T, R> List<T>.map(transform: (T) -> R): List<R> {
    val result = arrayListOf<R>()
    for (item in this)
        result.add(transform(item))
    return result
}
```

This function can be called as follows:
函数可以通过下面的方式调用

``` kotlin
val doubled = ints.map { value -> value * 2 }
```

Note that the parentheses in a call can be omitted entirely if the lambda is the only argument to that call.

### `it`: implicit name of a single parameter

One other helpful convention is that if a function literal has only one parameter,
its declaration may be omitted (along with the `->`), and its name will be `it`:
如果字面函数只有一个参数，则声明可以省略，名字就是 `it` :

``` kotlin
ints.map { it * 2 }
```

These conventions allow to write [LINQ-style](http://msdn.microsoft.com/en-us/library/bb308959.aspx) code:
这样就可以写[LINQ-风格](http://msdn.microsoft.com/en-us/library/bb308959.aspx)的代码了：

``` kotlin
strings.filter { it.length == 5 }.sortBy { it }.map { it.toUpperCase() }
```

### Underscore for unused variables (since 1.1)

If the lambda parameter is unused, you can place an underscore instead of its name:

``` kotlin
map.forEach { _, value -> println("$value!") }
```

### Destructuring in Lambdas (since 1.1)

Destructuring in lambdas is described as a part of [destructuring declarations](multi-declarations.html#destructuring-in-lambdas-since-11). 

## Inline Functions 内联函数

Sometimes it is beneficial to enhance performance of higher-order functions using [inline functions](inline-functions.html).
有些时候可以用 [内联函数](http://kotlinlang.org/docs/reference/inline-functions.html) 提高高阶函数的性能。

## Lambda Expressions and Anonymous Functions 字面函数和函数表达式

A lambda expression or an anonymous function is a "function literal", i.e. a function that is not declared,
but passed immediately as an expression. Consider the following example:
字面函数或函数表达式就是一个 "匿名函数"，也就是没有声明的函数，但立即作为表达式传递下去。想想下面的例子：

``` kotlin
max(strings, { a, b -> a.length < b.length })
```

Function `max` is a higher-order function, i.e. it takes a function value as the second argument.
This second argument is an expression that is itself a function, i.e. a function literal. As a function, it is equivalent to
`max` 函数就是一个高阶函数,它接受函数作为第二个参数。第二个参数是一个表达式所以本生就是一个函数，即字面函数。作为一个函数，相当于：

``` kotlin
fun compare(a: String, b: String): Boolean = a.length < b.length
```

### Function Types 函数类型

For a function to accept another function as a parameter, we have to specify a function type for that parameter.
For example the abovementioned function `max` is defined as follows:
一个函数要接受另一个函数作为参数，我们得给它指定一个类型。比如上面的 `max` 定义是这样的：

``` kotlin
fun <T> max(collection: Collection<T>, less: (T, T) -> Boolean): T? {
    var max: T? = null
    for (it in collection)
        if (max == null || less(max, it))
            max = it
    return max
}
```

The parameter `less` is of type `(T, T) -> Boolean`, i.e. a function that takes two parameters of type `T` and returns a `Boolean`:
true if the first one is smaller than the second one.
参数 `less` 是 `(T, T) -> Boolean`类型，也就是接受俩个 `T` 类型参数返回一个 `Boolean`:如果第一个参数小于第二个则返回真。

In the body, line 4, `less` is used as a function: it is called by passing two arguments of type `T`.
在函数体第四行， `less` 是用作函数

A function type is written as above, or may have named parameters, if you want to document the meaning of each parameter.
一个函数类型可以像上面那样写，也可有命名参数，更多参看[命名参数](http://kotlinlang.org/docs/reference/functions.html#named-arguments)

``` kotlin
val compare: (x: T, y: T) -> Int = ...
```

### Lambda Expression Syntax Lambda表达式语法

The full syntactic form of lambda expressions, i.e. literals of function types, is as follows:
函数文本的完全写法是下面这样的：

``` kotlin
val sum = { x: Int, y: Int -> x + y }
```

A lambda expression is always surrounded by curly braces,
parameter declarations in the full syntactic form go inside parentheses and have optional type annotations,
the body goes after an `->` sign. If the inferred return type of the lambda is not `Unit`, the last (or possibly single) expression inside the lambda body is treated as the return value.
函数文本总是在大括号里包裹着，在完全语法中参数声明是在括号内，类型注解是可选的，函数体是在　`->` 之后，像下面这样：

If we leave all the optional annotations out, what's left looks like this:

``` kotlin
val sum: (Int, Int) -> Int = { x, y -> x + y }
```


It's very common that a lambda expression has only one parameter.
If Kotlin can figure the signature out itself, it allows us not to declare the only parameter, and will implicitly
declare it for us under the name `it`:
函数文本有时只有一个参数。如果 kotlin 可以从它本生计算出签名，那么可以省略这个唯一的参数，并会通过 `it` 隐式的声明它：

``` kotlin
ints.filter { it > 0 } // this literal is of type '(it: Int) -> Boolean'
```

We can explicitly return a value from the lambda using the [qualified return](returns.html#return-at-labels) syntax. Otherwise, the value of the last expression is implictly returned. Therefore, the two following snippets are equivalent:

``` kotlin
ints.filter {
    val shouldFilter = it > 0 
    shouldFilter
}

ints.filter {
    val shouldFilter = it > 0 
    return@filter shouldFilter
}
```

Note that if a function takes another function as the last parameter, the lambda expression argument can be passed
outside the parenthesized argument list.
See the grammar for [callSuffix](grammar.html#callSuffix).
注意如果一个函数接受另一个函数做为最后一个参数，该函数文本参数可以在括号内的参数列表外的传递。参看 [callSuffix](http://kotlinlang.org/docs/reference/grammar.html#call-suffix)

### Anonymous Functions 匿名函数

One thing missing from the lambda expression syntax presented above is the ability to specify the return type of the
function. In most cases, this is unnecessary because the return type can be inferred automatically. However, if you
do need to specify it explicitly, you can use an alternative syntax: an _anonymous function_.
上面没有讲到可以指定返回值的函数。在大多数情形中，这是不必要的，因为返回值是可以自动推断的。然而，如果你需要自己指定，可以用函数表达式来做：

``` kotlin
fun(x: Int, y: Int): Int = x + y
```

An anonymous function looks very much like a regular function declaration, except that its name is omitted. Its body
can be either an expression (as shown above) or a block:
函数表达式很像普通的函数声明，除了省略了函数名。它的函数体可以是一个表达式(像上面那样)或者是一个块：

``` kotlin
fun(x: Int, y: Int): Int {
    return x + y
}
```

The parameters and the return type are specified in the same way as for regular functions, except that the parameter
types can be omitted if they can be inferred from context:
参数以及返回值和普通函数是一样的，如果它们可以从上下文推断出参数类型，则参数可以省略：

``` kotlin
ints.filter(fun(item) = item > 0)
```

The return type inference for anonymous functions works just like for normal functions: the return type is inferred
automatically for anonymous functions with an expression body and has to be specified explicitly (or is assumed to be
`Unit`) for anonymous functions with a block body.
返回值类型的推导和普通函数一样：函数返回值是通过表达式自动推断并被明确声明

Note that anonymous function parameters are always passed inside the parentheses. The shorthand syntax allowing
to leave the function outside the parentheses works only for lambda expressions.
注意函数表达式的参数总是在括号里传递的。 The shorthand syntax allowing to leave the function outside the parentheses works only for function literals.

One other difference between lambda expressions and anonymous functions is the behavior of
[non-local returns](inline-functions.html#non-local-returns). A *return*{: .keyword }  statement without a label
always returns from the function declared with the *fun*{: .keyword } keyword. This means that a *return*{: .keyword }
inside a lambda expression will return from the enclosing function, whereas a *return*{: .keyword } inside
an anonymous function will return from the anonymous function itself.
字面函数和表达式函数的另一个区别是没有本地返回。没有 lable 的返回总是返回到 fun 关键字所声明的地方。这意味着字面函数内的返回会返回到一个闭合函数，而表达式函数会返回到函数表达式自身。

### Closures 闭包

A lambda expression or anonymous function (as well as a [local function](functions.html#local-functions) and an [object expression](object-declarations.html#object-expressions))
can access its _closure_, i.e. the variables declared in the outer scope. Unlike Java, the variables captured in the closure can be modified:
一个字面函数或者表达式函数可以访问闭包，即访问自身范围外的声明的变量。不像 java 那样在闭包中的变量可以被捕获修改：

``` kotlin
var sum = 0
ints.filter { it > 0 }.forEach {
    sum += it
}
print(sum)
```


### Function Literals with Receiver 

Kotlin provides the ability to call a function literal with a specified _receiver object_.
Inside the body of the function literal, you can call methods on that receiver object without any additional qualifiers.
This is similar to extension functions, which allow you to access members of the receiver object inside the body of the function.
One of the most important examples of their usage is [Type-safe Groovy-style builders](type-safe-builders.html).

The type of such a function literal is a function type with receiver:

``` kotlin
sum : Int.(other: Int) -> Int
```

The function literal can be called as if it were a method on the receiver object:

``` kotlin
1.sum(2)
```

The anonymous function syntax allows you to specify the receiver type of a function literal directly.
This can be useful if you need to declare a variable of a function type with receiver, and to use it later.

``` kotlin
val sum = fun Int.(other: Int): Int = this + other
```

Lambda expressions can be used as function literals with receiver when the receiver type can be inferred from context.

``` kotlin
class HTML {
    fun body() { ... }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()  // create the receiver object
    html.init()        // pass the receiver object to the lambda
    return html
}


html {       // lambda with receiver begins here
    body()   // calling a method on the receiver object
}
```
