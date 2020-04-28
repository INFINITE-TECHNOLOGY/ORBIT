package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.model.ManagedOtpSms
import io.infinite.orbit.repositories.NamespaceRepository
import io.infinite.orbit.repositories.OtpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class OtpService {

    @Autowired
    NamespaceRepository namespaceRepository

    @Autowired
    OtpRepository otpRepository

    void otp(ManagedOtpSms managedOtpSms) {
        try {
            Optional<Otp> otpOptional = otpRepository.findByGuidAndNamespace(managedOtpSms.guid, managedOtpSms.namespace)
            if (!otpOptional.present) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP GUID not found")
            }
            Otp actualOtp = otpOptional.get()
            if (actualOtp.expiryDate.before(new Date())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired")
            }
            if (actualOtp.attemptsCount >= actualOtp.maxAttemptsCount) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP validation maximum attempts count exceeded")
            }
            if (actualOtp.otp != managedOtpSms.otp) {
                actualOtp.attemptsCount++
                otpRepository.saveAndFlush(actualOtp)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong OTP")
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception validating OTP", exception)
        }
    }

}
