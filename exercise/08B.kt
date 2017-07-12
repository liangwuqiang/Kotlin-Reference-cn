fun main(args: Array<String>) {

    val a: Int = 10000
    println(a === a)    //print 'true'

    val boxedA: Int ?= a
    val anotherBoxedA: Int ?= a
    println(boxedA === anotherBoxedA)  // print 'false'
}

// 运行结果
// true
// false