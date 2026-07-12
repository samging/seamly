package servlib

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import kotlin.time.Instant
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType

enum class UserRole {
	ROLE_USER,
	ROLE_ADMIN,
	ROLE_MANAGER
}
@Entity
data class UserInfoDTO(
	@field:NotBlank(message = "Email is required")
	val email: String,

	@Column(nullable = false, columnDefinition = "TEXT")
	@field:NotBlank(message = "Username/Email is required")
	@field:Size(min = 8, message = "Username must be between 8 and 60 characters")
	val password: String,
)

@Entity
@Table(name = "user_info") //snake case!
data class UserInfo(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "user_id", nullable = false)
	val id: Long = 0,

	@Column(nullable = false, name = "email")
	@field:NotBlank(message = "Username or password")
	val email: String,

	@Column(name = "role")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
	@Enumerated(EnumType.STRING)
	val roles: Set<String> = setOf("ROLE_USER"),

	@Column(nullable = false, columnDefinition = "TEXT")
	val password: String,
)

@Entity
@Table(name = "refresh_tokens")
data class jwtSessionEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,

	@Column(nullable = false, unique = true)
	val token: String,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = false)
	val user: UserInfo,

	@Column(nullable = false)
	val expirity: Instant,

	@Column(nullable = false)
	val revoked: Boolean = false,
)
