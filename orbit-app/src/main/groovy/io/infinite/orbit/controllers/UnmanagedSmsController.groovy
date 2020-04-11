package io.infinite.orbit.controllers


import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.UnmanagedSms
import io.infinite.orbit.services.UnmanagedSmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@BlackBox
@Slf4j
class UnmanagedSmsController {

    @Autowired
    UnmanagedSmsService unmanagedSmsService

    @PostMapping(value = "/orbit/unmanagedSms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void sms(@RequestParam("unmanagedSms") UnmanagedSms unmanagedSms
    ) {
        unmanagedSmsService.unmanagedSms(unmanagedSmsService)
    }

}
