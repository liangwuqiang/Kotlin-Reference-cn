---
type: doc
layout: reference
category: "Syntax"
title: "Control Flow"
---

# Control Flow 控制流

## If Expression If表达式

In Kotlin, *if*{: .keyword } is an expression, i.e. it returns a value.

在Kotlin中，if是个表达式，它能返回一个值。

Therefore there is no ternary operator (condition ? then : else), 

所以，Kotlin不需要三元操作符（condition语句？ then语句： else语句）

because ordinary *if*{: .keyword } works fine in this role.

因为if在这方面已经满足要求。

``` kotlin
// Traditional usage 传统用法
var max = a 
if (a < b) max = b

// With else 带else
var max: Int
if (a > b) {
    max = a
} else {
    max = b
}
 
// As expression 作为表达式
val max = if (a > b) a else b
```

*if*{: .keyword } branches can be blocks, 

if 分支里可以是一些代码块，（用大括号括起来的区域）

and the last expression is the value of a block:

代码块的值是最后的一个表达式的值：

``` kotlin
val max = if (a > b) {
    print("Choose a")
    a
} else {
    print("Choose b")
    b
}
```

If you're using *if*{: .keyword } as an expression rather than a statement 

如果你把if用作表达式，而不是语句

(for example, returning its value or assigning it to a variable), 

（例如，返回值或给变量赋值），

the expression is required to have an `else` branch.

那表达式要求有个`else`分支。

