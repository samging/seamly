package servlib

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class LoginService

fun main(args: Array<String>) {
	println("Initializing Spring Boot...")
	println("${args::class.qualifiedName}")
	runApplication<LoginService>(*args)
}