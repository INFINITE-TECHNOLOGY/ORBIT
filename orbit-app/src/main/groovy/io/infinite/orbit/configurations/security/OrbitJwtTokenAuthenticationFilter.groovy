package io.infinite.orbit.configurations.security

import groovy.util.logging.Slf4j
import io.infinite.ascend.validation.client.services.ClientAuthorizationValidationService
import io.infinite.blackbox.BlackBox
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * https://github.com/OmarElGabry/microservices-spring-boot/blob/master/spring-eureka-zuul/src/main/java/com/eureka/zuul/security/JwtTokenAuthenticationFilter.java
 */

@Slf4j
@BlackBox
@Service
class OrbitJwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    ClientAuthorizationValidationService clientAuthorizationValidationService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        clientAuthorizationValidationService.validateServletRequest(request, response, filterChain)
    }

}