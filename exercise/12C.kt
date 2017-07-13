open class Base {
    open fun v() {}
    fun nv() {}
}

class Derived: Base() {
    override fun v() {}

}

interface Foo {
    val count: Int
}

class Bar1(override val count: Int): Foo

open class Bar2 : Foo {
    override var count: Int = 0
}

class Bar3: Bar2() {
    override var count: Int = 5
}

fun main(args: Array<String>) {


}
