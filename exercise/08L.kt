fun main(args: Array<String>) {

    val asc = Array(5, { i -> (i * i).toString() })
    // public inline constructor(size: Int, init: (Int) -> T)
    //通过类Array来构造数组

    asc.forEach {println(it)}

}

//运行结果
//0
//1
//4
//9
//16