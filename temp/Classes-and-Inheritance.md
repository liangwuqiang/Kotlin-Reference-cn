
我们可以像使用普通函数那样使用构造函数创建类实例：

```kotlin
val invoice = Invoice()
val customer = Customer("Joe Smith")
```

注意 Kotlin 没有 new 关键字。

### 类成员
类可以包含：
>构造函数和初始化代码块

>[函数](FunctionsAndLambdas/Functions.md) 

>[属性](ClassesAndObjects/Properties-and-Filds.md)　

>[内部类](ClassesAndObjects/NestedClasses.md) 

>[对象声明](ClassesAndObjects/ObjectExpressicAndDeclarations.md) 

### 继承
Kotlin 中所有的类都有共同的父类 Any ，下面是一个没有父类声明的类：

```kotlin
class Example //　隐式继承于 Any
```

`Any` 不是 `java.lang.Object`；事实上它除了 `equals()`,`hashCode()`以及`toString()`外没有任何成员了。参看[ Java interoperability]( Java interoperability)了解更多详情。

声明一个明确的父类，需要在类头后加冒号再加父类：

```kotlin
open class Base(p: Int)

class Derived(p: Int) : Base(p)
```

如果类有主构造函数，则基类可以而且是必须在主构造函数中立即初始化。

如果类没有主构造函数，则必须在每一个构造函数中用 super 关键字初始化基类，或者在代理另一个构造函数做这件事。注意在这种情形中不同的二级构造函数可以调用基类不同的构造方法：

```kotlin
class MyView : View {
	constructor(ctx: Context) : super(ctx) {
	}
	constructor(ctx: Context, attrs: AttributeSet) : super(ctx,attrs) {
	}
}
```

open 注解与java 中的 final相反:它允许别的类继承这个类。默认情形下，kotlin 中所有的类都是 final ,对应 [Effective Java](http://www.oracle.com/technetwork/java/effectivejava-136174.html) ：Design and document for inheritance or else prohibit it.

### 复写成员
像之前提到的，我们在 kotlin 中坚持做明确的事。不像 java ，kotlin 需要把可以复写的成员都明确注解出来，并且重写它们：

```kotlin
open class Base {
	open fun v() {}
	fun nv() {}
}

class Derived() : Base() {
	override fun v() {}
}
```

对于 `Derived.v()` 来说 override 注解是必须的。如果没有加的话，编译器会提示。如果没有 open 注解，像 `Base.nv()` ,在子类中声明一个同样的函数是不合法的，要么加 override 要么不要复写。在 final 类(就是没有open注解的类)中，open 类型的成员是不允许的。

标记为 override 的成员是 open的，它可以在子类中被复写。如果你不想被重写就要加 final:

```kotlin
open class AnotherDerived() : Base() {
	final override fun v() {}
}
```

**等等！我现在怎么hack我的库？！**

有个问题就是如何复写子类中那些作者不想被重写的类，下面介绍一些令人讨厌的方案。

我们认为这是不好的，原因如下：

> 最好的实践建议你不应给做这些 hack

>人们可以用其他的语言成功做到类似的事情

>如果你真的想 hack 那么你可以在 java 中写好 hack 方案，然后在 kotlin 中调用 (参看[java调用](http://kotlinlang.org/docs/reference/java-interop.html))，专业的构架可以很好的做到这一点

### 复写规则
在 kotlin 中，实现继承通常遵循如下规则：如果一个类从它的直接父类继承了同一个成员的多个实现，那么它必须复写这个成员并且提供自己的实现(或许只是直接用了继承来的实现)。为表示使用父类中提供的方法我们用 `super<Base>`表示:

```kotlin
open class A {
	open fun f () { print("A") }
	fun a() { print("a") }
}

interface B {
	fun f() { print("B") } //接口的成员变量默认是 open 的
	fun b() { print("b") }
}

class C() : A() , B{
	override fun f() {
		super<A>.f()//调用 A.f()
		super<B>.f()//调用 B.f()
	}
}
```

可以同时从 A B 中继承方法，而且 C 继承 a() 或 b() 的实现没有任何问题，因为它们都只有一个实现。但是 f() 有俩个实现，因此我们在 C 中必须复写 f() 并且提供自己的实现来消除歧义。

### 抽象类
一个类或一些成员可能被声明成 abstract 。一个抽象方法在它的类中没有实现方法。记住我们不用给一个抽象类或函数添加 open 注解，它默认是带着的。

我们可以用一个抽象成员去复写一个带 open 注解的非抽象方法。

```kotlin
open class Base {
	open fun f() {}
}

abstract class Derived : Base() {
	override abstract fun f()
}
```

### 伴随对象
在 kotlin 中不像 java 或者 C# 它没有静态方法。在大多数情形下，我们建议只用包级别的函数。

如果你要写一个没有实例类就可以调用的方法，但需要访问到类内部(比如说一个工厂方法)，你可以把它写成它所在类的一个成员(you can write it as a member of an object declaration inside that class)

更高效的方法是，你可以在你的类中声明一个[伴随对象](http://kotlinlang.org/docs/reference/object-declarations.html#companion-objects)，这样你就可以像 java/c# 那样把它当做静态方法调用，只需要它的类名做一个识别就好了

### 密封类
密封类用于代表严格的类结构，值只能是有限集合中的某中类型，不可以是任何其它类型。这就相当于一个枚举类的扩展：枚举值集合的类型是严格限制的，但每个枚举常量只有一个实例，而密封类的子类可以有包含不同状态的多个实例。

声明密封类需要在 class 前加一个 sealed 修饰符。密封类可以有子类但必须全部嵌套在密封类声明内部、

```Kotlin
sealed class Expr {
	class Const(val number: Double) : Expr() 
	class Sum(val e1: Expr, val e2: Expr) : Expr() 
	object NotANumber : Expr()
}
```

注意密封类子类的扩展可以在任何地方，不必在密封类声明内部进行。

使用密封类的最主要的的好处体现在你使用 [when 表达式]()。可以确保声明可以覆盖到所有的情形，不需要再使用 else 情形。

```Kotlin
fun eval(expr: Expr): Double = when(expr) { 
	is Const -> expr.number
	is Sum -> eval(expr.e1) + eval(expr.e2) 
	NotANumber -> Double.NaN
    // the `else` clause is not required because we've covered all the cases
}
```
