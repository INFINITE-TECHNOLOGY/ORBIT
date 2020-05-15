package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
class HistoryRecord {

    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    Date date

    BigDecimal amount

    String currency

}
