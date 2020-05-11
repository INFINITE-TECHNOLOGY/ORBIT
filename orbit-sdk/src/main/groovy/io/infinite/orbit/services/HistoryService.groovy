package io.infinite.orbit.services

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpRequest
import io.infinite.http.HttpResponse
import io.infinite.http.SenderDefaultHttps
import io.infinite.orbit.model.HistoryRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class HistoryService {

    ObjectMapper objectMapper = new ObjectMapper()

    SenderDefaultHttps senderDefaultHttps = new SenderDefaultHttps()

    @Value('${crmUrl}')
    String crmUrl

    @Value('${crmToken}')
    String crmToken

    Set<HistoryRecord> getHistory(String userGuid) {
        HttpResponse httpResponse = senderDefaultHttps.expectStatus(
                new HttpRequest(
                        url: "$crmUrl/external/extended",
                        method: "POST",
                        headers: [
                                "Content-Type": "application/xml",
                                "Accept"      : "application/xml",
                                "X-Signature" : crmToken
                        ],
                        body: """<request point="12345">
<reconciliation begin="2007-10-12T12:00:00+0300"
end="2007-10-13T12:00:00+0300"
payments="1"
offset="1000"/>
</request>"""
                ), 200
        )
        return []
    }


}
