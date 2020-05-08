package io.infinite.orbit.configurations.security

import io.infinite.blackbox.BlackBox
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

import javax.servlet.http.HttpServletRequest

@BlackBox
class AscendPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return "No Principal"
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "No Credentials"
    }

}
