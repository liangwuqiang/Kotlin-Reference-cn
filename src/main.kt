
fun main(args: Array<String>) {
    var language = if (args.size == 0) "En" else args[0]
    println( when (language) {
        "En" -> "Hello"
        "Cn" -> "你好"
        "Fr" -> "Calut"
        "It" -> "dfs"
        else -> "Sorry, I can't greet you in $language yet!"
    })

}
