---
type: doc
layout: reference
category: "Syntax"
title: "Functions"
---

# Functions 函数

## Function Declarations 函数声明

Functions in Kotlin are declared using the *fun*{: .keyword } keyword
在 kotlin 中用关键字 `fun` 声明函数：

``` kotlin
fun double(x: Int): Int {
}
```

## Function Usage 函数用法

Calling functions uses the traditional approach
通过传统的方法调用函数

``` kotlin
val result = double(2)
```


Calling member functions uses the dot notation
通过`.`调用成员函数 

``` kotlin
Sample().foo() // create instance of class Sample and calls foo
```

### Infix notation 中缀符号

Functions can also be called using infix notations when
在满足以下条件时,函数也可以通过中缀符号进行调用:

* They are member functions or [extension functions](extensions.html)
* They have a single parameter
* They are marked with the `infix` keyword
>　它们是成员函数或者是[扩展函数](http://kotlinlang.org/docs/reference/extensions.html)
>　只有一个参数
>  使用`infix`关键词进行标记

``` kotlin
// Define extension to Int
//给 Int 定义一个扩展方法
infix fun Int.shl(x: Int): Int {
...
}

// call extension function using infix notation
//用中缀注解调用扩展函数
1 shl 2

// is the same as

1.shl(2)
```

### Parameters 参数

Function parameters are defined using Pascal notation, i.e. *name*: *type*. Parameters are separated using commas. Each parameter must be explicitly typed.
函数参数是用 Pascal 符号定义的　name:type。参数之间用逗号隔开，每个参数必须指明类型。

``` kotlin
fun powerOf(number: Int, exponent: Int) {
...
}
```

### Default Arguments 默认参数

Function parameters can have default values, which are used when a corresponding argument is omitted. This allows for a reduced number of overloads compared to
other languages.
函数参数可以设置默认值,当参数被忽略时会使用默认值。这样相比其他语言可以减少重载。

``` kotlin
fun read(b: Array<Byte>, off: Int = 0, len: Int = b.size()) {
...
}
```

Default values are defined using the **=** after type along with the value.
默认值可以通过在type类型后使用`=`号进行赋值

Overriding methods always use the same default parameter values as the base method.
When overriding a method with default parameters values, the default parameter values must be omitted from the signature:

``` kotlin
open class A {
    open fun foo(i: Int = 10) { ... }
}

class B : A() {
    override fun foo(i: Int) { ... }  // no default value allowed
}
```

### Named Arguments 命名参数

Function parameters can be named when calling functions. This is very convenient when a function has a high number of parameters or default ones.
在调用函数时可以参数可以命名。这对于那种有大量参数的函数是很方便的.

Given the following function
下面是一个例子：

``` kotlin
fun reformat(str: String,
             normalizeCase: Boolean = true,
             upperCaseFirstLetter: Boolean = true,
             divideByCamelHumps: Boolean = false,
             wordSeparator: Char = ' ') {
...
}
```

we could call this using default arguments
我们可以使用默认参数

``` kotlin
reformat(str)
```

However, when calling it with non-default, the call would look something like
然而当调用非默认参数是就需要像下面这样：

``` kotlin
reformat(str, true, true, false, '_')
```

With named arguments we can make the code much more readable
使用命名参数我们可以让代码可读性更强：

``` kotlin
reformat(str,
    normalizeCase = true,
    upperCaseFirstLetter = true,
    divideByCamelHumps = false,
    wordSeparator = '_'
)
```

and if we do not need all arguments
如果不需要全部参数的话可以这样：

``` kotlin
reformat(str, wordSeparator = '_')
```

Note that the named argument syntax cannot be used when calling Java functions, because Java bytecode does not
always preserve names of function parameters.
注意,命名参数语法不能够被用于调用Java函数中,因为Java的字节码不能确保方法参数命名的不变性


### Unit-returning functions 不带返回值的参数

If a function does not return any useful value, its return type is `Unit`. `Unit` is a type with only one value - `Unit`. This
value does not have to be returned explicitly
如果函数不会返回任何有用值，那么他的返回类型就是 `Unit` .`Unit` 是一个只有唯一值`Unit`的类型.这个值并不需要被直接返回:

``` kotlin
fun printHello(name: String?): Unit {
    if (name != null)
        println("Hello ${name}")
    else
        println("Hi there!")
    // `return Unit` or `return` is optional
}
```

The `Unit` return type declaration is also optional. The above code is equivalent to
`Unit` 返回值也可以省略，比如下面这样：

``` kotlin
fun printHello(name: String?) {
    ...
}
```

### Single-Expression functions 单表达式函数

When a function returns a single expression, the curly braces can be omitted and the body is specified after a **=** symbol
当函数只返回单个表达式时，大括号可以省略并在 = 后面定义函数体

``` kotlin
fun double(x: Int): Int = x * 2
```

Explicitly declaring the return type is [optional](#explicit-return-types) when this can be inferred by the compiler
在编译器可以推断出返回值类型的时候,返回值的类型可以省略:

``` kotlin
fun double(x: Int) = x * 2
```

### Explicit return types 明确返回类型

Functions with block body must always specify return types explicitly, unless it's intended for them to return `Unit`, [in which case it is optional](#unit-returning-functions).
Kotlin does not infer return types for functions with block bodies because such functions may have complex control flow in the body, and the return
type will be non-obvious to the reader (and sometimes even for the compiler). 
下面的例子中必须有明确返回类型,除非他是返回 `Unit`类型的值,Kotlin 并不会对函数体重的返回类型进行推断,因为函数体中可能有复杂的控制流,他的返回类型未必对读者可见(甚至对编译器而言也有可能是不可见的)：


### Variable number of arguments (Varargs) 变长参数

A parameter of a function (normally the last one) may be marked with `vararg` modifier:
函数的参数(通常是最后一个参数)可以用 vararg 修饰符进行标记：

``` kotlin
fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts) // ts is an Array
        result.add(t)
    return result
}
```

allowing a variable number of arguments to be passed to the function:
标记后,允许给函数传递可变长度的参数：

``` kotlin
val list = asList(1, 2, 3)
```

Inside a function a `vararg`-parameter of type `T` is visible as an array of `T`, i.e. the `ts` variable in the example above has type `Array<out T>`.

Only one parameter may be marked as `vararg`. If a `vararg` parameter is not the last one in the list, values for the
following parameters can be passed using the named argument syntax, or, if the parameter has a function type, by passing
a lambda outside parentheses.
只有一个参数可以被标注为 `vararg` 。加入`vararg`并不是列表中的最后一个参数,那么后面的参数需要通过命名参数语法进行传值,再或者如果这个参数是函数类型,就需要通过lambda法则.

When we call a `vararg`-function, we can pass arguments one-by-one, e.g. `asList(1, 2, 3)`, or, if we already have an array
 and want to pass its contents to the function, we use the **spread** operator (prefix the array with `*`):
当调用变长参数的函数时，我们可以一个一个的传递参数，比如 `asList(1, 2, 3)`，或者我们要传递一个 array 的内容给函数，我们就可以使用 * 前缀操作符：

```kotlin
val a = arrayOf(1, 2, 3)
val list = asList(-1, 0, *a, 4)
```

## Function Scope 函数范围

In Kotlin functions can be declared at top level in a file, meaning you do not need to create a class to hold a function, like languages such as Java, C# or Scala. In addition
to top level functions, Kotlin functions can also be declared local, as member functions and extension functions.
Kotlin 中可以在文件顶级声明函数，这就意味者你不用像在Java,C#或是Scala一样创建一个类来持有函数。除了顶级函数，Kotlin 函数可以声明为局部的，作为成员函数或扩展函数。

### Local Functions 局部函数

Kotlin supports local functions, i.e. a function inside another function
Kotlin 支持局部函数，比如在一个函数包含另一函数。

``` kotlin
fun dfs(graph: Graph) {
    fun dfs(current: Vertex, visited: Set<Vertex>) {
        if (!visited.add(current)) return
        for (v in current.neighbors)
            dfs(v, visited)
    }

    dfs(graph.vertices[0], HashSet())
}
```

Local function can access local variables of outer functions (i.e. the closure), so in the case above, the *visited* can be a local variable
局部函数可以访问外部函数的局部变量(比如闭包)

``` kotlin
fun dfs(graph: Graph) {
    val visited = HashSet<Vertex>()
    fun dfs(current: Vertex) {
        if (!visited.add(current)) return
        for (v in current.neighbors)
            dfs(v)
    }

    dfs(graph.vertices[0])
}
```

### Member Functions 成员函数

A member function is a function that is defined inside a class or object
成员函数是定义在一个类或对象里边的

``` kotlin
class Sample() {
    fun foo() { print("Foo") }
}
```

Member functions are called with dot notation
成员函数可以用 . 的方式调用

``` kotlin
Sample().foo() // creates instance of class Sample and calls foo
```

For more information on classes and overriding members see [Classes](classes.html) and [Inheritance](classes.html#inheritance)
更多请参看[类](http://kotlinlang.org/docs/reference/classes.html)和[继承](http://kotlinlang.org/docs/reference/classes.html#inheritance)

## Generic Functions 泛型函数

Functions can have generic parameters which are specified using angle brackets before the function name
函数可以有泛型参数，样式是在函数后跟上尖括号。

``` kotlin
fun <T> singletonList(item: T): List<T> {
    // ...
}
```

For more information on generic functions see [Generics](generics.html)
更多请参看[泛型](http://kotlinlang.org/docs/reference/generics.html)

## Inline Functions 内联函数

Inline functions are explained [here](inline-functions.html)

## Extension Functions 扩展函数

Extension functions are explained in [their own section](extensions.html)

## Higher-Order Functions and Lambdas 高阶函数和lamda表达式

Higher-Order functions and Lambdas are explained in [their own section](lambdas.html)

## Tail recursive functions 尾递归函数

Kotlin supports a style of functional programming known as [tail recursion](https://en.wikipedia.org/wiki/Tail_call).
This allows some algorithms that would normally be written using loops to instead be written using a recursive function, but without the risk of stack overflow.
When a function is marked with the `tailrec` modifier and meets the required form, the compiler optimises out the recursion, leaving behind a fast and efficient loop based version instead.
Kotlin 支持函数式编程的尾递归。这个允许一些算法可以通过循环而不是递归解决问题，从而避免了栈溢出。当函数被标记为 `tailrec` 时，编译器会优化递归，并用高效迅速的循环代替它。

``` kotlin
tailrec fun findFixPoint(x: Double = 1.0): Double
        = if (x == Math.cos(x)) x else findFixPoint(Math.cos(x))
```

This code calculates the fixpoint of cosine, which is a mathematical constant. It simply calls Math.cos repeatedly starting at 1.0 until the result doesn't change any more, yielding a result of 0.7390851332151607. The resulting code is equivalent to this more traditional style:
这段代码计算的是数学上的余弦不动点。Math.cos 从 1.0  开始不断重复，直到值不变为止，结果是 0.7390851332151607 
这段代码和下面的是等效的：

``` kotlin
private fun findFixPoint(): Double {
    var x = 1.0
    while (true) {
        val y = Math.cos(x)
        if (x == y) return y
        x = y
    }
}
```

To be eligible for the `tailrec` modifier, a function must call itself as the last operation it performs. You cannot use tail recursion when there is more code after the recursive call, and you cannot use it within try/catch/finally blocks. Currently tail recursion is only supported in the JVM backend.
使用 `tailrec` 修饰符必须在最后一个操作中调用自己。在递归调用代码后面是不允许有其它代码的，并且也不可以在 try/catch/finall 块中进行使用。当前的尾递归只在 JVM 的后端中可以用