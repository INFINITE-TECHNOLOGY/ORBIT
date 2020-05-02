package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.entities.Template
import io.infinite.orbit.model.ManagedOtpHandle
import io.infinite.orbit.model.ManagedSms
import io.infinite.orbit.repositories.TemplateRepository
import io.infinite.orbit.services.SendOtpSmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class TemplateController {

    @Autowired
    TemplateRepository templateRepository

    @PostMapping(value = "/{namespace}/templates")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void template(@PathVariable("namespace") String namespace, @RequestBody Template template) {
        template.namespace = namespace
        templateRepository.saveAndFlush(template)
    }

}
