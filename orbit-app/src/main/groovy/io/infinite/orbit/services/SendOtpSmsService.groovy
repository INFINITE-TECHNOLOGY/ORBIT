package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.entities.PrototypeOtp
import io.infinite.orbit.model.ManagedSms
import io.infinite.orbit.model.ManagedOtpHandle
import io.infinite.orbit.repositories.NamespaceRepository
import io.infinite.orbit.repositories.OtpRepository
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
    NamespaceRepository namespaceRepository

    @Autowired
    OtpRepository otpRepository

    ManagedOtpHandle sendOtpSms(ManagedSms managedSms, String namespaceName) {
        try {
            PrototypeOtp prototypeOtp = namespaceRepository.findByName(namespaceName).prototypeOtp
            Otp otp = otpRepository.saveAndFlush(new Otp(
                    namespace: namespaceName,
                    otp: new DecimalFormat("".padLeft(prototypeOtp.length, "0")).format(new SecureRandom().nextInt("".padLeft(prototypeOtp.length, "9").toInteger())),
                    creationDate: new Date(),
                    expiryDate: (Instant.now() + Duration.ofSeconds(prototypeOtp.durationSeconds)).toDate(),
                    maxAttemptsCount: prototypeOtp.maxAttemptsCount
            )).strip()
            managedSms.templateValues.put("otp", otp)
            managedSmsService.managedSms(
                    managedSms,
                    namespaceName
            )
            new ManagedOtpHandle(
                    guid: otp.guid,
            )
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception generating OTP", exception)
        }
    }

}
