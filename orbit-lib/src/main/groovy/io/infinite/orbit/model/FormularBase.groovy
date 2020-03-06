package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

abstract class FormularBase {

    Map<String, String> fieldValues

    @JsonProperty(required = true)
    TemplateSelector bodySelector

}
