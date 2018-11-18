package sample

fun hello(): String = "Hello, Kotlin/Native!\n"

fun main(args: Array<String>) {
    println(hello())
    platform.posix.system("date")
    platform.posix.system("ls -la")
}