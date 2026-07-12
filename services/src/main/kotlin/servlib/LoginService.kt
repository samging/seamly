package servlib

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import kotlin.jvm.Throws
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication
import javax.naming.AuthenticationException





interface LoginInterface {

	@Throws(AuthenticationException::class)
	fun loginJWT(credentials: UserPost): AuthSession

	@Throws(AuthenticationException::class)
	fun loginWebAuth(): Authentication

	@Throws(AuthenticationException::class)
	fun loginPasskey(): Authentication

	@Throws(AuthenticationException::class)
	fun loginPASETO(): Authentication

	@Throws(AuthenticationException::class)
	fun loginBRANCA(): Authentication

	@Throws(AuthenticationException::class)
	fun loginDPoP(): Authentication

	@Throws(AuthenticationException::class)
	fun loginSDJWT(): Authentication
}

@Configuration
open class LoginBean {

}


@SpringBootApplication
open class LoginService


fun main(args: Array<String>) {
	println("Initializing Spring Boot...")
	println("${args::class.qualifiedName}")
	runApplication<LoginService>(*args)
}