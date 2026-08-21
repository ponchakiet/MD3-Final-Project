package re.edu.quan_ly_thuc_tap.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import re.edu.quan_ly_thuc_tap.config.security.UserDetailsServiceCustom;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsServiceCustom userDetailServiceCustom;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (token != null) {
            try {
                String username = jwtProvider.extractUserName(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailServiceCustom.loadUserByUsername(username);

                    // Validate token
                    if (jwtProvider.validateToken(token, userDetails)) {

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (MalformedJwtException e) {
                log.error("Invalid token: {}", e.getMessage());
                request.setAttribute("exception", "MALFORMED_TOKEN");
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported token: {}", e.getMessage());
                request.setAttribute("exception", "UNSUPPORTED_TOKEN");
            } catch (ExpiredJwtException e) {
                log.error("Expired token: {}", e.getMessage());
                request.setAttribute("exception", "EXPIRED_TOKEN");
            } catch (IllegalArgumentException e) {
                log.error("Jwt key string invalid: {}", e.getMessage());
                request.setAttribute("exception", "ILLEGAL_ARGUMENT_TOKEN");
            } catch (Exception e) {
                log.error("Lỗi không xác định khi xác thực Token: {}", e.getMessage());
                request.setAttribute("exception", "UNKNOWN_ERROR");
            }
        } else {
            request.setAttribute("exception", "MISSING_TOKEN");
        }

        // Cho phép request đi tiếp
        filterChain.doFilter(request, response);
    }


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
