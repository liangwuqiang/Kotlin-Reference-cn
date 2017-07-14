
```kotlin
var allByDefault: Int? // 错误: 需要一个初始化语句, 默认实现了 getter 和 setter 方法
var initialized = 1 // 类型为 Int, 默认实现了 getter 和 setter
```



```kotlin
val simple: Int? // 类型为 Int ，默认实现 getter ，但必须在构造函数中初始化

val inferredType = 1 // 类型为 Int 类型,默认实现 getter
```



```kotlin
var isEmpty: Boolean
	get() = this.size == 0
```



```kotlin
var stringRepresentation: String
	get() = this.toString()
	set (value) {
		setDataFormString(value) // 格式化字符串,并且将值重新赋值给其他元素
}
```





```kotlin
var settVisibilite: String = "abc"//非空类型必须初始化
	private set // setter 是私有的并且有默认的实现
var setterVithAnnotation: Any?
	@Inject set // 用 Inject 注解 setter
```

###  备用字段


```kotllin
var counter = 0 //初始化值会直接写入备用字段
	set(value) {
		if (value >= 0)
			field  = value
	}
```







```kotlin
val isEmpty: Boolean
	get() = this.size == 0
```

### 备用属性


```kotlin
private var _table: Map<String, Int>? = null
public val table: Map<String, Int>
	get() {
	if (_table == null)
		_table = HashMap() //参数类型是推导出来的
		return _table ?: throw AssertionError("Set to null by another thread")
	}
```



### 编译时常量







	

```kotlin
const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"
@Deprected(SUBSYSTEM_DEPRECATED) fun foo() { ... }
```

### 延迟初始化属性



```kotlin
public class MyTest {
	lateinit var subject: TestSubject
	
	@SetUp fun setup() {
		subject = TestSubject()
	}
	
	@Test fun test() {
		subject.method() 
	}
}
```




### 复写属性
参看[复写成员](http://kotlinlang.org/docs/reference/classes.html#overriding-members)

### 代理属性


