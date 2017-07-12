class KotlinGetterAndSetter {
    var name: String = "jason"
        set(value){field = value }
        get() = field
}

fun main(args: Array<String>) {

    var k = KotlinGetterAndSetter()
    print(k.name)
    k.name.set("lwq")
//    print(k.name.get())

}
