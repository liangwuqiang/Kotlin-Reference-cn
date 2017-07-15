---
type: doc
layout: reference
category: "Syntax"
title: "Enum Classes"
---

# Enum Classes 枚举类

The most basic usage of enum classes is implementing type-safe enums
枚举类最基本的用法就是实现类型安全的枚举

``` kotlin
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
```

Each enum constant is an object. Enum constants are separated with commas.
每个自举常量都是一个对象。枚举常量通过逗号分开。

## Initialization 初始化

Since each enum is an instance of the enum class, they can be initialized
因为每个枚举都是枚举类的一个实例，它们是可以初始化的。

``` kotlin
enum class Color(val rgb: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF)
}
```

## Anonymous Classes 匿名类

Enum constants can also declare their own anonymous classes
枚举实例也可以声明它们自己的匿名类

``` kotlin
enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },

    TALKING {
        override fun signal() = WAITING
    };

    abstract fun signal(): ProtocolState
}
```

with their corresponding methods, as well as overriding base methods. Note that if the enum class defines any
members, you need to separate the enum constant definitions from the member definitions with a semicolon, just like
in Java.
可以有对应的方法，以及复写基本方法。注意如果枚举定义了任何成员，你需要像在 java 中那样用分号 ; 把枚举常量定义和成员定义分开。

## Working with Enum Constants 使用枚举常量

Just like in Java, enum classes in Kotlin have synthetic methods allowing to list
the defined enum constants and to get an enum constant by its name. The signatures
of these methods are as follows (assuming the name of the enum class is `EnumClass`):
像 java 一样，Kotlin 中的枚举类有合成方法允许列出枚举常量的定义并且通过名字获得枚举常量。这些方法的签名就在下面列了出来(假设枚举类名字是 EnumClass)：

``` kotlin
EnumClass.valueOf(value: String): EnumClass
EnumClass.values(): Array<EnumClass>
```

The `valueOf()` method throws an `IllegalArgumentException` if the specified name does
not match any of the enum constants defined in the class.
如果指定的名字在枚举类中没有任何匹配，那么`valueOf()`方法将会抛出参数异常。

Since Kotlin 1.1, it's possible to access the constants in an enum class in a generic way, using
the `enumValues<T>()` and `enumValueOf<T>()` functions:

``` kotlin
enum class RGB { RED, GREEN, BLUE }

inline fun <reified T : Enum<T>> printAllValues() {
    print(enumValues<T>().joinToString { it.name })
}

printAllValues<RGB>() // prints RED, GREEN, BLUE
```

Every enum constant has properties to obtain its name and position in the enum class declaration:
每个枚举常量都有获取在枚举类中声明的名字和位置的方法：

``` kotlin
val name: String
val ordinal: Int
```

The enum constants also implement the [Comparable](/api/latest/jvm/stdlib/kotlin/-comparable/index.html) interface,
with the natural order being the order in which they are defined in the enum class.
枚举类也实现了 [Comparable](http://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html) 接口，比较时使用的是它们在枚举类定义的自然顺序。