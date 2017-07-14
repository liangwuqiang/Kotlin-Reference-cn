class MyClass {
    var stringRepresentation: String
        get() = this.toString()
        set(value) {

//            setDataFromString(value)
        }
    var setterVisibility: String = "abc"
        private set

    var setterWithAnnotation: Any? = null
        @Inject set

}

fun main(args: Array<String>) {

    var myClass = MyClass()

    println(myClass.setterVisibility);

}
