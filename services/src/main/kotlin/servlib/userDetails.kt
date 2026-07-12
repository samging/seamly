package servlib

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails



class UserInfoDetails(userInfo: UserInfo) : UserDetails {

	private val usernameField: String = userInfo.email
	private val passwordField: String = userInfo.password
	private val authoritiesList: List<GrantedAuthority> = userInfo.roles
		.map { SimpleGrantedAuthority(it) }

	override fun getAuthorities(): Collection<GrantedAuthority> = authoritiesList

	override fun getUsername(): String = usernameField

	override fun getPassword(): String = passwordField

	override fun isAccountNonExpired(): Boolean = true

	override fun isAccountNonLocked(): Boolean = true

	override fun isCredentialsNonExpired(): Boolean = true

	override fun isEnabled(): Boolean = true
}