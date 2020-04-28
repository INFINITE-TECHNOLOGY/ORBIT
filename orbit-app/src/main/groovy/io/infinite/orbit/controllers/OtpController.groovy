package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.services.OtpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox
@Slf4j
class OtpController {

    @Autowired
    OtpService otpService

    @PostMapping(value = "/orbit/otp")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void otpSms(@RequestBody Otp otp) {
        otpService.otp(otp)
    }

}
