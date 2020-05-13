package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpResponse
import io.infinite.orbit.model.HistoryRecord
import org.springframework.stereotype.Controller

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class HistoryService extends CrmServiceBase {


    Set<HistoryRecord> getHistory(String userGuid) {
        HttpResponse httpResponse = crmRequest("""<request point="315">
    <reconciliation begin="2007-10-12T12:00:00+0300" end="2007-10-13T12:00:00+0300" payments="1" offset="1000"/>
</request>""")
        return []
    }

}
