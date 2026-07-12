package servlib

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
internal class SecurityConfig(
	private val jwtFilter: JwtFilter,
	private val userDetailsService: UserDetailsService,
	private val passwordEncoder: PasswordEncoder
) {
	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http {
			csrf { disable() }

			authorizeHttpRequests {
				authorize("/home/**", permitAll)
				authorize("/user/**", hasAuthority("ROLE_USER"))
				authorize("/admin/**", hasAuthority("ROLE_ADMIN"))
				authorize(anyRequest, authenticated)
			}

			sessionManagement {
				sessionCreationPolicy = SessionCreationPolicy.STATELESS
			}

			authenticationProvider()
			addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtFilter)
		}
		return http.build()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	@Bean
	fun authenticationProvider(): AuthenticationProvider {
		val provider = DaoAuthenticationProvider()
		provider.setUserDetailsService(userDetailsService)
		provider.setPasswordEncoder(passwordEncoder())
		return provider
	}

	@Bean
	@Throws(Exception::class)
	fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
		return config.getAuthenticationManager()
	}
}