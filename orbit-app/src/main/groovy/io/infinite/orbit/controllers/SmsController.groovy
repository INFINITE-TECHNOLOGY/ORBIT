package io.infinite.orbit.controllers

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.Sms
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@BlackBox
@Slf4j
class SmsController {

    @PostMapping(value = "/orbit/sms")
    @ResponseBody
    @CompileDynamic
    @CrossOrigin
    void sms(@RequestParam("sms") Sms sms
    ) {
        Twilio.init(System.getenv("TWILIO_SID"), System.getenv("TWILIO_TOKEN"))
        Message message = Message.creator(
                new PhoneNumber(sms.telephone),
                new PhoneNumber(sms.telephone),
                sms.message
        ).create()
        log.debug(message.getSid())
    }

}
