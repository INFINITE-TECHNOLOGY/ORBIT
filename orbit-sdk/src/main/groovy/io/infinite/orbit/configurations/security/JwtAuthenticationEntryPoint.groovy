package io.infinite.orbit.configurations.security

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * https://github.com/OmarElGabry/microservices-spring-boot/blob/master/spring-eureka-zuul/src/main/java/com/eureka/zuul/security/JwtAuthenticationEntryPoint.java
 */

@Component
@Slf4j
@BlackBox
class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED")
    }

}