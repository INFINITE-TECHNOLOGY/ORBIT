package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.ManagedSms
import io.infinite.orbit.services.ManagedSmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox
@Slf4j
class ManagedSmsController {

    @Autowired
    ManagedSmsService managedSmsService

    @PostMapping(value = "/orbit/{clientId}/managedSms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void managedSms(@RequestBody ManagedSms managedSms,
                    @PathVariable("clientId") String clientId
    ) {
        managedSmsService.managedSms(managedSms, clientId)
    }

}
