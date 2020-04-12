package io.infinite.orbit.configurations.security

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.infinite.ascend.other.AscendException
import io.infinite.ascend.validation.model.AscendHttpRequest
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpRequest
import io.infinite.http.HttpResponse
import io.infinite.http.SenderDefaultHttps
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.client.HttpServerErrorException
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
class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper()
            String authorizationHeader = request.getHeader("Authorization")
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                chain.doFilter(request, response)
                return
            }
            String incomingUrl
            if (request.getQueryString() != null) {
                incomingUrl = request.requestURL
                        .append('?')
                        .append(request.getQueryString())
                        .toString()
            } else {
                incomingUrl = request.requestURL
            }
            AscendHttpRequest ascendHttpRequest = new AscendHttpRequest(
                    authorizationHeader: authorizationHeader,
                    incomingUrl: incomingUrl,
                    method: request.method,
                    body: null as String
            )
            HttpRequest ascendRequest = new HttpRequest(
                    url: "${System.getenv("ASCEND_TRUSTED_URL")}",
                    headers: ["content-type": "application/json"],
                    method: "POST",
                    body: objectMapper.writeValueAsString(ascendHttpRequest)
            )
            HttpResponse ascendResponse = new HttpResponse()
            new SenderDefaultHttps().sendHttpMessage(ascendRequest, ascendResponse)
            if (ascendResponse.status != 200) {
                throw new AscendException("Failed Ascend HTTP status")
            }
            AscendHttpRequest ascendHttpResponse = objectMapper.readValue(ascendResponse.body, AscendHttpRequest.class)
            if (ascendHttpResponse.status != 200) {
                SecurityContextHolder.clearContext()
                response.sendError(ascendHttpResponse.status, "Unauthorized.")
                return
            }
            PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken =
                    new PreAuthenticatedAuthenticationToken(ascendHttpResponse.authorization.identity, ascendHttpResponse.authorization.identity?.authentications)
            preAuthenticatedAuthenticationToken.setAuthenticated(true)
            SecurityContextHolder.getContext().setAuthentication(preAuthenticatedAuthenticationToken)
            chain.doFilter(request, response)
        }
        catch (Exception e) {
            log.error("Exception", e)
            response.sendError(500, e.message)
            SecurityContextHolder.clearContext()
        }
    }

}