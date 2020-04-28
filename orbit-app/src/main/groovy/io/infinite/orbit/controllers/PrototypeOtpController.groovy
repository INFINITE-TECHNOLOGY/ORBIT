package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.entities.PrototypeOtp
import io.infinite.orbit.repositories.PrototypeOtpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox
@Slf4j
class PrototypeOtpController {

    @Autowired
    PrototypeOtpRepository prototypeOtpRepository

    @PostMapping(value = "/orbit/{namespace}/prototypeOtp")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void prototypeOtp(@PathVariable("namespace") String namespace, @RequestBody PrototypeOtp prototypeOtp) {
        prototypeOtp.namespace = namespace
        prototypeOtpRepository.saveAndFlush(prototypeOtp)
    }

}