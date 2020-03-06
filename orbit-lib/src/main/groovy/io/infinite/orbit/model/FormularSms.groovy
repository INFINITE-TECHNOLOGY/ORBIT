package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

class FormularSms extends FormularBase {

    @JsonProperty(required = true)
    String telephone

}
