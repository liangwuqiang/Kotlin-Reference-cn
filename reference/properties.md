---
type: doc
layout: reference
category: "Syntax"
title: "Properties and Fields"
---

# Properties and Fields 属性和字段

## Declaring Properties 属性声明

Classes in Kotlin can have properties.
在 Kotlin 中类可以有属性，

These can be declared as mutable, using the *var*{: .keyword } keyword 
它们可以声明为可变的，使用 *var* 关键字

or read-only using the *val*{: .keyword } keyword.
或者只读的，使用 *val* 关键字

``` kotlin
class Address {
    var name: String = ...
    var street: String = ...
    var city: String = ...
    var state: String? = ...
    var zip: String = ...
}
```

To use a property, we simply refer to it by name, as if it were a field in Java:
为了使用一个属性， 我们可以简单地通过名字引用它， 像在 java 中的字段那样：

``` kotlin
fun copyAddress(address: Address): Address {
    val result = Address() 
        // there's no 'new' keyword in Kotlin 
        // Kotlin没有'new'关键字
    result.name = address.name 
        // accessors are called 
        // 调用属性的存取  //# accessors 存取器
    result.street = address.street
    // ...
    return result
}
```

## Getters and Setters Getters和Setters

The full syntax for declaring a property is
声明一个属性的完整语法如下：

``` kotlin
var <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]
```

The initializer, getter and setter are optional. 
初始化语句，getter 和 setter 都是可选的。

Property type is optional if it can be inferred from the initializer
属性类型是可选的，如果它能从初始化语句中推测出来 //inferred 推测


(or from the getter return type, as shown below).
（或从getter返回值类型，如下所示）。

Examples:
例子：

``` kotlin
var allByDefault: Int? 
    // error: explicit initializer required, default getter and setter implied
    // 错误: 需要明确的初始化语句, 暗示使用默认的getter和setter方法  //# imply 暗示
var initialized = 1 
    // has type Int, default getter and setter
    // 类型为Int, 使用默认的getter和setter方法
```

The full syntax of a read-only property declaration differs from a mutable one in two ways: 
只读属性的声明完整语法 和可变属性的相比， 有两点不同: 

it starts with `val` instead of `var` and does not allow a setter:
它以`val`开头， 而不是`var`， 不允许setter方法：

``` kotlin
val simple: Int? 
    // has type Int, default getter, must be initialized in constructor
    // 类型为Int ，默认的getter方法 ，必须在构造方法中初始化
val inferredType = 1 
    // has type Int and a default getter
    // 类型为Int， 默认的getter方法
```
-----------
We can write custom accessors, 
我们可以写 自定义的属性存取方法，

very much like ordinary functions, 
就像普通函数那样，

right inside a property declaration. 
属性声明

Here's an example of a custom getter:
这里是一个例子， 自定义getter方法：

``` kotlin
val isEmpty: Boolean
    get() = this.size == 0  //# this.size是什么作用呢？
```

A custom setter looks like this:
自定义的setter方法看起来像这个样子:

``` kotlin
var stringRepresentation: String
    get() = this.toString()  //# this本来就是String,为什么还要.toString()呢？
    set(value) {
        setDataFromString(value) 
            // parses the string and assigns values to other properties
            // 
    }
```

By convention, the name of the setter parameter is `value`, 
为了方便起见, setter方法的参数名是 `value`,

but you can choose a different name if you prefer.
但是你也可以选择一个你自己喜欢的名称.

Since Kotlin 1.1, you can omit the property type if it can be inferred from the getter:
从Kotlin 1.1版开始， 你可以省略属性类型，如果它能被从getter方法中推测出来：

``` kotlin
val isEmpty get() = this.size == 0  
    // has type Boolean
    //  类型为Boolean
```

If you need to change the visibility of an accessor or to annotate it, 
如果你需要改变属性存取的可见性 或对它添加注释，

but don't need to change the default implementation,
但是不需要改变默认的实现，

you can define the accessor without defining its body:
你可以定义属性存取，而不定义它的方法体：

``` kotlin
var setterVisibility: String = "abc"
    private set 
        // the setter is private and has the default implementation
        // setter是私有的，并且有默认的实现

var setterWithAnnotation: Any? = null
    @Inject set 
        // annotate the setter with Inject
        // 使用Inject注释setter方法
```

### Backing Fields 备用的 fields（字段）

Classes in Kotlin cannot have fields. 
在kotlin中类不可以有字段。

However, sometimes it is necessary to have a backing field when using custom accessors. 
然而， 有时候必须有一个备用的字段，当我们使用自定义属性存取时。

For these purposes, Kotlin provides an automatic backing field 
出于这个目的， Kotlin 提供了一个自动的 备用 field （字段）

which can be accessed using the `field` identifier:
可以被访问， 使用 `field`标识符： 

``` kotlin
var counter = 0 
    // the initializer value is written directly to the backing field
    // 初始值被直接写入 备用的 field 中
    set(value) {
        if (value >= 0) field = value
    }
```

The `field` identifier can only be used in the accessors of the property.
`field` 标识符只能用于属性的存取方法中.


