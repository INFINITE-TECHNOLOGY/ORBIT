package io.infinite.orbit.other

enum TemplateTypes {

    EMAIL_SUBJECT("emailSubject"),
    EMAIL_BODY("emailBody"),
    SMS("sms")

    private final String templateType

    TemplateTypes(String iTemplateType) {
        templateType = iTemplateType
    }

    String value() {
        return templateType
    }

}