---
type: doc
类型: 文档
layout: reference
布局: 参考
category: "Basics"
分类: 基础
title: "Basic Syntax"
标题:  基础语法
---

# Basic Syntax 基础语法

## Defining packages 声明包名

Package specification should be at the top of the source file:

包名应该声明于源文件的开头：

``` kotlin
package my.demo

import java.util.*

// ...
```

It is not required to match directories and packages: 
source files can be placed <u>arbitrarily</u> in the file system.

源文件不必和包名及目录路径一致：源文件可以<u>任意地</u>放置。

See [Packages](packages.md).

参见 [包](packages.md)。

## Defining functions 定义函数

Function having two `Int` parameters with `Int` return type:

该函数带有两个`Int`值参数，并返回`Int`值：

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun sum(a: Int, b: Int): Int {
    return a + b
}
//sampleEnd 实例结束

fun main(args: Array<String>) {
    print("sum of 3 and 5 is ")
    println(sum(3, 5))
}
```
</div>

Function with an expression body and inferred return type:

该函数以表达式作为函数体，并可推测出返回值类型：

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun sum(a: Int, b: Int) = a + b
//sampleEnd 实例结束

fun main(args: Array<String>) {
    println("sum of 19 and 23 is ${sum(19, 23)}")
}
```
</div>

Function returning no meaningful value:

该函数无返回值：

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun printSum(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}
//sampleEnd 实例结束

fun main(args: Array<String>) {
    printSum(-1, 8)
}
```
</div>

`Unit` return type can be omitted:

返回值是`Unit`可以省略不写：

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun printSum(a: Int, b: Int) {
    println("sum of $a and $b is ${a + b}")
}
//sampleEnd 实例结束

fun main(args: Array<String>) {
    printSum(-1, 8)
}
```
</div>

See [Functions](functions.md).

参见 [函数](functions.md)。

## Defining local variables 定义局部变量

Assign-once (read-only) local variable:

常量：

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    val a: Int = 1  // immediate assignment 立即指定类型
    val b = 2   // `Int` type is inferred  可推测出是`Int`类型
    val c: Int  // Type required when no initializer is provided 不初始化时，要指定类型
    c = 3       // deferred assignment 后赋值
//sampleEnd 实例结束
    println("a = $a, b = $b, c = $c")
}
```
</div>

Mutable variable:

可变变量：

<div class="sample" markdown="1"> 

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    var x = 5 // `Int` type is inferred 可推测出是`Int`类型
    x += 1
//sampleEnd 实例结束
    println("x = $x")
}
```
</div>

See also [Properties And Fields](properties.md).

参见 [属性和字段](properties.md)。

## Comments 注释

Just like Java and JavaScript, Kotlin supports end-of-line and block comments.

像Java和JavaScript一样，Kotlin支持单行注释和块注释。

``` kotlin
// This is an end-of-line comment 这就是单行注释

/* This is a block comment 这是多行的块注释
   on multiple lines. */
```

Unlike Java, block comments in Kotlin can be nested.

和Java不一样的是，Kotlin的块注释可以嵌套。

See [Documenting Kotlin Code](kotlin-doc.md) for information on the documentation comment syntax.

参见 [Kotlin代码文档](kotlin-doc.md) 来了解文档注释的语法

## Using string templates 字符串模板

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    var a = 1
    // simple name in template: 含有简单变量的字符串模板
    val s1 = "a is $a" 
    
    a = 2
    // arbitrary expression in template: 含有表达式的字符串模板
    val s2 = "${s1.replace("is", "was")}, but now is $a"
//sampleEnd 实例结束
    println(s2)
}
```
</div>

See [String templates](basic-types.md#string-templates).

参见 [字符串模板](basic-types.md#string-templates)。

## Using conditional expressions 条件语句

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun maxOf(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}
//sampleEnd 实例结束

fun main(args: Array<String>) {
    println("max of 0 and 42 is ${maxOf(0, 42)}")
}
```
</div>


Using *if*{: .keyword } as an expression:

使用if语句：

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun maxOf(a: Int, b: Int) = if (a > b) a else b
//sampleEnd 实例结束

