package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.ManagedEmail
import io.infinite.orbit.services.ManagedEmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox
@Slf4j
class ManagedEmailController {

    @Autowired
    ManagedEmailService managedEmailService

    @PostMapping(value = "/orbit/{clientId}/managedEmail")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void managedEmail(@RequestParam("managedEmail") ManagedEmail managedEmail,
                      @PathVariable("clientId") String clientId
    ) {
        managedEmailService.managedEmail(managedEmail, clientId)
    }

}
