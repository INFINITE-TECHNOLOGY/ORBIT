package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

abstract class TemplatedMessage {

    Map<String, String> templateValues

    @JsonProperty(required = true)
    TemplateSelectionData bodySelectionData

}
