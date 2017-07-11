---
type: doc
layout: reference
category: "Syntax"
title: "Basic Types"
---

# Basic Types || 基本类型

In Kotlin, everything is an object in the sense
 
在Kotlin中，从某种意义来讲，一切都是对象

that we can call member functions and properties on any variable.

我们通过变量的方式来调用对象的成员函数和成员属性。

Some types are built-in, because their implementation is optimized, 

有些数据类型已经固化在系统中，因为它们已经优化好了，

but to the user they look like ordinary classes. 

对用户来说，它们看起来像普通类。

In this section we describe most of these types:
 
在本节中，我们将来描述许多这样的数据类型： 
 
numbers, characters, booleans and arrays.

数值，字符，布尔值和数组。

## Numbers || 数值

Kotlin handles numbers in a way close to Java, 

Kotlin处理数值类型的方法和Java相似，

but not exactly the same. For example, 

但不完全相同。例如，

there are no implicit widening conversions for numbers,
 
数值类型不能进行隐式的扩充式的转换，
 
and literals are slightly different in some cases.

某些情况下，有些数值名称也略有不同。

Kotlin provides the following built-in types representing numbers 

Kotlin提供了如下固有的数值类型

(this is close to Java):

（和Java相似）

| Type	  类型    | Bit width 位宽 |
|----------------|----------------|
| Double  双精度  | 64             |
| Float	  单精度  | 32             |
| Long	  长整形  | 64             |
| Int	  整形    | 32             |
| Short	  短整型  | 16             |
| Byte	  字节    | 8              |

Note that characters are not numbers in Kotlin.

注意，字符类型不在Kotlin数值类型中

-------------------------------------------------------暂时到此

### Literal Constants 字符常量

There are the following kinds of literal constants for integral values:

有以下几种用于整形值的数值常量：

* Decimals: `123`
* 十进制数值：`123`
  * Longs are tagged by a capital `L`: `123L`
  * 长整型要标记大写`L`: `123L`
* Hexadecimals: `0x0F`
* 十六进制: `0x0F`
* Binaries: `0b00001011`
* 二进制: `0b00001011`

NOTE: Octal literals are not supported.

注意：不支持八进制数值。

Kotlin also supports a conventional notation for floating-point numbers:

Kotlin也支持传统的浮点数表示法：
 
* Doubles by default: `123.5`, `123.5e10`
* 默认情况下是双精度浮点数: `123.5`, `123.5e10`
* Floats are tagged by `f` or `F`: `123.5f`
* 单精度浮点数要标记`f`或`F`: `123.5f`
 
### Underscores in numeric literals (since 1.1) 数值常量中的下划线
 
You can use underscores to make number constants more readable:

可以使用下划线来使得数值常量便于阅读：

``` kotlin
val oneMillion = 1_000_000
val creditCardNumber = 1234_5678_9012_3456L
val socialSecurityNumber = 999_99_9999L
val hexBytes = 0xFF_EC_DE_5E
val bytes = 0b11010010_01101001_10010100_10010010
```

### Representation 类型的表达形式

On the Java platform, numbers are physically stored as JVM primitive types, 

在Java平台上，数值是被作为JVM（虚拟机）基本数据类型的方式来进行物理存储的，（内存中存储区域不同）

unless we need a nullable number reference (e.g. `Int?`) or generics are involved.
 
 除非我们需要的是可空数值引用（例如 `Int?`）或涉及到泛型。

In the latter cases numbers are boxed.

在后面的那种情况下，数值是被封装的。

Note that boxing of numbers does not necessarily preserve identity:

注意，封装的数值不保留ID：（封装的数值会重新分配一个内存地址）

``` kotlin
val a: Int = 10000
print(a === a) // Prints 'true'
val boxedA: Int? = a
val anotherBoxedA: Int? = a
print(boxedA === anotherBoxedA) // !!!Prints 'false'!!!
```

On the other hand, it preserves equality:

