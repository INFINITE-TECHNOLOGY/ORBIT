package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.model.ManagedOtpHandle
import io.infinite.orbit.model.ManagedSms
import io.infinite.orbit.services.SendOtpSmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class SendOtpSmsController {

    @Autowired
    SendOtpSmsService otpSmsService

    @PostMapping(value = "/{namespace}/sendOtpSms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    ManagedOtpHandle otpSms(@PathVariable("namespace") String namespace, @RequestBody ManagedSms managedSms) {
        return otpSmsService.sendOtpSms(managedSms, namespace)
    }

}
