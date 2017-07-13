---
type: doc
layout: reference
category: "Syntax"
title: "Classes and Inheritance"
related:
    - functions.md
    - nested-classes.md
    - interfaces.md
---

# Classes and Inheritance 类和继承

## Classes 类

Classes in Kotlin are declared using the keyword *class*{: .keyword }:
在 Kotlin 中类用 *class* 声明：

``` kotlin
class Invoice {
}
```

The class declaration consists of the class name, 
类的声明包含类名，

the class header (specifying its type parameters, the primary constructor etc.) 
类头(指定类型参数，主构造函数等等)，

and the class body, surrounded by curly braces. 
以及类主体，用大括号包裹。  //curly 卷毛的 //braces 背带，吊带 //花括号

Both the header and the body are optional;
类头和类体是可选的；

if the class has no body, curly braces can be omitted.
如果没有类体可以省略大括号。

``` kotlin
class Empty
```

### Constructors 构造函数

A class in Kotlin can have a **primary constructor** and one or more **secondary constructors**. 
在 Kotlin 中类可以有一个 **主构造函数** 以及一个或多个 **次级构造函数**。

The primary constructor is part of the class header: 
主构造函数是类头的一部分：

it goes after the class name (and optional type parameters).
跟在类名后面(可选类型参数)。

``` kotlin
class Person constructor(firstName: String) {
}
```

If the primary constructor does not have any annotations or visibility modifiers, 
如果主构造函数没有注释或可见性修饰符，

the *constructor*{: .keyword } keyword can be omitted:
则 *constructor* 关键字可以省略：

``` kotlin
class Person(firstName: String) {
}
```

The primary constructor cannot contain any code. 
主构造函数不能包含任意代码。

Initialization code can be placed in **initializer blocks**, 
初始化代码可以放在 **初始化块** 内，

which are prefixed with the *init*{: .keyword } keyword:
以 *init* 关键字做前缀：

``` kotlin
class Customer(name: String) {
    init {
        logger.info("Customer initialized with value ${name}")
    }
}
```

Note that parameters of the primary constructor can be used in the initializer blocks. 
注意，主构造函数的参数可以用在初始化块内，

They can also be used in property initializers declared in the class body:
（参数）也可以用于类体中的属性初始化声明：

``` kotlin
class Customer(name: String) {
    val customerKey = name.toUpperCase()
}
```

In fact, for declaring properties and initializing them from the primary constructor, 
事实上，属性声明和初始化可以在主构造函数中进行,

Kotlin has a concise syntax:
Kotlin 有个简洁的语法：

``` kotlin
class Person(val firstName: String, val lastName: String, var age: Int) {
    // ...
}
```

Much the same way as regular properties, 
像普通的属性，

the properties declared in the primary constructor can be mutable (*var*{: .keyword }) 
or read-only (*val*{: .keyword }).
在主构造函数中的属性声明可以是可变（*var*）或只读（*val*）。

If the constructor has annotations or visibility modifiers, 
如果构造函数有注释或可见性修饰符，

the *constructor*{: .keyword } keyword is required, 
则 *constructor* 关键字是必须的，

and the modifiers go before it:
并且修饰符放在它前面：

``` kotlin
class Customer public @Inject constructor(name: String) { ... }
```

