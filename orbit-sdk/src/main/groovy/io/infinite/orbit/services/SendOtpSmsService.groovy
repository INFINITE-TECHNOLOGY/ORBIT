package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.model.ManagedOtpHandle
import io.infinite.orbit.model.ManagedSms
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class SendOtpSmsService {

    @Autowired
    ManagedSmsService managedSmsService

    @Autowired
    SendOtpService otpService

    ManagedOtpHandle sendOtpSms(ManagedSms managedSms) {
        try {
            Otp otp = otpService.sendOtp()
            managedSms.templateValues.put("guid", otp.guid.toString())
            managedSms.templateValues.put("otp", otp.otp)
            managedSms.templateValues.put("maxAttemptsCount", otp.maxAttemptsCount.toString())
            managedSms.templateValues.put("durationSeconds", otp.durationSeconds.toString())
            managedSmsService.managedSms(managedSms)
            new ManagedOtpHandle(guid: otp.guid)
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception generating OTP", exception)
        }
    }

}
