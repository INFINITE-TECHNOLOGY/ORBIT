package io.infinite.orbit.services

import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel
import io.infinite.http.HttpResponse
import io.infinite.orbit.entities.ReconciliationRecord
import io.infinite.orbit.model.HistoryRecord
import io.infinite.orbit.other.OrbitException
import io.infinite.orbit.repositories.ReconciliationRecordRepository
import org.apache.commons.lang3.time.FastDateFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import java.time.Duration
import java.time.Instant

@Controller
@BlackBox(level = CarburetorLevel.METHOD)
@Slf4j
@CompileDynamic
class HistoryService extends CrmServiceBase {

    @Autowired
    ReconciliationRecordRepository reconciliationRecordRepository

    FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZ")

    XmlSlurper xmlSlurper = new XmlSlurper()

    void downloadHistory() {
        Optional<Date> dateFrom = reconciliationRecordRepository.lastDownloadDate()
        Integer offset = 0
        while (true) {
            HttpResponse httpResponse = crmRequest("""<request point="315">
    <reconciliation 
    begin="${dateFrom.present ? fastDateFormat.format(dateFrom.get()) : "2020-01-01T00:00:00+0000"}" 
    end="${fastDateFormat.format((Instant.now() + Duration.ofDays(1)).toDate())}" 
    payments="1" 
    offset="${(offset * 1000) + 1}"/>
</request>""")
            def response = xmlSlurper.parseText(httpResponse.body)
            if (response.result.@count.toInteger() != 0) {
                reconciliationRecordRepository.saveAll(response.result.payment.collect { convertToReconciliationRecord(it) })
                if (response.result.@count.toInteger() == 1000) {
                    offset++
                } else {
                    break
                }
            } else {
                break
            }
        }
    }

    ReconciliationRecord convertToReconciliationRecord(def xmlRecord) {
        return new ReconciliationRecord(
                crmId: xmlRecord.@id,
                dateLocal: fastDateFormat.parse(xmlRecord.@dateLocal.toString()),
                dateUtc: fastDateFormat.parse(xmlRecord.@dateLocal.toString()).toInstant().toDate(),
                state: xmlRecord.@state,
                substate: xmlRecord.@substate,
                code: xmlRecord.@code,
                crmFinal: xmlRecord.@final,
                trans: xmlRecord.@trans,
                sum: xmlRecord.@sum.toBigDecimal() / 100,
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
                date: reconciliationRecord.dateLocal,
                amount: reconciliationRecord.sum,
                currency: "USD"
        )
    }

    Set<HistoryRecord> getHistory(String phone, Optional<String> tranCount) {
        downloadHistory()
        if (!phone.startsWith("1")) {
            throw new OrbitException("Only USA phone country code is supported at the moment")
        }
        String account = phone.substring(1)
        List<ReconciliationRecord> reconciliationRecordList = reconciliationRecordRepository.findByAccount(account)
        if (tranCount.present) {
            return reconciliationRecordList.collect { convertToHistoryRecord(it) }[0..new Integer(tranCount.get()) - 1].reverse()
        } else {
            return reconciliationRecordList.collect { convertToHistoryRecord(it) }.reverse()
        }
    }

}
