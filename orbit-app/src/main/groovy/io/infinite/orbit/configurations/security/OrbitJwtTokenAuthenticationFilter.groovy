package io.infinite.orbit.configurations.security


import groovy.util.logging.Slf4j
import io.infinite.ascend.web.security.JwtTokenAuthenticationFilter
import io.infinite.blackbox.BlackBox
import org.springframework.stereotype.Service

/**
 * https://github.com/OmarElGabry/microservices-spring-boot/blob/master/spring-eureka-zuul/src/main/java/com/eureka/zuul/security/JwtTokenAuthenticationFilter.java
 */

@Slf4j
@BlackBox
@Service
class OrbitJwtTokenAuthenticationFilter extends JwtTokenAuthenticationFilter {


}