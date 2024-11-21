package config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import util.JwtUtil;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwtToken = null;
        String username = null;

        try {

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.warn("Authorization header is missing or " +
                        "does not start with Bearer for request "+request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }


            jwtToken = authorizationHeader.substring(7);
            if (jwtToken.isEmpty()) {
                logger.warn("JWT token is empty");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is empty");
                return;
            }


            username = jwtUtil.extractUsername(jwtToken);
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
            return;
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT token", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed JWT token");
            return;
        } catch (Exception e) {
            logger.error("Error extracting username from JWT token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwtToken, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
