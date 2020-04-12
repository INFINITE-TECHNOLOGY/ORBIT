package io.infinite.orbit.services

import groovy.transform.ToString
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.entities.Template
import io.infinite.orbit.model.TemplateSelectionData
import io.infinite.orbit.other.OrbitException
import io.infinite.orbit.other.TemplateTypes
import io.infinite.orbit.repositories.TemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@ToString(includeNames = true, includeFields = true)
@BlackBox
@Slf4j
@Component
class TemplateService {

    @Autowired
    TemplateRepository templateRepository

    Template priorityOne(Set<Template> templates, TemplateSelectionData templateSelectionData) {
        return templates.find {
            it.application == templateSelectionData.application &&
                    it.language == templateSelectionData.language
        }
    }

    Template priorityTwo(Set<Template> templates, TemplateSelectionData templateSelectionData) {
        return templates.find {
            it.application == templateSelectionData.application
        }
    }

    Template priorityThree(Set<Template> templates, TemplateSelectionData templateSelectionData) {
        return templates.find {
            it.language == templateSelectionData.language
        }
    }

    Template findTemplate(Set<Template> templates, TemplateSelectionData templateSelectionData) {
        Template result = [
                priorityOne(templates, templateSelectionData),
                priorityTwo(templates, templateSelectionData),
                priorityThree(templates, templateSelectionData)
        ].find { it != null }
        if (result == null) {
            throw new OrbitException("Template not found: $templateSelectionData")
        }
        return result
    }

    String executeTemplate(TemplateSelectionData templateSelectionData, String clientId, TemplateTypes templateType, Map<String, String> templateValues) {
        Set<Template> templates = templateRepository.findByNameAndClientIdAndTemplateType(
                templateSelectionData.templateName,
                clientId,
                templateType.value()
        )
        Template template = findTemplate(templates, templateSelectionData)
        String result = template.text
        templateValues.each { k, v ->
            result = result.replace("\${" + k + "}", v)
        }
        return result
    }

}
