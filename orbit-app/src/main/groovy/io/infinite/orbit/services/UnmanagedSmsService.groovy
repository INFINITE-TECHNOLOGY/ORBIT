package io.infinite.orbit.services

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.orbit.model.UnmanagedSms
import org.springframework.stereotype.Service

@Service
@BlackBox
@Slf4j
class UnmanagedSmsService {

    void unmanagedSms(UnmanagedSms unmanagedSms) {
        Twilio.init(System.getenv("TWILIO_SID"), System.getenv("TWILIO_TOKEN"))
        Message message = Message.creator(
                new PhoneNumber(unmanagedSms.telephone),
                new PhoneNumber(unmanagedSms.telephone),
                unmanagedSms.text
        ).create()
        log.debug(message.getSid())
    }

}
