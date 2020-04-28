package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.model.ManagedOtp
import io.infinite.orbit.services.ValidateOtpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox
@Slf4j
class ValidateOtpController {

    @Autowired
    ValidateOtpService otpService

    @PostMapping(value = "/orbit/{namespace}/validateOtp")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void validateOtp(@PathVariable("namespace") String namespace, @RequestBody ManagedOtp managedOtp) {
        otpService.validateOtp(managedOtp, namespace)
    }

}
