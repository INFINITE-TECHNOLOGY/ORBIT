package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

class TemplatedSms extends TemplatedMessage {

    @JsonProperty(required = true)
    String telephone

}
