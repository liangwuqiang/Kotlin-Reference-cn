open class Base {
    open fun v() {}
    fun nv() {}
}

class Derived(): Base() {
    override fun v() {}

}

fun main(args: Array<String>) {


}
