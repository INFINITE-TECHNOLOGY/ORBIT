package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.ManagedEmail
import io.infinite.orbit.model.UnmanagedEmail
import io.infinite.orbit.components.TemplateSelector
import io.infinite.orbit.other.TemplateTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox
@Slf4j
class ManagedEmailController {

    @Autowired
    TemplateSelector templateSelector

    @PostMapping(value = "/orbit/{clientId}/managedEmail")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void managedEmail(@RequestParam("managedEmail") ManagedEmail managedEmail,
               @PathVariable("clientId") String clientId
    ) {
        UnmanagedEmail unmanagedEmail = new UnmanagedEmail()
        unmanagedEmail.to = managedEmail.to
        unmanagedEmail.subject = templateSelector.executeTemplate(
                managedEmail.templateSelectionData,
                clientId,
                TemplateTypes.SUBJECT,
                managedEmail.templateValues
        )
        unmanagedEmail.text = templateSelector.executeTemplate(
                managedEmail.templateSelectionData,
                clientId,
                TemplateTypes.TEXT,
                managedEmail.templateValues
        )
    }

}
