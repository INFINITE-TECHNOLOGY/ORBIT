package io.infinite.orbit.controllers

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.Email
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import javax.mail.internet.MimeMessage

@Controller
@BlackBox
@Slf4j
class FormularSmsController {

    @PostMapping(value = "/orbit/formularSms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void email(@RequestParam("email") Email email
    ) {

    }

}
