package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.model.ManagedEmail
import io.infinite.orbit.model.UnmanagedEmail
import io.infinite.orbit.other.TemplateTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class ManagedEmailService {

    @Autowired
    TemplateService templateSelector

    @Autowired
    UnmanagedEmailService unmanagedEmailService

    void managedEmail(ManagedEmail managedEmail, String namespace) {
        UnmanagedEmail unmanagedEmail = new UnmanagedEmail()
        unmanagedEmail.to = managedEmail.to
        unmanagedEmail.subject = templateSelector.executeTemplate(
                managedEmail.templateSelectionData,
                namespace,
                TemplateTypes.EMAIL_SUBJECT,
                managedEmail.templateValues
        )
        unmanagedEmail.text = templateSelector.executeTemplate(
                managedEmail.templateSelectionData,
                namespace,
                TemplateTypes.EMAIL_BODY,
                managedEmail.templateValues
        )
        unmanagedEmailService.unmanagedEmail(unmanagedEmail)
    }

}
