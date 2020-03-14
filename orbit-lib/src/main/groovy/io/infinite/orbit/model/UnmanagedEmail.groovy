package io.infinite.orbit.model

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, includeSuper = true)
class UnmanagedEmail extends UnmanagedMessage {

    String to

    String subject

}
