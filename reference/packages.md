---
type: doc
layout: reference
category: "Syntax"
title: "Packages"
---

# Packages || 包

A source file may start with a package declaration:

一个源文件可以以包名的声明开始编写：

``` kotlin
package foo.bar

fun baz() {}

class Goo {}

// ...
```

-----------------------

All the contents (such as classes and functions) of the source file are contained by the package declared.

源文件的所有内容（例如类和函数）都被包含于所声明的包中。

So, in the example above, the full name of `baz()` is `foo.bar.baz`, and the full name of `Goo` is `foo.bar.Goo`.
 
 因此，上面的例子中，`baz()`的全名是`foo.bar.baz`，而`Goo`的全名是`foo.bar.Goo`。
 
If the package is not specified, the contents of such a file belong to "default" package that has no name.

如果不指定包名，那样的文件的内容就属于无名的"default"包。（包名是用于避免名称冲突的）

## Default Imports 系统默认导入

A number of packages are imported into every Kotlin file by default:

许多包被默认导入到Kotlin文件中：

- kotlin.*
- kotlin.annotation.*
- kotlin.collections.*
- kotlin.comparisons.*  (since 1.1)
- kotlin.io.*
- kotlin.ranges.*
- kotlin.sequences.*
- kotlin.text.*

Additional packages are imported depending on the target platform:

另外一些包则根据使用平台来决定是否导入：

- JVM:
  - java.lang.*
  - kotlin.jvm.*

- JS:    
  - kotlin.js.*

## Imports 导入

Apart from the default imports, each file may contain its own import directives.

除了系统默认的导入外，每个文件都可以包含自己的导入语句。

Syntax for imports is described in the [grammar](grammar.md#import).

导入的语法在[语法](grammar.md#import)中有说明.

We can import either a single name, e.g.

我们既可以导入单独的名称，例如：

``` kotlin
import foo.Bar // Bar is now accessible without qualification
```

or all the accessible contents of a scope (package, class, object etc):

也可以导入一个范围内所有可以访问的内容（包，类，对象等等）：

``` kotlin
import foo.* // everything in 'foo' becomes accessible
```

If there is a name clash, we can disambiguate by using *as*{: .keyword } keyword 

如果有名称冲突，我们可以解决它，通过使用as关键字

to locally rename the clashing entity:

本地重新命名冲突实体

``` kotlin
import foo.Bar // Bar is accessible
import bar.Bar as bBar // bBar stands for 'bar.Bar'
```

The `import` keyword is not restricted to importing classes; 

`import`关键字不仅限于导入类； 
 
you can also use it to import other declarations:

你也可以使用它来导入其他声明：

  * top-level functions and properties;
  * 顶层函数和属性;
  * functions and properties declared in [object declarations](object-declarations.md#object-declarations);
  * 函数和属性在[对象声明](object-declarations.md#object-declarations)中有说明;
  * [enum constants](enum-classes.md)
  * [枚举常量](enum-classes.md)

Unlike Java, Kotlin does not have a separate "import static" syntax; 

和Java不一样，Kotlin没有单独的"import static"语法

all of these declarations are imported using the regular `import` keyword.

所有这些声明都被统一使用`import`关键字来导入。

## Visibility of Top-level Declarations 顶层声明的可见性

If a top-level declaration is marked *private*{: .keyword }, 

如果顶层声明被标记为private，

it is private to the file it's declared in (see [Visibility Modifiers](visibility-modifiers.md)).

它对该文件是私有的。它在[可见的修饰符](visibility-modifiers.md)中有说明。
