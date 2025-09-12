import javax.sql.rowset.Predicate
import kotlin.math.PI
import kotlin.math.sqrt

fun main() {
    //lambdaFun()
    //classFun()
    //interfaceFun()
    //otherClassesFun()
    //inheritanceFun()
}

// Lambda Functions
fun add(num1: Int, num2: Int): Int {
    return num1+num2
}

fun lambdaFun() {
    // lamdba basic syntax
    // variable: (type of params)->type of return = {parameters -> body/return}
    // val add: (Int, Int)->Int = {a: Int, b: Int -> a+b}
    // other ways of the above lambda
    // val add: (Int, Int)->Int = {a, b -> a+b}
    // val add = {a: Int, b: Int -> a+b}
    println("1 + 2 = ${add(1, 2)}")

    // shorthand
    // val square: (Int) -> Int = {num: Int -> num * num}
    // "it" is a special keyword used when we dont want to specify a single paramenter explicitly
    // can only be used to replace one single parameter
    val square: (Int) -> Int = {it * it}
    println("Square of 4 is ${square(4)}")

    println("Enter a string: ")
    val input = readln()

    //prebuilt lambda
    val lettersOnly = input.filter {
        it.isLetter()
    }
    println("Letters only using prebuilt: ${lettersOnly}")

    // our own predicate to pass inside lambda
    val myLambda: (Char) -> Boolean = {
        it.isLetter()
    }
    // our own predicate can be passed as a parameter
    val res = input.filter(myLambda)
    println("Letters only using myLambda: ${res}")

    // how to build our own function that takes a lambda as an argument
    val res2 = input.myFilter(myLambda)
    println("Letters only using myFilter: ${res2}")
}

fun String.myFilter(predicate: (Char) -> Boolean): String {
    return buildString {
        for(char in this@myFilter) {
            if(predicate(char)) {
                append(char)
            }
        }
    }
}
// *********************************************************************************************************************
// When to use the below
// Normal class: 1. class has a constructor
//                2. class has an internal state
// Data class: used to bundle data together.
// Class and Data class

data class Rectangle(val width: Float, val height: Float) {
    val diagonal = sqrt((width * width + height * height))

    val area = width * height
    val perimeter: Float = 2 * width + 2 * height
}

data class Circle(val radius: Float) {
    val area = PI * radius * radius
    val diameter = 2 * radius
}

fun classFun() {
    val rect1 = Rectangle(height = 10f, width = 5f)
    val rect2 = Rectangle(width = 5f, height = 10f)
    // println("diagonal is: ${rect1.diagonal}")
    // println("area is: ${rect1.area}")
    // when the rectangle class is a normal class the below print prints false
    // but if the rectangle class is a data class then the print prints as true
    //data class is used to on classes that mainly holds only the data and the functions that perform only on the data
    println(rect1 == rect2)

    //normal class output = Rectangle@6e8cf4c6
    //data class output = Rectangle(width=5.0, height=10.0)
    println(rect1)

    // data class = provides a copy function that is used to copy an existing object
    // the below rect3 is a copy of rect2, and i can change the height param only. The width will be same as rect2
    val rect3 = rect2.copy(
        height = 20f
    )
    //Rectangle(width=5.0, height=20.0)
    println(rect3)

    val circle = Circle(10f)
    println("area of the circle is: ${circle.area}")

}

// *********************************************************************************************************************
// Interfaces
fun interfaceFun() {
    val sq1 = Square(10f)

    println("Area of the square is: ${sq1.area}")
    println("Circumference of the square is: ${sq1.circumference}")
    // Area of the square is: 100.0
    // Circumference of the square is: 40.0

    sq1.side = 15f
    println("Area of the square is: ${sq1.area}")
    println("Circumference of the square is: ${sq1.circumference}")
    // Area of the square is: 100.0
    // Circumference of the square is: 60.0

    val pent1 = Pentagon(10f)

    //
    println("Sum of all the shapes is: "+sumAreas(sq1, pent1))

}

fun sumAreas(vararg shapes: Shape): Double {
    return shapes.sumOf {
        currShape -> currShape.area.toDouble()
    }
}

interface Shape {
    val area: Float
    val circumference: Float
}

data class Square(var side: Float): Shape {

    //This is a stored property. It’s evaluated once at initialization and kept in a backing field.
    // If side later change (they’re vars), area will not update—it stays whatever it was when the object was created.
    // Type is inferred from the expression.
    override val area: Float = side * side

