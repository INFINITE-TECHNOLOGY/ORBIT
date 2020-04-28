package io.infinite.orbit.model

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, includeSuper = true)
class ManagedOtpSms extends ManagedOtpSmsHandle {

    String otp

}
