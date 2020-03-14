package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.components.TemplateSelector
import io.infinite.orbit.model.ManagedSms
import io.infinite.orbit.model.UnmanagedSms
import io.infinite.orbit.other.TemplateTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox
@Slf4j
class ManagedSmsController {

    @Autowired
    TemplateSelector templateSelector

    @PostMapping(value = "/orbit/{partnerId}/managedSms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void managedSms(@RequestParam("managedSms") ManagedSms managedSms,
                    @PathVariable("partnerId") String partnerId
    ) {
        UnmanagedSms unmanagedSms = new UnmanagedSms()
        unmanagedSms.telephone = managedSms.telephone
        unmanagedSms.text = templateSelector.executeTemplate(
                managedSms.templateSelectionData,
                partnerId,
                TemplateTypes.TEXT,
                managedSms.templateValues
        )
    }

}
