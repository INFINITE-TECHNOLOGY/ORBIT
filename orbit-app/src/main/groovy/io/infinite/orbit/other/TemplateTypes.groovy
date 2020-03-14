package io.infinite.orbit.other

enum TemplateTypes {

    SUBJECT("subject"),
    TEXT("text")

    private final String templateType

    TemplateTypes(String iTemplateType) {
        templateType = iTemplateType
    }

    String value() {
        return templateType
    }

}