class KotlinGetterAndSetter {
    var name: String = "jason"
        set(value){field = value }
        get() = field.toUpperCase()
}

fun main(args: Array<String>) {

    val k = KotlinGetterAndSetter()
    println(k.name)
    k.name = "tom"
    println(k.name)

}
