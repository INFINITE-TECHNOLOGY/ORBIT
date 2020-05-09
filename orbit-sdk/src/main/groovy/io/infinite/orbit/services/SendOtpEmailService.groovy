package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.model.ManagedEmail
import io.infinite.orbit.model.ManagedOtpHandle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class SendOtpEmailService {

    @Autowired
    ManagedEmailService managedEmailService

    @Autowired
    SendOtpService otpService

    ManagedOtpHandle sendOtpEmail(ManagedEmail managedEmail) {
        try {
            Otp otp = otpService.sendOtp()
            managedEmail.templateValues.put("guid", otp.guid.toString())
            managedEmail.templateValues.put("otp", otp.otp)
            managedEmail.templateValues.put("maxAttemptsCount", otp.maxAttemptsCount.toString())
            managedEmail.templateValues.put("durationSeconds", otp.durationSeconds.toString())
            managedEmailService.managedEmail(managedEmail)
            new ManagedOtpHandle(guid: otp.guid)
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception generating OTP", exception)
        }
    }

}
