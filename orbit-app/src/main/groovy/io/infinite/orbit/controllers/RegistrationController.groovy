package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Registration
import io.infinite.orbit.services.RegistrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class RegistrationController {

    @Autowired
    RegistrationService registrationService

    @PostMapping(value = "/public/validateRegistrationGuid/{guid}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void validateRegistrationGuid(@PathVariable("guid") String guid) {
        registrationService.validateGuid(guid)
    }

    @GetMapping(value = "/{namespace}/registration/{phone}")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Registration findByNamespaceAndPhone(@PathVariable("namespace") String namespace, @PathVariable("phone") String phone) {
        return registrationService.findByNamespaceAndPhone(namespace, phone)
    }

    @PostMapping(value = "/{namespace}/registration")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    Registration createRegistration(@PathVariable("namespace") String namespace, @RequestParam String phone) {
        return registrationService.createRegistration(namespace, phone)
    }

}
