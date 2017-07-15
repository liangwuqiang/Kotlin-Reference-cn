---
type: doc
layout: reference
category: "Syntax"
title: "Delegated Properties"
---

# Delegated Properties 代理属性

There are certain common kinds of properties, that, though we can implement them manually every time we need them, 
would be very nice to implement once and for all, and put into a library. Examples include
很多常用属性，虽然我们可以在需要的时候手动实现它们，但更好的办法是一次实现多次使用，并放到库。比如：

* lazy properties: the value gets computed only upon first access,
* observable properties: listeners get notified about changes to this property,
* storing properties in a map, instead of a separate field for each property.
> 延迟属性：只在第一次访问是计算它的值
>观察属性：监听者从这获取这个属性更新的通知
>在 map 中存储的属性，而不是单独存在分开的字段

To cover these (and other) cases, Kotlin supports _delegated properties_:
为了满足这些情形，Kotllin 支持代理属性：

``` kotlin
class Example {
    var p: String by Delegate()
}
```

The syntax is: `val/var <property name>: <Type> by <expression>`. The expression after *by*{:.keyword} is the _delegate_, 
because `get()` (and `set()`) corresponding to the property will be delegated to its `getValue()` and `setValue()` methods.
Property delegates don’t have to implement any interface, but they have to provide a `getValue()` function (and `setValue()` --- for *var*{:.keyword}'s).
For example:
语法结构是： `val/var <property name>: <Type> by <expression>` 在 by 后面的属性就是代理，这样这个属性的 get() 和 set() 方法就代理给了它。

属性代理不需要任何接口的实现，但必须要提供 `get()` 方法(如果是变量还需要 `set()` 方法)。像这样：

``` kotlin
class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }
 
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name} in $thisRef.'")
    }
}
```

When we read from `p` that delegates to an instance of `Delegate`, the `getValue()` function from `Delegate` is called,
so that its first parameter is the object we read `p` from and the second parameter holds a description of `p` itself 
(e.g. you can take its name). For example:
当我们从 `p` 也就是 `Delegate` 的代理，中读东西时，会调用 `Delegate` 的 `get()` 函数，因此第一个参数是我们从 `p` 中读取的，第二个参数是 `p` 自己的一个描述。比如：

``` kotlin
val e = Example()
println(e.p)
```

This prints 
打印结果

``` kotlin
Example@33a17727, thank you for delegating ‘p’ to me!
```
 
Similarly, when we assign to `p`, the `setValue()` function is called. The first two parameters are the same, and the third holds the value being assigned:
同样当我们分配 `p` 时 `set()` 函数就会调动。前俩个参数所以一样的，第三个持有分配的值：


``` kotlin
e.p = "NEW"
```

This prints
打印结果
 
``` kotlin
NEW has been assigned to ‘p’ in Example@33a17727.
```

The specification of the requirements to the delegated object can be found [below](delegated-properties.html#property-delegate-requirements).

Note that since Kotlin 1.1 you can declare a delegated property inside a function or code block, it shouldn't necessarily be a member of a class.
Below you can find [the example](delegated-properties.html#local-delegated-properties-since-11).

## Standard Delegates 标准代理

The Kotlin standard library provides factory methods for several useful kinds of delegates.
`kotlin.properties.Delegates` 对象是标准库提供的一个工厂方法并提供了很多有用的代理

### Lazy 延迟  //懒

`lazy()` is a function that takes a lambda and returns an instance of `Lazy<T>` which can serve as a delegate for implementing a lazy property:
the first call to `get()` executes the lambda passed to `lazy()` and remembers the result, 
subsequent calls to `get()` simply return the remembered result. 
`Delegate.lazy()` 是一个接受 lamdba 并返回一个实现延迟属性的代理：第一次调用 `get()` 执行 lamdba 并传递 `lazy()` 并记下结果，随后调用 `get()` 并简单返回之前记下的值。


``` kotlin
val lazyValue: String by lazy {
    println("computed!")
    "Hello"
}

fun main(args: Array<String>) {
    println(lazyValue)
    println(lazyValue)
}
```

This example prints:

```
computed!
Hello
Hello
```

By default, the evaluation of lazy properties is **synchronized**: the value is computed only in one thread, and all threads
will see the same value. If the synchronization of initialization delegate is not required, so that multiple threads
can execute it simultaneously, pass `LazyThreadSafetyMode.PUBLICATION` as a parameter to the `lazy()` function. 
And if you're sure that the initialization will always happen on a single thread, you can use `LazyThreadSafetyMode.NONE` mode, 
which doesn't incur any thread-safety guarantees and the related overhead.
如果你想要线程安全，使用 `blockingLazy()`: 它还是按照同样的方式工作，但保证了它的值只会在一个线程中计算，并且所有的线程都获取的同一个值。


### Observable 观察者

`Delegates.observable()` takes two arguments: the initial value and a handler for modifications.
The handler gets called every time we assign to the property (_after_ the assignment has been performed). It has three
parameters: a property being assigned to, the old value and the new one:
`Delegates.observable()` 需要俩个参数：一个初始值和一个修改者的 handler 。每次我们分配属性时都会调用handler (在分配前执行)。它有三个参数：一个分配的属性，旧值，新值：

``` kotlin
import kotlin.properties.Delegates

class User {
    var name: String by Delegates.observable("<no name>") {
        prop, old, new ->
        println("$old -> $new")
    }
}

fun main(args: Array<String>) {
    val user = User()
    user.name = "first"
    user.name = "second"
}
```

This example prints:

```
<no name> -> first
first -> second
```

If you want to be able to intercept an assignment and "veto" it, use `vetoable()` instead of `observable()`.
The handler passed to the `vetoable` is called _before_ the assignment of a new property value has been performed.
如果你想能够截取它的分配并取消它，用 `vetoable()`代替  `observable()`

## Storing Properties in a Map 在map（映象）中存储属性

One common use case is storing the values of properties in a map.
This comes up often in applications like parsing JSON or doing other “dynamic” things.
In this case, you can use the map instance itself as the delegate for a delegated property.
`Delegates.mapVal()` 拥有一个 map 实例并返回一个可以从 map 中读其中属性的代理。在应用中有很多这样的例子，比如解析 JSON 或者做其它的一些 "动态"的事情：


``` kotlin
class User(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int     by map
}
```

In this example, the constructor takes a map:
在这个例子中，构造函数持有一个 map :

``` kotlin
val user = User(mapOf(
    "name" to "John Doe",
    "age"  to 25
))
```

Delegated properties take values from this map (by the string keys --– names of properties):
代理从这个 map 中取指(通过属性的名字)：

``` kotlin
println(user.name) // Prints "John Doe"
println(user.age)  // Prints 25
```

This works also for *var*{:.keyword}’s properties if you use a `MutableMap` instead of read-only `Map`:

``` kotlin
class MutableUser(val map: MutableMap<String, Any?>) {
    var name: String by map
    var age: Int     by map
}
```

## Local Delegated Properties (since 1.1)

You can declare local variables as delegated properties.
For instance, you can make a local variable lazy:

``` kotlin
fun example(computeFoo: () -> Foo) {
    val memoizedFoo by lazy(computeFoo)

    if (someCondition && memoizedFoo.isValid()) {
        memoizedFoo.doSomething()
    }
}
```

The `memoizedFoo` variable will be computed on the first access only.
If `someCondition` fails, the variable won't be computed at all.

## Property Delegate Requirements

Here we summarize requirements to delegate objects. 

For a **read-only** property (i.e. a *val*{:.keyword}), a delegate has to provide a function named `getValue` that takes the following parameters:

* `thisRef` --- must be the same or a supertype of the _property owner_ (for extension properties --- the type being extended),
* `property` --- must be of type `KProperty<*>` or its supertype,
 
this function must return the same type as property (or its subtype).

For a **mutable** property (a *var*{:.keyword}), a delegate has to _additionally_ provide a function named `setValue` that takes the following parameters:
 
* `thisRef` --- same as for `getValue()`,
* `property` --- same as for `getValue()`,
* new value --- must be of the same type as a property or its supertype.
 
`getValue()` and/or `setValue()` functions may be provided either as member functions of the delegate class or extension functions.
The latter is handy when you need to delegate property to an object which doesn't originally provide these functions.
Both of the functions need to be marked with the `operator` keyword.

The delegate class may implement one of the interfaces `ReadOnlyProperty` and `ReadWriteProperty` containing the required `operator` methods.
These interfaces are declared in the Kotlin standard library:

``` kotlin
interface ReadOnlyProperty<in R, out T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
}

interface ReadWriteProperty<in R, T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
    operator fun setValue(thisRef: R, property: KProperty<*>, value: T)
}
```

### Translation Rules

Under the hood for every delegated property the Kotlin compiler generates an auxiliary property and delegates to it.
For instance, for the property `prop` the hidden property `prop$delegate` is generated, and the code of the accessors simply delegates to this additional property:

``` kotlin
class C {
    var prop: Type by MyDelegate()
}

// this code is generated by the compiler instead:
class C {
    private val prop$delegate = MyDelegate()
    var prop: Type
        get() = prop$delegate.getValue(this, this::prop)
        set(value: Type) = prop$delegate.setValue(this, this::prop, value)
}
```
The Kotlin compiler provides all the necessary information about `prop` in the arguments: the first argument `this` refers to an instance of the outer class `C` and `this::prop` is a reflection object of the `KProperty` type describing `prop` itself.

Note that the syntax `this::prop` to refer a [bound callable reference](reflection.html#bound-function-and-property-references-since-11) in the code directly is available only since Kotlin 1.1.  

### Providing a delegate (since 1.1)

By defining the `provideDelegate` operator you can extend the logic of creating the object to which the property implementation is delegated.
If the object used on the right hand side of `by` defines `provideDelegate` as a member or extension function, that function will be
called to create the property delegate instance.

One of the possible use cases of `provideDelegate` is to check property consistency when the property is created, not only in its getter or setter.

For example, if you want to check the property name before binding, you can write something like this:

``` kotlin
class ResourceLoader<T>(id: ResourceID<T>) {
    operator fun provideDelegate(
            thisRef: MyUI,
            prop: KProperty<*>
    ): ReadOnlyProperty<MyUI, T> {
        checkProperty(thisRef, prop.name)
        // create delegate
    }

    private fun checkProperty(thisRef: MyUI, name: String) { ... }
}

fun <T> bindResource(id: ResourceID<T>): ResourceLoader<T> { ... }

class MyUI {
    val image by bindResource(ResourceID.image_id)
    val text by bindResource(ResourceID.text_id)
}
```

The parameters of `provideDelegate` are the same as for `getValue`:

* `thisRef` --- must be the same or a supertype of the _property owner_ (for extension properties --- the type being extended),
* `property` --- must be of type `KProperty<*>` or its supertype.

The `provideDelegate` method is called for each property during the creation of the `MyUI` instance, and it performs the necessary validation right away.

Without this ability to intercept the binding between the property and its delegate, to achieve the same functionality
you'd have to pass the property name explicitly, which isn't very convenient:

``` kotlin
// Checking the property name without "provideDelegate" functionality
class MyUI {
    val image by bindResource(ResourceID.image_id, "image")
    val text by bindResource(ResourceID.text_id, "text")
}

fun <T> MyUI.bindResource(
        id: ResourceID<T>,
        propertyName: String
): ReadOnlyProperty<MyUI, T> {
   checkProperty(this, propertyName)
   // create delegate
}
```

In the generated code, the `provideDelegate` method is called to initialize the auxiliary `prop$delegate` property.
Compare the generated code for the property declaration `val prop: Type by MyDelegate()` with the generated code 
[above](delegated-properties.html#translation-rules) (when the `provideDelegate` method is not present):

``` kotlin
class C {
    var prop: Type by MyDelegate()
}

// this code is generated by the compiler 
// when the 'provideDelegate' function is available:
class C {
    // calling "provideDelegate" to create the additional "delegate" property
    private val prop$delegate = MyDelegate().provideDelegate(this, this::prop)
    val prop: Type
        get() = prop$delegate.getValue(this, this::prop)
}
```

Note that the `provideDelegate` method affects only the creation of the auxiliary property and doesn't affect the code generated for getter or setter.
