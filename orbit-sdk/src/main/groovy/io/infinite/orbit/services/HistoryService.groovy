package io.infinite.orbit.services

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpResponse
import io.infinite.orbit.entities.ReconciliationRecord
import io.infinite.orbit.model.HistoryRecord
import io.infinite.orbit.repositories.ReconciliationRecordRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import javax.annotation.PostConstruct
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
@CompileDynamic
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
        while (resultCount != 0) {
            HttpResponse httpResponse = crmRequest("""<request point="315">
    <reconciliation 
    begin="${dateFrom.present ? dateFormatter.format(dateFrom.get().toInstant()) : "2020-01-01T00:00:00+0300"}" 
    end="${dateFormatter.format(ZonedDateTime.now())}" 
    payments="1" 
    offset="$page"/>
</request>""")
            def response = xmlSlurper.parseText(httpResponse.body)
            resultCount = response.result.@count.toInteger()
            if (resultCount > 0) {
                reconciliationRecordRepository.saveAll(response.result.payment.collect {convert(it)})
            }
            page++
        }
    }

    ReconciliationRecord convert(def xmlRecord) {
        return new ReconciliationRecord(
                crmId: xmlRecord.@id,
                date: LocalDate.parse(xmlRecord.@date, dateFormatter).toDate(),
                state: xmlRecord.@state,
                substate: xmlRecord.@substate,
                code: xmlRecord.@code,
                crmFinal: xmlRecord.@final,
                trans: xmlRecord.@trans,
                sum: xmlRecord.@sum,
                service: xmlRecord.@service,
                market: xmlRecord.@market,
                dealer: xmlRecord.@dealer,
                branch: xmlRecord.@branch,
                id_point: xmlRecord.@id_point,
                account: xmlRecord.@account,
                tender: xmlRecord.@tender,
                payment_type: xmlRecord.@payment_type
        )
    }

    Set<HistoryRecord> getHistory(String userGuid, Optional<String> tranCount) {

        return []
    }

}
