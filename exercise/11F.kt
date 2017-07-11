var ints = arrayOf(1, 2, 3, 0 ,4, 5)

fun foo() {
    ints.forEach(fun(value: Int) {
        if (value == 0) return
        print(value)
    })
}

fun main(args: Array<String>) {

    foo()
}

// 运行结果
// 12345
