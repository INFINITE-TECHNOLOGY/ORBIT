package io.infinite.orbit.services

import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpResponse
import io.infinite.orbit.model.HistoryRecord
import io.infinite.orbit.repositories.ReconciliationRecordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import javax.annotation.PostConstruct
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
class HistoryService extends CrmServiceBase {

    @Autowired
    ReconciliationRecordRepository reconciliationRecordRepository

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")

    XmlSlurper xmlSlurper = new XmlSlurper()

    @PostConstruct
    void downloadHistory() {
        Optional<Date> dateFrom = reconciliationRecordRepository.lastDownloadDate()
        Integer resultCount = 0
        Integer page = 1
        while (resultCount == 0) {
            HttpResponse httpResponse = crmRequest("""<request point="315">
    <reconciliation 
    begin="${dateFrom.present ? dateFormatter.format(dateFrom.get().toInstant()) : "2020-01-01T00:00:00+0300"}" 
    end="${dateFormatter.format(ZonedDateTime.now())}" 
    payments="1" 
    offset="$page"/>
</request>""")
            def xmlResponse = xmlSlurper.parse(httpResponse.body)
            resultCount = xmlResponse.response.result.@count
            page++
            log.debug(">>>>>>>$resultCount")
        }
    }

    Set<HistoryRecord> getHistory(String userGuid, Optional<String> tranCount) {

        return []
    }

}
