fun main(args: Array<String>) {

    val x: IntArray = intArrayOf(1, 2, 3)
    x[0] = x[1] + x[2]
    println(x[0])
}

//运行结果
//5