另一方面，它保留相等：（内容上相同，存储地址不同）

``` kotlin
val a: Int = 10000
print(a == a) // Prints 'true'
val boxedA: Int? = a
val anotherBoxedA: Int? = a
print(boxedA == anotherBoxedA) // Prints 'true'
```

### Explicit Conversions 明确的转换

Due to different representations, smaller types are not subtypes of bigger ones.

由于类型的表达形式不同，短类型的数据并不是其长类型数据的子类。

If they were, we would have troubles of the following sort:

假设有的话，我们将会遇到以下的问题：

``` kotlin
// Hypothetical code, does not actually compile:
// 假想的代码，编译不能通过：
val a: Int? = 1 // A boxed Int (java.lang.Integer) 一个Int封装
val b: Long? = a // implicit conversion yields a boxed Long (java.lang.Long)  悄悄地转成了Long封装
print(a == b) // Surprise! This prints "false" as Long's equals() check for other part to be Long as well
// 奇怪! 打印出了"false"，因为Long类型的equals()检查要求其他部分也是Long类型 
```
So not only identity, but even equality would have been lost silently all over the place.

所以不仅是ID不同（内存地址），而且等式也同样不能成立（数值内容）

As a consequence, smaller types are NOT implicitly converted to bigger types.

因此，较短类型数值不能隐式转成较长类型数据。

This means that we cannot assign a value of type `Byte` to an `Int` variable without an explicit conversion

这意味着我们没有通过显式转换，就不能把`Byte`类型的变量直接指定为`Int`类型的变量

``` kotlin
val b: Byte = 1 // OK, literals are checked statically 静态地检查数值常量
val i: Int = b // ERROR 错误
```

We can use explicit conversions to widen numbers

我们可以使用显式转换来改变数值类型

``` kotlin
val i: Int = b.toInt() // OK: explicitly widened 明确地转换
```

Every number type supports the following conversions:

每种数据类型都支持以下的转换：

* `toByte(): Byte`
* `toShort(): Short`
* `toInt(): Int`
* `toLong(): Long`
* `toFloat(): Float`
* `toDouble(): Double`
* `toChar(): Char`

Absence of implicit conversions is rarely noticeable 

隐式转换是很少被注意到的，

because the type is inferred from the context, 

因为变量类型可通过上下文推导出来，

and arithmetical operations are overloaded for appropriate conversions, for example

而且算术运算会为合适的转换进行重载，例如

``` kotlin
val l = 1L + 3 // Long + Int => Long Long类型 + Int类型 => Long类型
```

### Operations 运算符

Kotlin supports the standard set of arithmetical operations over numbers, 

Kotlin在数值运算上支持算术运算的标准集

which are declared as members of appropriate classes 

它们被声明为相应的类的成员

(but the compiler optimizes the calls down to the corresponding instructions).

（但是编译器优化这些调用为相应的指令）

See [Operator overloading](operator-overloading.md).

参见[运算符重载](operator-overloading.md).

As of bitwise operations, there're no special characters for them, 

对于位操作符，没有为它们提供特别的操作符，

but just named functions that can be called in infix form, for example:

但是只有功能名称，可以以中缀形式被调用，例如：

``` kotlin
val x = (1 shl 2) and 0x000FF000
```

Here is the complete list of bitwise operations (available for `Int` and `Long` only):

这里是全部的位运算符（只能用于`Int`类型和`Long`类型）

