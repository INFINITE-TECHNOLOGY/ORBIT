package io.infinite.orbit.model

import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
abstract class UnmanagedMessage {

    String text

}
