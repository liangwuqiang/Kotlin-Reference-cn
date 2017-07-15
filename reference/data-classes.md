---
type: doc
layout: reference
category: "Classes and Objects"
title: "Data Classes"
---

# Data Classes 数据类

We frequently create a class to do nothing but hold data. In such a class some standard functionality is often mechanically
derivable from the data. In Kotlin, this is called a _data class_ and is marked as `data`:
我们经常创建一个只保存数据的类。在这样的类中一些函数只是机械的对它们持有的数据进行一些推导。在 kotlin 中这样的类称之为 data 类，用 `data` 标注:

``` kotlin
data class User(val name: String, val age: Int)
```

The compiler automatically derives the following members from all properties declared in the primary constructor:
编译器会自动根据主构造函数中声明的所有属性添加如下方法：
  
  * `equals()`/`hashCode()` pair, 
  * `toString()` of the form `"User(name=John, age=42)"`,
  * [`componentN()` functions](multi-declarations.html) corresponding to the properties in their order of declaration,
  * `copy()` function (see below).
  
If any of these functions is explicitly defined in the class body or inherited from the base types, it will not be generated.
如果在类中明确声明或从基类继承了这些方法，编译器不会自动生成。

To ensure consistency and meaningful behavior of the generated code, data classes have to fulfil the following requirements:
为确保这些生成代码的一致性，并实现有意义的行为，数据类要满足下面的要求：

  * The primary constructor needs to have at least one parameter;
> 主构造函数应该至少有一个参数；

  * All primary constructor parameters need to be marked as `val` or `var`;
> 主构造函数的所有参数必须标注为 `val` 或者 `var` ；

  * Data classes cannot be abstract, open, sealed or inner;
> 数据类不能是 abstract，open，sealed，或者 inner ；

  * (before 1.1) Data classes may only implement interfaces.

> 数据类不能继承其它的类（但可以实现接口）。

  
Since 1.1, data classes may extend other classes (see [Sealed classes](sealed-classes.html) for examples).

On the JVM, if the generated class needs to have a parameterless constructor, default values for all properties have to be specified
(see [Constructors](classes.html#constructors)).
> 在 JVM 中如果构造函数是无参的，则所有的属性必须有默认的值，(参看[Constructors](http://kotlinlang.org/docs/reference/classes.html#constructors));

``` kotlin
data class User(val name: String = "", val age: Int = 0)
```

## Copying 复制
  
It's often the case that we need to copy an object altering _some_ of its properties, but keeping the rest unchanged. 
This is what `copy()` function is generated for. For the `User` class above, its implementation would be as follows:
我们经常会对一些属性做修改但想要其他部分不变。这就是 `copy()` 函数的由来。在上面的 User 类中，实现起来应该是这样：
     
``` kotlin
fun copy(name: String = this.name, age: Int = this.age) = User(name, age)     
```     

This allows us to write
有了 copy 我们就可以像下面这样写了：

``` kotlin
val jack = User(name = "Jack", age = 1)
val olderJack = jack.copy(age = 2)
```

## Data Classes and Destructuring Declarations 数据类和多重声明  //重构

_Component functions_ generated for data classes enable their use in [destructuring declarations](multi-declarations.html):
组件函数允许数据类在[多重声明](http://kotlinlang.org/docs/reference/multi-declarations.html)中使用：

``` kotlin
val jane = User("Jane", 35) 
val (name, age) = jane
println("$name, $age years of age") // prints "Jane, 35 years of age"
```

## Standard Data Classes 标准数据类

The standard library provides `Pair` and `Triple`. In most cases, though, named data classes are a better design choice, 
because they make the code more readable by providing meaningful names for properties.
标准库提供了 `Pair` 和 `Triple`。在大多数情形中，命名数据类是更好的设计选择，因为这样代码可读性更强而且提供了有意义的名字和属性。