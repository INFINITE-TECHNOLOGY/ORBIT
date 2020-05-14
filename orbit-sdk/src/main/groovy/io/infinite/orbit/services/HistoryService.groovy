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

    void downloadHistory() {
        Optional<Date> dateFrom = reconciliationRecordRepository.lastDownloadDate()
        Integer offset = 1
        while (true) {
            HttpResponse httpResponse = crmRequest("""<request point="315">
    <reconciliation 
    begin="${dateFrom.present ? dateFormatter.format(dateFrom.get().toInstant()) : "2020-01-01T00:00:00+0300"}" 
    end="${dateFormatter.format(ZonedDateTime.now())}" 
    payments="1" 
    offset="${offset * 1000}"/>
</request>""")
            def response = xmlSlurper.parseText(httpResponse.body)
            if (response.result.@count.toInteger() > 0) {
                reconciliationRecordRepository.saveAll(response.result.payment.collect { convertToReconciliationRecord(it) })
                offset++
            } else {
                break
            }
        }
    }

    ReconciliationRecord convertToReconciliationRecord(def xmlRecord) {
        return new ReconciliationRecord(
                crmId: xmlRecord.@id,
                date: LocalDate.parse(xmlRecord.@date.toString(), dateFormatter).toDate(),
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

    HistoryRecord convertToHistoryRecord(ReconciliationRecord reconciliationRecord) {
        return new HistoryRecord(
                date: reconciliationRecord.date,
                amount: Long.valueOf(reconciliationRecord.sum),
                currency: "USD"
        )
    }

    Set<HistoryRecord> getHistory(String userGuid, Optional<String> tranCount) {
        downloadHistory()
        return reconciliationRecordRepository.findAll().collect { convertToHistoryRecord(it) }.sort { a, b -> a.date <=> b.date }[0..tranCount]
    }

}
