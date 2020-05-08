package io.infinite.orbit.services


import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.model.ManagedSms
import io.infinite.orbit.model.UnmanagedSms
import io.infinite.orbit.other.TemplateTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class ManagedSmsService {

    @Autowired
    TemplateService templateSelector

    @Autowired
    UnmanagedSmsService unmanagedSmsService

    void managedSms(ManagedSms managedSms) {
        UnmanagedSms unmanagedSms = new UnmanagedSms()
        unmanagedSms.telephone = managedSms.telephone
        unmanagedSms.text = templateSelector.executeTemplate(
                managedSms.templateSelectionData,
                TemplateTypes.SMS,
                managedSms.templateValues
        )
        unmanagedSmsService.unmanagedSms(unmanagedSms)
    }

}
