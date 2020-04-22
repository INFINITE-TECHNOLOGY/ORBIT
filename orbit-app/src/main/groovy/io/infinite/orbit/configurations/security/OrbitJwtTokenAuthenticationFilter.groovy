package io.infinite.orbit.configurations.security

import groovy.util.logging.Slf4j
import io.infinite.ascend.validation.client.services.ClientAuthorizationValidationService
import io.infinite.blackbox.BlackBox
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * https://github.com/OmarElGabry/microservices-spring-boot/blob/master/spring-eureka-zuul/src/main/java/com/eureka/zuul/security/JwtTokenAuthenticationFilter.java
 */

@Slf4j
@BlackBox
@Service
class OrbitJwtTokenAuthenticationFilter extends OncePerRequestFilter {

    ClientAuthorizationValidationService clientAuthorizationValidationService = new ClientAuthorizationValidationService()

    @Value('${ascendValidationUrl}')
    String ascendValidationUrl

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        clientAuthorizationValidationService.validateServletRequest(ascendValidationUrl, request, response, filterChain)
    }

}