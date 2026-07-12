package servlib

import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserInfoService(
	private val repository: UserRepository,
	private val encoder: PasswordEncoder
) : UserDetailsService {

	@Throws(UsernameNotFoundException::class)
	override fun loadUserByUsername(username: String): UserDetails {
		val userInfo = repository.findByEmail(username)
			?: throw UsernameNotFoundException("User not found with email: $username")

		return UserInfoDetails(userInfo)
	}


	@Transactional
	fun registerUser(request: UserInfoDTO) {
		if (repository.existsByEmail(request.email)) {
			throw IllegalArgumentException("An account with the email ${request.email} already exists.")
		}

		val secureHash = encoder.encode(request.password)

		val newUser = UserInfo(
			email = request.email,
			password = secureHash
		)

		repository.save(newUser)
	}
}