package servlib

data class AuthSession (
	val accessToken: String,
	val refreshToken: String?,
	val tokenType: String = "Bearer",
)

data class UserPost(
	val username: String,
	val password: String,
)