A backing field will be generated for a property 
属性就会自动产生一个备用的 field（字段） 

if it uses the default implementation of at least one of the accessors, 
如果属性使用了默认的实现，至少一个属性存取，

or if a custom accessor references it through the `field` identifier.
或者 如果一个自定义的属性存取方法 通过 `field` 标识符 引用它（field）


For example, in the following case there will be no backing field:
例如， 下面的情况下，就没有使用备用的字段：

``` kotlin
val isEmpty: Boolean
    get() = this.size == 0
    
//# 因为使用了val,所以没有setter方法；而getter方法中，又没有使用field（字段）
```

### Backing Properties 备用属性

If you want to do something that does not fit into this "implicit backing field" scheme, 
如果你想做某事， 不适合这个“隐含的使用备用field”方案，

you can always fall back to having a *backing property*:
你总可以退一步去使用 *备用的属性*：

``` kotlin
private var _table: Map<String, Int>? = null
public val table: Map<String, Int>
    get() {
        if (_table == null) {
            _table = HashMap() 
                // Type parameters are inferred
                // 类型是推导出来的
        }
        return _table ?: throw AssertionError("Set to null by another thread")
    }
    //# field = HashMap() ?: throw ... 这样不知道可以吗？
```

In all respects, this is just the same as in Java since access to private properties 
综合各方面， 这只是 和java相同的 访问私有属性的方法

with default getters and setters is optimized so that no function call overhead is introduced.
默认的getters和setters方法 已被优化， 以至于 没有函数能被从顶层引入来调用 //# 外部函数不能直接访问类属性

## Compile-Time Constants 编译时常量

Properties the value of which is known at compile time 
在编译时就知道的属性值

can be marked as _compile time constants_ using the `const` modifier.
可以 使用 `const` 修饰符 来标记为 _编译时常量_ 。

Such properties need to fulfil the following requirements:
这样的属性需要满足以下条件：

  * Top-level or member of an `object`
  * Top-level 或者 是一个`object`成员
  
  * Initialized with a value of type `String` or a primitive type
  * 使用 类型`String`值 或者基本类型 初始化
  
  * No custom getter
  * 没有自定义getter

Such properties can be used in annotations:
这样的属性可以被用于注释:

``` kotlin
const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"

@Deprecated(SUBSYSTEM_DEPRECATED) fun foo() { ... }  //# deprecated 弃用
```

---------------------------------------------------
## Late-Initialized Properties 延迟初始化属性

Normally, properties declared as having a non-null type must be initialized in the constructor.
However, fairly often this is not convenient. For example, properties can be initialized through dependency injection,
or in the setup method of a unit test. In this case, you cannot supply a non-null initializer in the constructor,
but you still want to avoid null checks when referencing the property inside the body of a class.
通常,那些被定义为拥有非空类型的属性,都需要在构造器中初始化.但有时候这并没有那么方便.例如在单元测试中,属性应该通过依赖注入进行初始化,
或者通过一个 setup 方法进行初始化.在这种条件下,你不能在构造器中提供一个非空的初始化语句,但是你仍然希望在访问这个属性的时候,避免非空检查.

To handle this case, you can mark the property with the `lateinit` modifier:
为了处理这种情况,你可以为这个属性加上 `lateinit` 修饰符

``` kotlin
public class MyTest {
    lateinit var subject: TestSubject

    @SetUp fun setup() {
        subject = TestSubject()
    }

    @Test fun test() {
        subject.method()  // dereference directly
    }
}
```

The modifier can only be used on `var` properties declared inside the body of a class (not in the primary constructor), and only
when the property does not have a custom getter or setter. The type of the property must be non-null, and it must not be
a primitive type.
这个修饰符只能够被用在类的 var 类型的可变属性定义中,不能用在构造方法中.并且属性不能有自定义的 getter 和 setter访问器.这个属性的类型必须是非空的,同样也不能为一个基本类型.

Accessing a `lateinit` property before it has been initialized throws a special exception that clearly identifies the property
being accessed and the fact that it hasn't been initialized.
在一个延迟初始化的属性初始化前访问他,会导致一个特定异常,告诉你访问的时候值还没有初始化.

## Overriding Properties 复写属性

See [Overriding Properties](classes.html#overriding-properties)

## Delegated Properties 代理属性
  
The most common kind of properties simply reads from (and maybe writes to) a backing field. 
On the other hand, with custom getters and setters one can implement any behaviour of a property.
Somewhere in between, there are certain common patterns of how a property may work. A few examples: lazy values,
reading from a map by a given key, accessing a database, notifying listener on access, etc.
最常见的属性就是从备用属性中读（或者写）。另一方面，自定义的 getter 和 setter 可以实现属性的任何操作。有些像懒值( lazy values )，根据给定的关键字从 map 中读出，读取数据库，通知一个监听者等等，像这些操作介于 getter setter 模式之间。

Such common behaviours can be implemented as libraries using [_delegated properties_](delegated-properties.html).
像这样常用操作可以通过代理属性作为库来实现。更多请参看[代理属性](delegated-properties.html)。