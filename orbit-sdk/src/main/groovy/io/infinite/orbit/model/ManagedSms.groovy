package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, includeSuper = true)
class ManagedSms extends ManagedMessage {

    @JsonProperty(required = true)
    String telephone

}
