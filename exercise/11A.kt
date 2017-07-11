class Person{
    var name:String ?= ""
    var age:Int = 0
}

fun main(args: Array<String>) {

    var person = Person()
    person.name = null
    person.age = 24

    val s = person.name ?: return   //s为null,函数直接返回
    println("程序继续执行")
    println(s)
}


// 运行结果
// （空）
