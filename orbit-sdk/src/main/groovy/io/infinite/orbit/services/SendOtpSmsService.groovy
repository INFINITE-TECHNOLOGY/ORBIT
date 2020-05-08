package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.entities.PrototypeOtp
import io.infinite.orbit.model.ManagedOtpHandle
import io.infinite.orbit.model.ManagedSms
import io.infinite.orbit.other.OrbitException
import io.infinite.orbit.repositories.OtpRepository
import io.infinite.orbit.repositories.PrototypeOtpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

import java.security.SecureRandom
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class SendOtpSmsService {

    @Autowired
    ManagedSmsService managedSmsService

    @Autowired
    PrototypeOtpRepository prototypeOtpRepository

    @Autowired
    OtpRepository otpRepository

    ManagedOtpHandle sendOtpSms(ManagedSms managedSms) {
        try {
            List<PrototypeOtp> prototypeOtpOptional = prototypeOtpRepository.findAll()
            if (prototypeOtpOptional.empty) {
                throw new OrbitException("Prototype OTP is not configured.")
            }
            PrototypeOtp prototypeOtp = prototypeOtpOptional.first()
            Otp otp = otpRepository.saveAndFlush(new Otp(
                    otp: new DecimalFormat("".padLeft(prototypeOtp.length, "0")).format(new SecureRandom().nextInt("".padLeft(prototypeOtp.length, "9").toInteger())),
                    creationDate: new Date(),
                    expiryDate: (Instant.now() + Duration.ofSeconds(prototypeOtp.durationSeconds)).toDate(),
                    maxAttemptsCount: prototypeOtp.maxAttemptsCount,
                    durationSeconds: prototypeOtp.durationSeconds
            ))
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
