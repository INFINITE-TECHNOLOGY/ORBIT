package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.ManagedEmail
import io.infinite.orbit.model.UnmanagedEmail
import io.infinite.orbit.other.TemplateTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@BlackBox
@Slf4j
class ManagedEmailService {

    @Autowired
    TemplateService templateSelector

    @Autowired
    UnmanagedEmailService unmanagedEmailService

    void managedEmail(ManagedEmail managedEmail, String clientId) {
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
        unmanagedEmailService.unmanagedEmail(unmanagedEmail)
    }

}
