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
class FormularEmailController {

    @PostMapping(value = "/orbit/formularEmail")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void email(@RequestParam("email") Email email
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl()
        mailSender.setHost("smtp.gmail.com")
        mailSender.setPort(465)
        mailSender.setUsername(System.getenv("GMAIL_USERNAME"))
        mailSender.setPassword(System.getenv("GMAIL_PASSWORD"))
        Properties mailProp = mailSender.getJavaMailProperties()
        mailProp.put("mail.transport.protocol", "smtp")
        mailProp.put("mail.smtp.auth", "true")
        mailProp.put("mail.smtp.starttls.enable", "true")
        mailProp.put("mail.smtp.starttls.required", "true")
        mailProp.put("mail.debug", "true")
        mailProp.put("mail.smtp.ssl.enable", "true")
        mailProp.put("mail.smtp.user", System.getenv("GMAIL_USERNAME"))
        MimeMessage mimeMessage = mailSender.createMimeMessage()
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false)
        helper.setTo(email.to)
        helper.setSubject(email.subject)
        helper.setText(email.message, false)
        helper.setFrom(System.getenv("GMAIL_FROM"))
        mailSender.send(mimeMessage)
    }

}
