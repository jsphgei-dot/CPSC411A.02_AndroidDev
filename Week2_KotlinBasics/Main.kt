//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// https://onecompiler.com/kotlin

fun main() {
    // variables
//    variables()
    //input/output
//    inout()
    //string literals, template
    //null safety
//    nullSafety()

    //if else, when
//    controlStmt()
    //loops
    //functions
    //extension
    extFun()
}

fun extFun() {
    //"hello".reverseMe() == "olleh"
//    print("hello".reverseMe())
    val num = 1234
    println(num.reverseMe())
}

fun String.reverseMe(): String {
    val reversed = buildString {
        for(i in this@reverseMe.lastIndex downTo 0) {
            append(this@reverseMe[i])
        }
    }

    return reversed
}

fun Int.reverseMe(): Int {
  return this.toString().reverseMe().toInt()
}




fun controlStmt() {
    val num = 10

//    val isEven = if(num.rem(2).equals(0)) "Even" else "Odd"
//
//    print(isEven)

    var arr = intArrayOf(1, 2,3,4)

    arr += 10

//    for(i in arr) {
//        println(i)
//    }

    for(i in 1 until  5){
        println(i)
    }




}

fun nullSafety() {
    var myInput: String? = "hello"
    myInput = null

//    val l = myInput!!.length
//
//    print(l)
    println("Enter a number:")
    val l : Int? = readln().toIntOrNull() ?: 10
    print(l)
    val isEven = l?.rem(2)?.equals(0) ?: false

//    print(isEven)



}

fun inout() {
    val a = """
       for(i in range 1..5){
            print(i)
       }
    """.trimIndent()

//    print(a)

    //input
    println("Enter a string: ")
//    val str = readln()
//
//    print("entered string value is: $str and the length is ${str.length}")
    val num = readln().toIntOrNull()

    print(num)

}

fun variables() {
    //val
    val a = "str"
//    a = "dfghdg"
//    print(a)
    //var

    var b = 10
    b+1
    print(b)
}





