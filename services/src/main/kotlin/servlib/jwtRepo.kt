package servlib

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

@Repository
interface UserRepository : JpaRepository<UserInfo, Long> {

	@Modifying
	@Query("UPDATE UserInfo u SET u.password = :newPasswordHash WHERE u.id = :userId")
	fun findByEmail(username: String): UserInfo?

	fun existsByEmail(email: String): Boolean
}
