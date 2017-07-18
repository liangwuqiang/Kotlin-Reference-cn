//package bottles

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printBottles(99)
    } else {
        try {
            printBottles(args[0].toInt())
        } catch (e: NumberFormatException) {
            println("You have passed '${args[0]}' as a number of bottles, " +
                    "but it is not a valid integer number")
        }
    }
}

fun printBottles(bottleCount: Int) {
    if (bottleCount <= 0) {
        println("No bottles - no song")
        return
    }

    println("The \"${bottlesOfBeer(bottleCount)}\" song\n")

    var bottles = bottleCount
    while (bottles > 0) {
        val bottlesOfBeer = bottlesOfBeer(bottles)
        print("$bottlesOfBeer on the wall, $bottlesOfBeer.\nTake one down, pass it around, ")
        bottles--
        println("${bottlesOfBeer(bottles)} on the wall.\n")
    }
    println("No more bottles of beer on the wall, no more bottles of beer.\n" +
            "Go to the store and buy some more, ${bottlesOfBeer(bottleCount)} on the wall.")
}

fun bottlesOfBeer(count: Int): String =
        when (count) {
            0 -> "no more bottles"
            1 -> "1 bottle"
            else -> "$count bottles"
        } + " of beer"

/*
 * An excerpt from the Standard Library
 *
 */


// This is an extension property, i.e. a property that is defined for the
// 这是个扩展属性，例如，一个属性 被定义为数组类
// type Array<T>, but does not sit inside the class Array
// 但是不处于数组类的内部
val <T> Array<T>.isEmpty: Boolean get() = size == 0
// 在数组类中添加属性，返回布尔值类型, 定义get方法, get() = (size == 0)

