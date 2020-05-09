package io.infinite.orbit.services

import com.sendgrid.Content
import com.sendgrid.Email
import com.sendgrid.Mail
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGrid
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.orbit.model.UnmanagedEmail
import org.springframework.stereotype.Service

@Service
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class UnmanagedEmailService {

    void unmanagedEmail(UnmanagedEmail unmanagedEmail) {
        Email from = new Email(System.getenv("GMAIL_USERNAME"))
        String subject = unmanagedEmail.subject
        Email to = new Email(unmanagedEmail.to)
        Content content = new Content("text/plain", unmanagedEmail.text)
        Mail mail = new Mail(from, subject, to, content)
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"))
        Request request = new Request()
        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()
        Response response = sg.api(request)
        System.out.println(response.statusCode)
        System.out.println(response.body)
        System.out.println(response.headers)
    }

}