    // This is a computed property with a custom getter and no backing field (since there’s no initializer).
    // It’s recomputed on every access, so it always reflects the current side.
    override val circumference: Float
        get() = 4 * side
}

data class Pentagon(var side: Float): Shape {
    override val area: Float
        get() = side * side
    override val circumference: Float
        get() = 5 * side
}

// *********************************************************************************************************************
// Enum Class
enum class HttpStatus(val code: Int, val msg: String) {
    OK(200, "Success"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not Found");

    fun toResponseString(): String {
        return "Response from server $code: $msg"
    }
}

// Sealed Class
// used to bundle all the related classes together
// the related classes must be in the same module/package
sealed class NetworkResult {
    data class Success(val data: String): NetworkResult()
    data class Error(val data: Throwable): NetworkResult()
}



fun otherClassesFun() {
    // The request is OK
    println("The request is ${HttpStatus.OK}")
    // The request is Response from server 200: Success
    println("The request is ${HttpStatus.OK.toResponseString()}")

    // sealed class
    val netA = NetworkResult.Success("from network A")
    val netB = NetworkResult.Error(throw Exception("Error in network call"))

    println(netA)
    println(netB)

}

// *********************************************************************************************************************
// Inheritance
data class PaymentRequest(val amount:Double, val currency: String, val userId: String)

sealed class PaymentResult {
    data class Success(val id: String): PaymentResult()
    data class Declined(val reason: String): PaymentResult()
    data class NetworkError(val retrySec: Int): PaymentResult()
}

//Base class
abstract class PaymentProcessor(open val provider: String) {
    open fun fee(amount: Double): Double = amount * 0.029 + 0.30
    abstract fun process(req: PaymentRequest): PaymentResult
    open fun supportRefund(): Boolean = true
    open fun describe(): String = "Processor=$provider (~2.9% + $0.30)"
}

// sub class 1
class StripeProcessor(): PaymentProcessor("Stripe") {
    override fun fee(amount: Double) = super.fee(amount) + 0.10

    override fun process(req: PaymentRequest): PaymentResult {
        val total = req.amount + fee(req.amount)
        println("Processing the payment using Stripe for the amount: ${req.amount}")
        return PaymentResult.Success("Transaction completed for the amount ${req.amount} using Stripe")
    }

}

//sub class 2
class PayPalProcessor(): PaymentProcessor("PayPal") {
    override fun fee(amount: Double) = amount * 0.050 + 0.30

    override fun supportRefund(): Boolean {
        return false
    }

    override fun process(req: PaymentRequest): PaymentResult {
        println("Processing the payment for the amount ${req.amount} using PayPal")
        return PaymentResult.Declined("Transaction declined PayPal")
    }
}

fun inheritanceFun() {
    val request = PaymentRequest(500.0, "USD", "u_001")

    val processors: List<PaymentProcessor> = listOf(
        StripeProcessor(),
        PayPalProcessor()
    )

    processors.forEach { p ->
        println("-- ${p.describe()} (refunds=${p.supportRefund()}) --")

        when(val result = p.process(request)) {
            is PaymentResult.Success -> println("Success id=${request.userId}")
            is PaymentResult.Declined -> println("Declined id=${request.userId}")
            is PaymentResult.NetworkError -> println("Network error id=${request.userId}")
        }
        println()
    }

}

//**********************************************************************************************************************
// Dependency Inversion principle
// Violation
class EmailSender {
    fun send(msg: String) = println("EMAIL -> $msg")
}

class OrderServiceBad {
    fun placeOrder(id: String) {
        // ...process order...
        EmailSender().send("Order $id placed") // depends on a concrete class
    }
}

// Fix
// 1) Abstraction
interface Notifier { fun notify(msg: String) }

// 2) Low-level details implement the abstraction
class EmailNotifier : Notifier {
    override fun notify(msg: String) = println("EMAIL -> $msg")
}
class SmsNotifier : Notifier {
    override fun notify(msg: String) = println("SMS   -> $msg")
}

// 3) High-level policy depends ONLY on Notifier
class OrderService(private val notifier: Notifier) {
    fun placeOrder(id: String) {
        // ...process order...
        notifier.notify("Order $id placed")
    }
}

// 4)
fun OrderProcessor() {
    val prod = OrderService(EmailNotifier())
    // prod = OrderService(SmsNotifier())
    prod.placeOrder("A123")  // can notify by email + sms without changing OrderService
}



