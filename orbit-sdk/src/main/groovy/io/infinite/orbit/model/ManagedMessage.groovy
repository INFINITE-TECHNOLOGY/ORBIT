package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
abstract class ManagedMessage {

    Map<String, String> templateValues

    @JsonProperty(required = true)
    TemplateSelectionData templateSelectionData

}