See the [grammar for *if*{: .keyword }](grammar.md#if).

参见 [if 语法](grammar.md#if).

## When Expression When表达式

*when*{: .keyword } replaces the switch operator of C-like languages. 

when替代了类C语言的switch操作符。

In the simplest form it looks like this

最简单的用法如下

``` kotlin
when (x) {
    1 -> print("x == 1")
    2 -> print("x == 2")
    else -> { // Note the block 注意这个代码块
        print("x is neither 1 nor 2")
    }
}
```

*when*{: .keyword } matches its argument against all branches sequentially 

when逐个匹配分支里的参数，

until some branch condition is satisfied.

直到某个分支条件满足。

*when*{: .keyword } can be used either as an expression or as a statement. 

when可以用作表达式也可用作语句

If it is used as an expression, 

如果它被用作表达式，

the value of the satisfied branch becomes the value of the overall expression.

满足条件分支的值就成为整个表达式的值。

 If it is used as a statement, the values of individual branches are ignored. 
 
 如果它被用作语句，分支里的值就被忽略掉了。
 
 (Just like with *if*{: .keyword }, each branch can be a block, 
 
 （像if一样，每个分支也可以是代码块，
 
 and its value is the value of the last expression in the block.)
 
 代码块的值就是代码块中最后表达式的值。）
 
The *else*{: .keyword } branch is evaluated if none of the other branch conditions are satisfied.

如果其它分支条件都不满足，else分支就会被执行。

If *when*{: .keyword } is used as an expression, 

如果when被用作表达式，

the *else*{: .keyword } branch is mandatory,

就必须带有else分支，

unless the compiler can prove that all possible cases are covered with branch conditions.

除非编译器可以证明分支条件能覆盖所有可能的情况。

If many cases should be handled in the same way, 

如果许多情况都以相同的方式来处理的话，

the branch conditions may be combined with a comma:

分支条件可以用逗号组合在一起：

``` kotlin
when (x) {
    0, 1 -> print("x == 0 or x == 1")
    else -> print("otherwise")
}
```

We can use arbitrary expressions (not only constants) as branch conditions

可以使用任何表达式（不仅是常量）作为分支条件

``` kotlin
when (x) {
    parseInt(s) -> print("s encodes x")
    else -> print("s does not encode x")
}
```

We can also check a value for being *in*{: .keyword } or *!in*{: .keyword } a [range](ranges.md) or a collection:

也可以检查一个值是否in或者!in一个[范围](ranges.md)或一个集合：

``` kotlin
when (x) {
    in 1..10 -> print("x is in the range")
    in validNumbers -> print("x is valid")
    !in 10..20 -> print("x is outside the range")
    else -> print("none of the above")
}
```

Another possibility is to check that a value *is*{: .keyword } or *!is*{: .keyword } of a particular type. 

其它可能是，检查一个值is或者!is一种特定的类型

Note that, due to [smart casts](typecasts.md#smart-casts), 

注意，由于[智能转换](typecasts.md#smart-casts), 

you can access the methods and properties of the type without any extra checks.

你可以不用另外的检查就可以使用类型（类型属于一种对象）里的方法和属性。

```kotlin
fun hasPrefix(x: Any) = when(x) {
    is String -> x.startsWith("prefix")
    else -> false
}
```

*when*{: .keyword } can also be used as a replacement for an *if*{: .keyword }-*else*{: .keyword } *if*{: .keyword } chain.

when也可以替代if-else if代码链

If no argument is supplied, the branch conditions are simply boolean expressions, 

如果不带参数，分支条件就是简单的布尔值表达式，

and a branch is executed when its condition is true:

执行条件为ture的分支：

``` kotlin
when {
    x.isOdd() -> print("x is odd")
    x.isEven() -> print("x is even")
    else -> print("x is funny")
}
```

See the [grammar for *when*{: .keyword }](grammar.md#when).

参见 [when语法](grammar.md#when).


## For Loops For循环

*for*{: .keyword } loop iterates through anything that provides an iterator. The syntax is as follows:

for循环通过任何提供迭代器的对象来进行迭代。语法如下：

``` kotlin
for (item in collection) print(item)
```

The body can be a block.

主体可以是一个代码块。

``` kotlin
for (item: Int in ints) {
    // ...
}
```

As mentioned before, *for*{: .keyword } iterates through anything that provides an iterator, i.e.

正如之前所提到的，for通过提供迭代器的对象进行迭代，比如：

    * has a member- or extension-function `iterator()`, whose return type
    一个成员变量或扩展函数`iterator()`，它返回所定义类型
    * has a member- or extension-function `next()`, and
    一个成员变量或扩展函数`next()`，还有
    * has a member- or extension-function `hasNext()` that returns `Boolean`.
    一个成员变量或扩展函数`hasNext()`，返回`Boolean`类型值
    
All of these three functions need to be marked as `operator`.

这三个函数都需要标记为`operator`。

A `for` loop over an array is compiled to an index-based loop that does not create an iterator object.

对数组的for循环会被编译成一个基于索引的循环，不会创建迭代器对象。

If you want to iterate through an array or a list with an index, you can do it this way:

如果你想通过数组或带索引的列表进行迭代，你可以这样做：

``` kotlin
for (i in array.indices) {
    print(array[i])
}
```

Note that this "iteration through a range" is compiled down to optimal implementation with no extra objects created.

注意，这个“通过范围迭代”被编译成优化的方案，且不产生另外的对象。

Alternatively, you can use the `withIndex` library function:

或者，你可以使用`withIndex`库函数：

``` kotlin
for ((index, value) in array.withIndex()) {
    println("the element at $index is $value")
}
```

See the [grammar for *for*{: .keyword }](grammar.md#for).

参见 [for 语法](grammar.md#for).

## While Loops While循环

*while*{: .keyword } and *do*{: .keyword }..*while*{: .keyword } work as usual

while 和 do..while 像往常一样用

``` kotlin
while (x > 0) {
    x--
}

do {
    val y = retrieveData()
} while (y != null) // y is visible here! y在这里是可见的
```

See the [grammar for *while*{: .keyword }](grammar.md#while).

参见 [while 语法](grammar.md#while).

## Break and continue in loops 循环中的Break和continue

Kotlin supports traditional *break*{: .keyword } and *continue*{: .keyword } operators in loops. 

Kotlin支持传统的循环中的break和continue操作符。

See [Returns and jumps](returns.md).

参见 [返回和跳转](returns.md).

