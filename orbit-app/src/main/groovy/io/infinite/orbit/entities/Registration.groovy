package io.infinite.orbit.entities

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table
@ToString(includeNames = true, includeFields = true, excludes = ["otp"])
class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id

    String namespace

    UUID guid = UUID.randomUUID()

    String phone

    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    Date creationDate

}
