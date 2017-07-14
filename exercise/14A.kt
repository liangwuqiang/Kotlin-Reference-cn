interface A {
    fun foo() { println("A foo") }
    fun bar()
}
interface B {
    fun foo() { println("B foo") }
    fun bar() { println("B bar") }
}
class C : A {
    override fun bar() { println("C bar") }
}
class D : A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }
    override fun bar() {
        super<B>.bar()
    }
}

fun main(args: Array<String>) {
    var c = C()
    c.bar()
    c.foo()
    var d = D()
    d.foo()
    d.bar()
}
