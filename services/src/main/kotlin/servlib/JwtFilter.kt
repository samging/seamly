package servlib

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import java.io.IOException

@Component
internal class JwtFilter(
	private val userDetailService: UserDetailsService,
	private val jwtService: JwtService
) : OncePerRequestFilter() {

	@Throws(ServletException::class, IOException::class)
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val authHeader: String? = request.getHeader("Authorization")
		var token: String? = null
		var username: String? = null

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7)
			username = jwtService.extractUsername(token)
		}

		if (username != null && SecurityContextHolder.getContext().authentication == null) {
			val userDetails = userDetailService.loadUserByUsername(username)

			if (token != null && jwtService.validateToken(token, userDetails)) {
				val authToken = UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.authorities
				).apply {
					details = WebAuthenticationDetailsSource().buildDetails(request)
				}

				SecurityContextHolder.getContext().authentication = authToken
			}
		}

		filterChain.doFilter(request, response)
	}
}