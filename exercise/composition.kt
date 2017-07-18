fun main(args: Array<String>) {
    val oddLength = compse(::isOdd, ::length) //函数的组合
    val strings = listOf("a", "ab", "abc")
    println(strings.filter(oddLength))   //对迭代对象进行过滤，过滤条件是两个函数的组合
}
fun isOdd(x: Int) = x % 2 != 0

fun length(s: String) = s.length

//fun <A, B, C> compse(f: (B) -> C, g: (A) -> B): (A) -> C { //很不明白,有点意思
// 三种状态， 函数在三种状态之间进行转换
// 三种泛型，迭代对象的一个元素，String字符串A型，转到Int整形B型，这是g的转换过程，也就是length函数
// 第二个过程也就是中将元素从B型状态（Int整形），装换成Boolean布尔型C型状态，这是f的过程,也就是isOdd函数
// 整个组合的函数是从A转换到C的组合过程，得到的结果就是ture和false
// 通过filter进行过滤，剩余的迭代元素保留下来
//    return { x -> f(g(x))} //g代表length函数，f代表isOdd函数
//}
fun compse(f: (Int) -> Boolean, g: (String) -> Int): (String) -> Boolean {
    return { x -> f(g(x)) } //返回的是lamda表达式
}
