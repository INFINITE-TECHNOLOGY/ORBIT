package io.infinite.orbit.model

import com.fasterxml.jackson.annotation.JsonProperty

class TemplatedEmail extends TemplatedMessage {

    @JsonProperty(required = true)
    String from

    @JsonProperty(required = true)
    String to

    @JsonProperty(required = true)
    TemplateSelectionData subjectSelectionData

}