fun main(args: Array<String>) {
    println("max of 0 and 42 is ${maxOf(0, 42)}")
}
```
</div>

See [*if*{: .keyword }-expressions](control-flow.md#if-expression).

参见 [if语句](control-flow.md#if-expression)。

## Using nullable values and checking for *null*{: .keyword } 可空值和null值检查

A reference must be explicitly marked as nullable when *null*{: .keyword } value is possible.

可能是null值时，引用的名称必须被明确标识为可空。

Return *null*{: .keyword } if `str` does not hold an integer:

字符串`str`不是以整数数字的形式出现时，就会返回null值：

``` kotlin
fun parseInt(str: String): Int? {
    // ...
}
```

Use a function returning nullable value:

使用一个返回可空值的函数：

<div class="sample" markdown="1" data-min-compiler-version="1.1">

``` kotlin
fun parseInt(str: String): Int? {
    return str.toIntOrNull()
}

//sampleStart 实例开始
fun printProduct(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    val y = parseInt(arg2)

    // Using `x * y` yields error because they may hold nulls.
    // 直接使用 x * y`会产生错误，因为它们有可能为空值。
    if (x != null && y != null) {
        // x and y are automatically cast to non-nullable after null check
        // 空值检查后，x 和 y 进行计算，不会产生空值问题
        
        println(x * y)
    }
    else {
        println("either '$arg1' or '$arg2' is not a number")
    }    
}
//sampleEnd 实例结束


fun main(args: Array<String>) {
    printProduct("6", "7")
    printProduct("a", "7")
    printProduct("a", "b")
}
```
</div>

or

或者

<div class="sample" markdown="1" data-min-compiler-version="1.1">

``` kotlin
fun parseInt(str: String): Int? {
    return str.toIntOrNull()
}

fun printProduct(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    val y = parseInt(arg2)
    
//sampleStart 实例开始
    // ...
    if (x == null) {
        println("Wrong number format in arg1: '${arg1}'")
        return
    }
    if (y == null) {
        println("Wrong number format in arg2: '${arg2}'")
        return
    }

    // x and y are automatically cast to non-nullable after null check
    // 空值检查后，x 和 y 的计算就不会存在空值问题了
    println(x * y)
//sampleEnd 实例结束
}

fun main(args: Array<String>) {
    printProduct("6", "7")
    printProduct("a", "7")
    printProduct("99", "b")
}
```
</div>

See [Null-safety](null-safety.md).

参见 [安全的空值](null-safety.md)。

## Using type checks and automatic casts 使用类型检查和自动转换

The *is*{: .keyword } operator checks if an expression is an instance of a type.

使用is操作符来检查一个表达式是否是某种类型的实例。

If an immutable local variable or property is checked for a specific type, 

如果一个不可变的局部变量或属性被检查为一种具体的类型，

there's no need to cast it explicitly:

就不需要明确地转换了：

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // `obj` is automatically cast to `String` in this branch
        // 在该分支中，`obj`被自动转换为`String`类型
        return obj.length
    }

    // `obj` is still of type `Any` outside of the type-checked branch
    // 在类型检查分支外，`obj`仍然是`Any`类型
    return null
}
//sampleEnd 实例结束

fun main(args: Array<String>) {
    fun printLength(obj: Any) {
        println("'$obj' string length is ${getStringLength(obj) ?: "... err, not a string"} ")
    }
    printLength("Incomprehensibilities")
    printLength(1000)
    printLength(listOf(Any()))
}
```
</div>

    ****************************************
    译者注：（以下为上面程序的运行结果）
    'Incomprehensibilities' string length is 21 
    '1000' string length is ... err, not a string 
    '[java.lang.Object@2626b418]' string length is ... err, not a string
     ****************************************

or

或者

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun getStringLength(obj: Any): Int? {
    if (obj !is String) return null

    // `obj` is automatically cast to `String` in this branch
    // 在这个分支中，`obj`自动转换成`String'类型
    return obj.length
}
//sampleEnd 实例结束


fun main(args: Array<String>) {
    fun printLength(obj: Any) {
        println("'$obj' string length is ${getStringLength(obj) ?: "... err, not a string"} ")
    }
    printLength("Incomprehensibilities")
    printLength(1000)
    printLength(listOf(Any()))
}
```
</div>

or even

或者这样

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun getStringLength(obj: Any): Int? {
    // `obj` is automatically cast to `String` on the right-hand side of `&&`
    // `obj`在`&&`的右边已经自动转成了`String`类型（左边转换）
    if (obj is String && obj.length > 0) {
        return obj.length
    }

    return null
}
//sampleEnd 实例结束


fun main(args: Array<String>) {
    fun printLength(obj: Any) {
        println("'$obj' string length is ${getStringLength(obj) ?: "... err, is empty or not a string at all"} ")
    }
    printLength("Incomprehensibilities")
    printLength("")
    printLength(1000)
}
```
</div>

