class Address {

    var name: String = ""
    var street: String = ""
    var city: String = ""
    var state: String = ""
    var zip: String = ""

}

fun copyAdress(address: Address): Address {

    var result = Address()

    result.name = address.name
    result.street = address.street
    result.city = address.city
    result.state = address.state
    result.zip = address.zip

    return result
}


fun main(args: Array<String>) {

    var myAddress = Address()

    myAddress.name = "阿强"
    myAddress.street = "胜利大街"
    myAddress.city = "东营市"
    myAddress.state = "山东省"
    myAddress.zip = "257000"

    println(copyAdress(myAddress).name)
    println(copyAdress(myAddress).street)
    println(copyAdress(myAddress).city)
    println(copyAdress(myAddress).state)
    println(copyAdress(myAddress).zip)

}
