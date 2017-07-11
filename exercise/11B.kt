fun main(args: Array<String>) {

    loop@ for (i in 1..100) {
        for (j in 1..100) {
            if (j == 5) break@loop
            println(j)
        }
    }
}

// 运行结果
// 1
// 2
// 3
// 4
