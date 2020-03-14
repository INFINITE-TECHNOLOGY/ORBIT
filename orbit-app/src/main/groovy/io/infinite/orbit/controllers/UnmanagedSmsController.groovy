package io.infinite.orbit.controllers

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.UnmanagedSms
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@BlackBox
@Slf4j
class UnmanagedSmsController {

    @PostMapping(value = "/orbit/unmanagedSms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void sms(@RequestParam("unmanagedSms") UnmanagedSms unmanagedSms
    ) {
        Twilio.init(System.getenv("TWILIO_SID"), System.getenv("TWILIO_TOKEN"))
        Message message = Message.creator(
                new PhoneNumber(unmanagedSms.telephone),
                new PhoneNumber(unmanagedSms.telephone),
                unmanagedSms.text
        ).create()
        log.debug(message.getSid())
    }

}
