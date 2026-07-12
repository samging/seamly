package servlib

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.lang.Objects
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import io.jsonwebtoken.Jwts.claims
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.security.core.userdetails.UserDetails


@Service
open class UserInteractionService(private val userRepository: UserRepository) {
	fun getEmail(username: String): String {
		return userRepository.findByEmail(username)?.email
			?: throw NoSuchElementException("User with name $username not found")
	}
}
@Component
internal class JwtService(
	private val userService: UserInteractionService
) {
	companion object {
		private const val SECRET: String = "MDk4MjM0OTgzMjQ3OTIzODQ3OTIzODQ3OTIzODQ3OTIzODQ3OTIzODQ3OTIzODQ3OTIzODQ3OTIzODQ3OTI="
	}

	private val signKey: SecretKey by lazy {
		val keyBytes = Decoders.BASE64.decode(SECRET)
		Keys.hmacShaKeyFor(keyBytes)
	}

	@Throws(NoSuchElementException::class)
	fun generateToken(name: String): String {
		val claims = mapOf<String, Any>()
		val email = userService.getEmail(name)
		return createToken(claims, email)
	}

	private fun createToken(claims: Map<String, Any>, subject: String): String {
		return Jwts.builder()
			.claims(claims)
			.subject(subject)
			.issuedAt(Date(System.currentTimeMillis()))
			.expiration(Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes
			.signWith(signKey)
			.compact()
	}

	fun extractUsername(token: String): String {
		return extractClaim(token) { it.subject }
	}

	fun extractExpiration(token: String): Date {
		return extractClaim(token) { it.expiration }
	}

	fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
		val claims = extractAllClaims(token)
		return claimsResolver(claims)
	}

	private fun extractAllClaims(token: String): Claims {
		return Jwts.parser()
			.verifyWith(signKey)
			.build()
			.parseSignedClaims(token)
			.payload
	}

	private fun isTokenExpired(token: String): Boolean {
		return extractExpiration(token).before(Date())
	}

	// 4. Added proper function signatures here
	fun validateToken(token: String, userDetails: UserDetails): Boolean {
		val username = extractUsername(token)
		return (username == userDetails.username && !isTokenExpired(token))
	}
}