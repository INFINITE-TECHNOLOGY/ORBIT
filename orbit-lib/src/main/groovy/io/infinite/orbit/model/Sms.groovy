package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

class Sms {

    @JsonProperty(required = true)
    String telephone

    @JsonProperty(required = true)
    String message

}