* `shl(bits)` – signed shift left (Java's `<<`)
* `shr(bits)` – signed shift right (Java's `>>`)
* `ushr(bits)` – unsigned shift right (Java's `>>>`)
* `and(bits)` – bitwise and
* `or(bits)` – bitwise or
* `xor(bits)` – bitwise xor
* `inv()` – bitwise inversion

## Characters 字符

Characters are represented by the type `Char`. They can not be treated directly as numbers

字符是用`Char`来表示。它们不能直接作为数值来使用。

``` kotlin
fun check(c: Char) {
    if (c == 1) { // ERROR: incompatible types 错误：类型不匹配
        // ...
    }
}
```

Character literals go in single quotes: `'1'`.

字符文字放在单引号内：`'1'`。

Special characters can be escaped using a backslash.

特殊的字符可以用反斜杠来转义

The following escape sequences are supported: `\t`, `\b`, `\n`, `\r`, `\'`, `\"`, `\\` and `\$`.

下面字符序列支持转义: `\t`, `\b`, `\n`, `\r`, `\'`, `\"`, `\\` 和 `\$`.

To encode any other character, use the Unicode escape sequence syntax: `'\uFF00'`.

要编码其他字符，就使用Unicode转义序列语法：`'\uFF00'`

We can explicitly convert a character to an `Int` number:

我们可以将一个字符显式转义为一个`Int`数值

``` kotlin
fun decimalDigitValue(c: Char): Int {
    if (c !in '0'..'9')
        throw IllegalArgumentException("Out of range")
    return c.toInt() - '0'.toInt() // Explicit conversions to numbers 显式转成数值
}
```

Like numbers, characters are boxed when a nullable reference is needed. 

像数值类型一样，需要空值引用时，字符类型就被封装起来。

Identity is not preserved by the boxing operation.

ID值（内存地址）在封装后，不会保留下来。（产生了一个新的ID值）

## Booleans 布尔值

The type `Boolean` represents booleans, and has two values: *true*{: .keyword } and *false*{: .keyword }.

`Boolean`类型表示布尔值，它有两个值ture和false

Booleans are boxed if a nullable reference is needed.

如果需要空值引用，布尔值就要被封装起来。

Built-in operations on booleans include

内建的布尔值操作符包括

* `||` – lazy disjunction 逻辑或
* `&&` – lazy conjunction 逻辑与
* `!` - negation  取反

## Arrays 数组

Arrays in Kotlin are represented by the `Array` class, 

在Kotlin中，数组是用`Array`类来表示的，

that has `get` and `set` functions (that turn into `[]` by operator overloading conventions), 

它有`get`和`set`函数（通过运算符重载转换转成`[]`的形式）。

and `size` property, along with a few other useful member functions:

和`size`属性，还有一些其它有用的成员函数：

``` kotlin
class Array<T> private constructor() {
    val size: Int
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T): Unit

    operator fun iterator(): Iterator<T>
    // ...
}
```

To create an array, we can use a library function `arrayOf()`
 
可以使用库函数`arrayOf()`来创建一个数组
 
and pass the item values to it, 

并给它传递项目值，

so that `arrayOf(1, 2, 3)` creates an array [1, 2, 3].

因此`arrayOf(1, 2, 3)`可以创建数组 [1, 2, 3]。

Alternatively, the `arrayOfNulls()` library function can be used to create an array of a given size filled with null elements.

或者，使用`arrayOfNulls()`库函数来创建一个给定大小，用空元素填充的数组。

Another option is to use a factory function that takes the array size and the function that can return the initial value

其它的选择是使用工厂函数。取得数组大小和能够返回初始值的函数， 

of each array element given its index:

通过每个元素所给定它的索引值：

``` kotlin
// Creates an Array<String> with values ["0", "1", "4", "9", "16"] 创建一个数组["0", "1", "4", "9", "16"]
val asc = Array(5, { i -> (i * i).toString() })
```

As we said above, the `[]` operation stands for calls to member functions `get()` and `set()`.

正如我们上面所说，`[]`操作符可以替换调用成员函数`get()`和`set()`。

Note: unlike Java, arrays in Kotlin are invariant. 

注意：和Java不一样，Kotlin中数组是不变的。

This means that Kotlin does not let us assign an `Array<String>`to an `Array<Any>`, 

意思是Kotlin不让我们指定`Array<String>`为`Array<Any>`，

which prevents a possible runtime failure (but you can use `Array<out Any>`, 

防止可能的运行时失败（但是你可以使用`Array<out Any>`），

see [Type Projections](generics.md#type-projections)).

参见 [项目类型](generics.md#type-projections))。

Kotlin also has specialized classes to represent arrays of primitive types without boxing overhead: 

Kotlin也有专门的类来表示没有封装的原始类型的数组：

`ByteArray`,`ShortArray`, `IntArray` and so on. 

`ByteArray`,`ShortArray`, `IntArray`等等。

These classes have no inheritance relation to the `Array` class, 

这些类和`Array`类没有继承关系，

but they have the same set of methods and properties. 

但是它们有同样的方法集和属性集。

Each of them also has a corresponding factory function:

它们也都有相应的工厂函数：

``` kotlin
val x: IntArray = intArrayOf(1, 2, 3)
x[0] = x[1] + x[2]
```

## Strings 字符串

Strings are represented by the type `String`. Strings are immutable.

`String`类型表示字符，字符串是不变的。

Elements of a string are characters that can be accessed by the indexing operation: `s[i]`.

字符串元素是字符，可以通过索引操作`s[i]`来访问。

A string can be iterated over with a *for*{: .keyword }-loop:

字符串可以用for循环来迭代：

``` kotlin
for (c in str) {
    println(c)
}
```

### String Literals 

Kotlin has two types of string literals: 

Kotlin有两种类型的字符串

escaped strings that may have escaped characters in them and raw strings that can contain newlines and arbitrary text. 

具有转义字符的转义字符串 和 包含换行符和任意文本的原始字符串。

An escaped string is very much like a Java string:

转义字符串就像Java的字符串：

``` kotlin
val s = "Hello, world!\n"
```

Escaping is done in the conventional way, with a backslash. 

转义使用传统的反斜杠方式

See [Characters](#characters) above for the list of supported escape sequences.

参见 上面的[特性](#characters) 寻找所支持的转义清单。

A raw string is delimited by a triple quote (`"""`), 

原始字符串是由三个引号(`"""`)来限定的

contains no escaping and can contain newlines and any other characters:

不包含转义词，可以包含换行符和任何其他字符：

``` kotlin
val text = """
    for (c in "foo")
        print(c)
"""
```

You can remove leading whitespace with [trimMargin()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/trim-margin.html) function:

你可以使用[trimMargin()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/trim-margin.html)函数去掉前导空格:

``` kotlin
val text = """
    |Tell me and I forget.
    |Teach me and I remember.
    |Involve me and I learn.
    |(Benjamin Franklin)
    """.trimMargin()
```

By default `|` is used as margin prefix, 

`|`被默认用作边界前缀

but you can choose another character and pass it as a parameter, like `trimMargin(">")`.

但是你可以选择其它字符，并作为参数传递，就像`trimMargin(">")`这样。

### String Templates 字符串模板

Strings may contain template expressions, i.e. 

字符串可以包含模板表达式，例如

pieces of code that are evaluated and whose results are concatenated into the string.

要求计算的代码片段，其结果需要串接到字符串中。

A template expression starts with a dollar sign ($) and consists of either a simple name:

模板表达式使用美元标记($)开始，由简单的名字组成：

``` kotlin
val i = 10
val s = "i = $i" // evaluates to "i = 10" 计算结果为"i = 10"
```

or an arbitrary expression in curly braces:

或者是一对大括号内的任意表达式：

``` kotlin
val s = "abc"
val str = "$s.length is ${s.length}" // evaluates to "abc.length is 3"
```

Templates are supported both inside raw strings and inside escaped strings.

模板支持内置原始字符串和内置转义字符串两种。

If you need to represent a literal `$` character in a raw string (which doesn't support backslash escaping), 

如果你需要在原始字符串（不支持反斜杠的转义词）中表达文字`$`字符，

you can use the following syntax:

你可以使用下面的语法：

``` kotlin
val price = """
${'$'}9.99
"""
```
