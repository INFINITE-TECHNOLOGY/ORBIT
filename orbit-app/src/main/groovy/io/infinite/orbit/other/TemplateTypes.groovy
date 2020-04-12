package io.infinite.orbit.other

enum TemplateTypes {

    SUBJECT("subject"),
    BODY("body")

    private final String templateType

    TemplateTypes(String iTemplateType) {
        templateType = iTemplateType
    }

    String value() {
        return templateType
    }

}