package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

class Email {

    @JsonProperty(required = true)
    String from

    @JsonProperty(required = true)
    String to

    @JsonProperty(required = true)
    String text

    @JsonProperty(required = true)
    String subject

    @JsonProperty(required = true)
    String message

}
