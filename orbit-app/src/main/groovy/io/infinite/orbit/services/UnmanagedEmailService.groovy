package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.UnmanagedEmail
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

import javax.mail.internet.MimeMessage

@Service
@BlackBox
@Slf4j
class UnmanagedEmailService {

    void unmanagedEmail(UnmanagedEmail unmanagedEmail) {
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
        helper.setTo(unmanagedEmail.to)
        helper.setSubject(unmanagedEmail.subject)
        helper.setText(unmanagedEmail.text, false)
        helper.setFrom(System.getenv("GMAIL_FROM"))
        mailSender.send(mimeMessage)
    }

}
