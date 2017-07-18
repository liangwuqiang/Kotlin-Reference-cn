fun main(args: Array<String>) {
    if (args.isEmpty) {
        printBottles(99)
    } else {
        try {
            printBottles(args[0].toInt())
        } catch (e:NumberFormatException) {
            println("你传入${args[0]}作为瓶子数量，但它不是有效整数")
        }
    }
}
fun printBottles(bottleCount: Int) {
    if (bottleCount <= 0) {
        println("没有瓶子，也就没有歌")
        return
    }

    println("${bottlesOfBeer(bottleCount)}之歌")

    var bottles = bottleCount
    while (bottles > 0) {
        var bottlesOfBeer = bottlesOfBeer(bottles)
        println("$bottlesOfBeer 在墙上，$bottlesOfBeer。 \n拿下来一个，并把它拿走")
        bottles--
        println("${bottlesOfBeer(bottles)}在墙上")
    }
    println("墙上没有瓶子，没有啤酒瓶了。\n去商店了买些, ${bottlesOfBeer(bottleCount)}放在墙上")

}

val <T> Array<T>.isEmpty: Boolean get() = size == 0

fun bottlesOfBeer(count: Int): String =
        when (count) {
            0 -> "没有瓶子"
            1 -> "只有一个瓶子"
            else -> "有${count}个瓶子"
        } + "装啤酒"
