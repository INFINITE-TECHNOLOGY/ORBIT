package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

class FormularEmail extends FormularBase {

    @JsonProperty(required = true)
    String from

    @JsonProperty(required = true)
    String to

    @JsonProperty(required = true)
    TemplateSelector subjectSelector

}