For more details, see [Visibility Modifiers](visibility-modifiers.html#constructors).
详见[可见性修饰符](visibility-modifiers.html#constructors)。


#### Secondary Constructors 次级构造函数

The class can also declare **secondary constructors**, 
类也可以声明 **次级构造函数**，

which are prefixed with *constructor*{: .keyword }:
需要加前缀 *constructor*:

``` kotlin
class Person {
    constructor(parent: Person) {
        parent.children.add(this)
    }
}
```

If the class has a primary constructor, 
如果类有主构造函数，

each secondary constructor needs to delegate to the primary constructor, 
每个次级构造函数需要引用主构造函数，  //delegate 委派，代表

either directly or indirectly through another secondary constructor(s). 
或直接或间接引用另一个次级构造函数。

Delegation to another constructor of the same class is done using the *this*{: .keyword } keyword:
在同一个类中引用另一个构造函数 使用 *this* 关键字：

``` kotlin
class Person(val name: String) {
    constructor(name: String, parent: Person) : this(name) {
        parent.children.add(this)
    }
}
```

If a non-abstract class does not declare any constructors (primary or secondary), 
如果一个非抽象类没有声明构造函数(主构造函数或次级构造函数)，

it will have a generated primary constructor with no arguments. 
它会产生一个没有参数的主构造函数。

The visibility of the constructor will be public. 
构造函数的可见性是 public 。

If you do not want your class to have a public constructor, 
如果你不想你的类有公共的构造函数，

you need to declare an empty primary constructor with non-default visibility:
你就得声明一个空的主构造函数，并带上可见性标识：

``` kotlin
class DontCreateMe private constructor () {
}
```

> **NOTE**: On the JVM, if all of the parameters of the primary constructor have default values, 
> **注意**：在 JVM 虚拟机中，如果主构造函数的所有参数都有默认值，

> the compiler will generate an additional parameterless constructor which will use the default values. 
> 编译器会生成一个附加的无参的构造函数，这个构造函数会直接使用默认值。

> This makes it easier to use Kotlin with libraries such as Jackson or JPA 
> 这使得 Kotlin 可以更容易地使用像 Jackson 或者 JPA 这样的库

> that create class instances through parameterless constructors.
> 这样通过无参构造函数来创建类实例。
>
> ``` kotlin
> class Customer(val customerName: String = "")
> ```
{:.info}

### Creating instances of classes 创建类的实例

To create an instance of a class, we call the constructor as if it were a regular function:
我们可以像调用普通函数那样调用构造函数来创建类实例：

``` kotlin
val invoice = Invoice()

val customer = Customer("Joe Smith")
```

Note that Kotlin does not have a *new*{: .keyword } keyword.
注意， Kotlin 没有 *new* 关键字。

Creating instances of nested, inner and anonymous inner classes 
is described in [Nested classes](nested-classes.html).
创建嵌套类、内部类和匿名内部类的实例， 在[嵌套类]有说明。

### Class Members 类成员

Classes can contain
类可以包含：

* Constructors and initializer blocks
* 构造函数和初始化代码块

* [Functions](functions.html)
* [Properties](properties.html)
* [Nested and Inner Classes](nested-classes.html)
* [Object Declarations](object-declarations.html)


## Inheritance 继承

All classes in Kotlin have a common superclass `Any`, 
Kotlin 中所有的类都有一个共同的父类 Any ，

that is a default super for a class with no supertypes declared:
下面是一个使用默认父类的类， 没有父类声明：

``` kotlin
class Example // Implicitly inherits from Any 隐式继承于Any
```

`Any` is not `java.lang.Object`; 
`Any` 不是 `java.lang.Object`；

in particular, it does not have any members other than `equals()`, `hashCode()` and `toString()`.
事实上，它除了 `equals()`,`hashCode()`以及`toString()`外没有任何成员了。


Please consult the [Java interoperability](java-interop.html#object-methods) section for more details.
参看[ Java 交互性](java-interop.html#object-methods)了解更多详情。  //conslut 查阅

To declare an explicit supertype, we place the type after a colon in the class header:
为了声明一个明确的父类，我们需要在类头后加冒号之后再加父类：

``` kotlin
open class Base(p: Int)

class Derived(p: Int) : Base(p)
```

If the class has a primary constructor, 
如果类有主构造函数，

the base type can (and must) be initialized right there,
基类可以（并且必须）在这里初始化，

using the parameters of the primary constructor.
使用主构造函数的参数。


If the class has no primary constructor, 
如果类没有主构造函数，

then each secondary constructor has to initialize the base type using the *super*{: .keyword } keyword, 
则每一个次级构造函数不得不用 *super* 关键字来初始化基类，

or to delegate to another constructor which does that.
或者去引用另一个已经这么干了的构造函数。

Note that in this case different secondary constructors can call different constructors of the base type:
注意，在这种情形中，不同的次级构造函数可以调用基类不同的构造函数：

``` kotlin
class MyView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
}
```

The *open*{: .keyword } annotation on a class is the opposite of Java's *final*{: .keyword }: 
类中的 *open* 注解与java 中的 *final* 相反:

it allows others to inherit from this class. 
它允许别的类继承这个类。

By default, all classes in Kotlin are final, 
默认情形下，kotlin 中所有的类都是 final ,

which corresponds to [Effective Java](http://www.oracle.com/technetwork/java/effectivejava-136174.html),
与[Effective Java](http://www.oracle.com/technetwork/java/effectivejava-136174.html)相对应，

Item 17: *Design and document for inheritance or else prohibit it*.
项目17： *继承或其它禁用继承的设计和文档* 。

### Overriding Methods 复写成员

As we mentioned before, we stick to making things explicit in Kotlin. 
像之前提到的，我们在 kotlin 中坚持做事明朗化。

And unlike Java, Kotlin requires explicit annotations 
for overridable members (we call them *open*) and for overrides:
不像 java ，kotlin要求对可复写的成员明确注解（我们称之为 *open* ），并使用 overrides： 

``` kotlin
open class Base {
    open fun v() {}
    fun nv() {}
}
class Derived() : Base() {
    override fun v() {}
}
```

The *override*{: .keyword } annotation is required for `Derived.v()`. 
对于 `Derived.v()` 来说 *override* 注解是必须的。

If it were missing, the compiler would complain.
如果没有加的话，编译器会提示。

If there is no *open*{: .keyword } annotation on a function, like `Base.nv()`, 
如果函数没有 *open* 注解，像 `Base.nv()` ,

declaring a method with the same signature in a subclass is illegal,
在子类中声明一个同样的函数是不合法的，

either with *override*{: .keyword } or without it. 
要么加 override 要么不要复写。

In a final class (e.g. a class with no *open*{: .keyword } annotation), 
在final类(例如:没有open注解的类)中，

open members are prohibited.
open类型的成员是不允许的。


A member marked *override*{: .keyword } is itself open, 
标记为 *override* 的成员本身就是open的，

i.e. it may be overridden in subclasses. 
例如，它可以在子类中被复写。

If you want to prohibit re-overriding, use *final*{: .keyword }:
如果你不想被重写，就要加 *final* :

``` kotlin
open class AnotherDerived() : Base() {
    final override fun v() {}
}
```

### Overriding Properties 复写属性

Overriding properties works in a similar way to overriding methods; 
复写属性和复写方法一样；

properties declared on a superclass 
父类中的属性声明

that are then redeclared on a derived class must be prefaced with *override*{: .keyword }, 
在子类中重新声明，必须前置 *override* ,

and they must have a compatible type. 
并且它们的类型必须兼容。

Each declared property can be overridden by a property with an initializer or by a property with a getter method.
每个已声明的属性都可以被复写，通过初始化属性值 或者 通过给属性指定getter方法

``` kotlin
open class Foo {
    open val x: Int get { ... }
}

class Bar1 : Foo() {
    override val x: Int = ...
}
```

You can also override a `val` property with a `var` property, 
你也可以把一个 `val` 属性复写为一个 `var` 属性，

but not vice versa. 
但是不能反过来使用。

This is allowed because a `val` property essentially declares a getter method, 
这是允许的，因为 `val` 属性本来就声明了一个getter方法，

and overriding it as a `var` additionally declares a setter method in the derived class.
将它复写为 `var` 额外在子类中声明一个setter方法。

Note that you can use the *override*{: .keyword } keyword 
as part of the property declaration in a primary constructor.
注意，你可以在主构造函数中使用 *override* 关键字作为属性声明的一部分。

``` kotlin 
interface Foo {
    val count: Int
}

class Bar1(override val count: Int) : Foo

class Bar2 : Foo {
    override var count: Int = 0
}
```

### Overriding Rules 复写规则

In Kotlin, implementation inheritance is regulated by the following rule: 
在 kotlin 中，实现继承通常遵循如下规则：

if a class inherits many implementations of the same member from its immediate superclasses,
如果一个类从它的直接父类中继承了同名成员的多个实现，

it must override this member and provide its own implementation (perhaps, using one of the inherited ones).
那么它必须复写这个成员并且提供它自己的实现(或许，直接使用其中的一个继承的成员)。


To denote the supertype from which the inherited implementation is taken, 
为表示采用了哪个父类中继承的实现方法， 

we use *super*{: .keyword } qualified by the supertype name in angle brackets, e.g. `super<Base>`:
我们用 *super* 和 加尖括号的父类名， 例如 `super<Base>`:

``` kotlin
open class A {
    open fun f() { print("A") }
    fun a() { print("a") }
}

interface B {
    fun f() { print("B") } 
    // interface members are 'open' by default
    // 接口成员默认式 open 的
    fun b() { print("b") }
}

class C() : A(), B {
    // The compiler requires f() to be overridden:
    // 编译器要求复写 f()
    override fun f() {
        super<A>.f() // call to A.f()
        super<B>.f() // call to B.f()
    }
}
```

It's fine to inherit from both `A` and `B`, 
可以正常地从 A 和 B 中继承，

and we have no problems with `a()` and `b()` 
对于 `a()` 和 `b()` ，没有任何问题 

since `C` inherits only one implementation of each of these functions.
因为 `C` 继承这些函数只有一个实现方法。  

But for `f()` we have two implementations inherited by `C`, 
但是 `C` 中继承的 `f()` 有两个实现，

and thus we have to override `f()` in `C`
因此，我们必须在 `C` 中复写 `f()`

and provide our own implementation that eliminates the ambiguity.
并且提供自己的实现来消除歧义。 //eliminates 消除 //ambiguity 含糊

## Abstract Classes 抽象类

A class and some of its members may be declared *abstract*{: .keyword }.
一个类或一些成员可能被声明成 *abstract* (抽象) 。

An abstract member does not have an implementation in its class.
一个抽象方法在它的类中没有实现方法。

Note that we do not need to annotate an abstract class or function with open – it goes without saying.
注意，我们不需要给一个抽象类或函数添加 open 注解，它默认是带着的。

We can override a non-abstract open member with an abstract one
我们可以用一个抽象成员去复写一个非抽象 open 成员。

``` kotlin
open class Base {
    open fun f() {}
}

abstract class Derived : Base() {
    override abstract fun f()
}
```

## Companion Objects 伴随对象

In Kotlin, unlike Java or C#, classes do not have static methods. 
在 kotlin 中，不像 java 或者 C#， 类没有静态方法。

In most cases, it's recommended to simply use package-level functions instead.
在大多数情形下，建议简单地使用包级别的函数来代替。 //就是函数定义在类外


If you need to write a function that can be called without having a class instance 
如果你要写一个 没有类实例 就可以调用的函数，

but needs access to the internals of a class (for example, a factory method), 
但需要访问到 类内部(比如，一个工厂方法)，

you can write it as a member of an [object declaration](object-declarations.html) inside that class.
你可以把它写成一个 类内部的[对象声明](object-declarations.html) 的成员

Even more specifically, 
更高效的方法是，

if you declare a [companion object](object-declarations.html#companion-objects) inside your class,
如果你在类内部声明一个[伴随对象](object-declarations.html#companion-objects)

you'll be able to call its members with the same syntax as calling static methods in Java/C#, 
你就可以调用它的成员，使用在java/c#中，调用静态方法同样的语法，

using only the class name as a qualifier.
只使用类名作为标识。

（完 2017-7-13）