See [Classes](classes.md) and [Type casts](typecasts.md).

参见 [类](classes.md)和[类型转换](typecasts.md)。

## Using a `for` loop 使用for循环

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    val items = listOf("apple", "banana", "kiwi")
    for (item in items) {
        println(item)
    }
//sampleEnd 实例结束
}
```
</div>

or

或者

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    val items = listOf("apple", "banana", "kiwi")
    for (index in items.indices) {
        println("item at $index is ${items[index]}")
    }
//sampleEnd 实例结束
}
```
</div>


See [for loop](control-flow.md#for-loops).

参见 [for循环](control-flow.md#for-loops)。

## Using a `while` loop 使用while循环

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    val items = listOf("apple", "banana", "kiwi")
    var index = 0
    while (index < items.size) {
        println("item at $index is ${items[index]}")
        index++
    }
//sampleEnd 实例结束
}
```
</div>


See [while loop](control-flow.md#while-loops).

参见 [while循环](control-flow.md#while-loops).

## Using `when` expression 使用when语句

<div class="sample" markdown="1">

``` kotlin
//sampleStart 实例开始
fun describe(obj: Any): String =
    when (obj) {
        1          -> "One"
        "Hello"    -> "Greeting"
        is Long    -> "Long"
        !is String -> "Not a string"
        else       -> "Unknown"
    }
//sampleEnd 实例结束

fun main(args: Array<String>) {
    println(describe(1))
    println(describe("Hello"))
    println(describe(1000L))
    println(describe(2))
    println(describe("other"))
}
```
</div>


See [when expression](control-flow.md#when-expression).

参见 [when语句](control-flow.md#when-expression).

## Using ranges 使用`范围`

Check if a number is within a range using *in*{: .keyword } operator:

使用in操作符来检查一个数字是否在一个范围之内

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    val x = 10
    val y = 9
    if (x in 1..y+1) {
        println("fits in range")
    }
//sampleEnd 实例结束
}
```
</div>


Check if a number is out of range:

数字不在范围内的检查

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    val list = listOf("a", "b", "c")
    
    if (-1 !in 0..list.lastIndex) {
        println("-1 is out of range")
    }
    if (list.size !in list.indices) {
        println("list size is out of valid list indices range too")
    }
//sampleEnd 实例结束
}
```
</div>


Iterating over a range:

遍历一个范围

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    for (x in 1..5) {
        print(x)
    }
//sampleEnd 实例结束
}
```
</div>

or over a progression:

或在一个步进中遍历

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
//sampleStart 实例开始
    for (x in 1..10 step 2) {
        print(x)
    }
    for (x in 9 downTo 0 step 3) {
        print(x)
    }
//sampleEnd 实例结束
}
```
</div>

See [Ranges](ranges.md).

参见 [范围](ranges.md).

## Using collections 使用集合

Iterating over a collection:

遍历一个集合

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
    val items = listOf("apple", "banana", "kiwi")
//sampleStart 实例开始
    for (item in items) {
        println(item)
    }
//sampleEnd 实例结束
}
```
</div>


Checking if a collection contains an object using *in*{: .keyword } operator:

使用 in 操作符来检查一个对象是否在集合中

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
    val items = setOf("apple", "banana", "kiwi")
//sampleStart 实例开始
    when {
        "orange" in items -> println("juicy")
        "apple" in items -> println("apple is fine too")
    }
//sampleEnd 实例结束
}
```
</div>


Using lambda expressions to filter and map collections:

使用lambda表达式来过滤和映射集合

<div class="sample" markdown="1">

``` kotlin
fun main(args: Array<String>) {
    val fruits = listOf("banana", "avocado", "apple", "kiwi")
//sampleStart 实例开始
    fruits
        .filter { it.startsWith("a") }
        .sortedBy { it }
        .map { it.toUpperCase() }
        .forEach { println(it) }
//sampleEnd 实例结束
}
```
</div>

See [Higher-order functions and Lambdas](lambdas.md).
 
 参见 [高阶函数和Lambdas](lambdas.md)。
