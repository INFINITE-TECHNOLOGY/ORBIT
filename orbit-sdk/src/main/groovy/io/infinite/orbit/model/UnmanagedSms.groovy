package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, includeSuper = true)
class UnmanagedSms extends UnmanagedMessage {

    @JsonProperty(required = true)
    String telephone

}
