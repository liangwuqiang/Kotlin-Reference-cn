---
type: doc
layout: reference
category: "Classes and Objects"
title: "Visibility Modifiers"
---

# Visibility Modifiers 可见性修饰符

Classes, objects, interfaces, constructors, functions, properties and their setters can have _visibility modifiers_.
类，对象，接口，构造方法，函数，属性和它们的setters方法 可以有 _可见性修饰符_。

(Getters always have the same visibility as the property.) 
（Getters方法总是和属性（关联的）具有相同的可见性。）

There are four visibility modifiers in Kotlin: `private`, `protected`, `internal` and `public`.
Kotlin中有四种可见性修饰符：`private`, `protected`, `internal` 和 `public`.

The default visibility, used if there is no explicit modifier, is `public`.
默认的可见性 是 `public` ，用于没有指明修饰符。

Below please find explanations of these for different type of declaring scopes.
下面请找出这些说明， 不同类型的声明作用域 
  
## Packages 包
  
Functions, properties and classes, objects and interfaces can be declared on the "top-level", 
函数，属性和类，对象和接口 可以在顶层上声明，

i.e. directly inside a package:
例如，直接在包内：
  
``` kotlin
// file name: example.kt
package foo

fun baz() {}
class Bar {}
```

* If you do not specify any visibility modifier, `public` is used by default, 
    如果没有指定可见性修饰符，默认会使用 `public`

    which means that your declarations will be visible everywhere;
    意思是你的声明在任何地方都是可见的；
    
* If you mark a declaration `private`, 
    如果你用 `private` 标识一个声明，

    it will only be visible inside the file containing the declaration;
    它只在包含声明的文件中是可见的；
    
* If you mark it `internal`, it is visible everywhere in the same [module](#modules);
    如果你标识了 `internal`, 它在相同的[模块](#modules)内是可见的；

* `protected` is not available for top-level declarations.
    `protected` 在顶层声明中不可用。

Examples:
例子：

``` kotlin
// file name: example.kt
package foo

private fun foo() {} // visible inside example.kt

public var bar: Int = 5 // property is visible everywhere
    private set         // setter is visible only in example.kt
    
internal val baz = 6    // visible inside the same module
```

## Classes and Interfaces 类和接口

For members declared inside a class:
对于类内部的成员声明：

* `private` means visible inside this class only (including all its members);
    `private` 意味着只是在类内部是可见的 （包含所用成员）；

* `protected` --- same as `private` + visible in subclasses too;
    `protected` 和 `private`相同 + 在子类中也是可见的；

* `internal` --- any client *inside this module* who sees the declaring class sees its `internal` members;
    `internal` 任何 能看见类声明的 *该模块内部* 的客户可以看见它的`internal`成员；

* `public` --- any client who sees the declaring class sees its `public` members.
    `public` 任何 能看见类声明的 客户 可以看见它的`internal`成员。

*NOTE* for Java users: outer class does not see private members of its inner classes in Kotlin.
注意，（针对Java用户），在Kotlin中，外层类不可见内层类的私有成员。

If you override a `protected` member and do not specify the visibility explicitly, 
如果你复写一个 `protected`成员 并 不明确指定可见性，

the overriding member will also have `protected` visibility.
复写成员也会有 `protectec` 可见性。
 
 
Examples:
例子：

``` kotlin
open class Outer {
    private val a = 1
    protected open val b = 2
    internal val c = 3
    val d = 4  // public by default 默认为public
    
    protected class Nested {
        public val e: Int = 5
    }
}

class Subclass : Outer() {
    // a is not visible
    // b, c and d are visible
    // Nested and e are visible

    override val b = 5   // 'b' is protected
}

class Unrelated(o: Outer) {
    // o.a, o.b are not visible
    // o.c and o.d are visible (same module)
    // Outer.Nested is not visible, and Nested::e is not visible either 
}
```

### Constructors 构造方法

To specify a visibility of the primary constructor of a class, 
为了指定一个类的主构造方法的可见性，

use the following syntax 
使用下面的语法

(note that you need to add an explicit *constructor*{: .keyword } keyword):
（注意，你需要添加明确的 *constructor* 关键字）：

``` kotlin
class C private constructor(a: Int) { ... }
```

Here the constructor is private. 
这里的构造方法是私有的。

By default, all constructors are `public`, 
默认情况下，所有的构造方式都是 `public`,

which effectively amounts to them being visible everywhere where the class is visible 
大多数情况下， 只要类是可见的，它们就是可见的

(i.e. a constructor of an `internal` class is only visible within the same module).
（例如，一个 `internal` 类的构造方法 只是在同一个模块中是可见的）。
     
### Local declarations 局部声明
     
Local variables, functions and classes can not have visibility modifiers.
局部变量，函数 和 类 不能有可见性修饰符。

## Modules 模块

The `internal` visibility modifier means that the member is visible with the same module. 
`internal` 可见性修饰符 意味着 在同一个模块内 成员是可见的。

More specifically, a module is a set of Kotlin files compiled together:
更具体地说， 模块是一系列要编译在一起的Kotlin文件集合:

  * an IntelliJ IDEA module;
  * a Maven or Gradle project;
  * a set of files compiled with one invocation of the <kotlinc> Ant task.

（完 2017-07-14）