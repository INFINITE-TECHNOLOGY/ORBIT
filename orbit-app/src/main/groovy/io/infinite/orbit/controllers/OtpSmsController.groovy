package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.entities.Otp
import io.infinite.orbit.services.OtpSmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@BlackBox
@Slf4j
class OtpSmsController {

    @Autowired
    OtpSmsService otpSmsService

    @GetMapping(value = "/orbit/{namespace}/otpSms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Otp otpSms(@PathVariable("namespace") String namespace) {
        return otpSmsService.otpSms(namespace)
    }

}
