
enum class Lang(val hello: String) {
    ENGLISH("hello"),
    CHINESE("你好");

    fun sayHello() {
        println(hello)
    }

    companion object {
        fun parse(str: String): Lang {
            return Lang.valueOf(str.toUpperCase())
        }
    }
}


fun main(args: Array<String>) {
    if (args.isEmpty()) return
    val lang = Lang.parse(args[0])

    println(lang)
    lang.sayHello()
    lang.sayBye()

}

fun Lang.sayBye() {
    val bye = when (this) {
        Lang.ENGLISH -> "bye"
        Lang.CHINESE -> "再见"
    }
    println(bye)
}
