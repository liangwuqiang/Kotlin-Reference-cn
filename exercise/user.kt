data class User(val name: String, val id: Int)

fun main(args: Array<String>) {
    val user = User("Alex", 1)
    println(user)

    val secondUser = User("Alex", 1)
    val thirdUser = User("Max", 2)

    println("user == secondUser: ${user == secondUser}")
    println("user == thirUser: ${user == thirdUser}")

    println(user.copy())
    println(user.copy("Max"))
    println(user.copy(id = 2))
    println(user.copy("Max", 2))

    //copy之后，并没有对原来的实例做出改变，而是产生了一个新的实例

    println(user)

}