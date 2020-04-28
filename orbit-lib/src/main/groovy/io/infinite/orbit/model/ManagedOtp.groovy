package io.infinite.orbit.model

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, includeSuper = true, excludes = ["otp"])
class ManagedOtp extends ManagedOtpHandle {

    String otp

}
