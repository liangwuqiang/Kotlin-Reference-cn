var ints = arrayOf(1, 2, 3, 0 ,4, 5)

fun foo() {
    ints.forEach lit@ {
        if (it == 0) return@lit
        print(it)
    }
}

fun main(args: Array<String>) {

    foo()
}

// 运行结果
// 12345
