import kotlin.reflect.KProperty

class Example() {
    var p: String by Delegate()

    override fun toString() = "样例类"
}
class Delegate() {   //注意传递的参数 类型
    operator fun getValue(thisRef: Any?, prop: KProperty<*>):String {
        return "$thisRef, 感谢你给我传递了 '${prop.name}' "
    }
    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String): Unit {
        println("$value 已经被指派给 ${prop.name} ，在 $thisRef 之中")
    }
}

fun main(args: Array<String>) {
    val e = Example()
    println(e.p)
    e.p = "NEW"
}