fun decimalDigitValue(c: Char): Int {
    if (c !in '0'..'9')
        throw IllegalArgumentException("Out of range")
    return c.toInt() - '0'.toInt()
}

fun main(args: Array<String>) {

    println(decimalDigitValue('3'))
    println(decimalDigitValue('7'))
    println(decimalDigitValue('a'))

}

//运行结果
//3
//7
//Exception in thread "main" java.lang.IllegalArgumentException: Out of range
//at MainKt.decimalDigitValue(main.kt:3)
//at MainKt.main(main.kt:11)