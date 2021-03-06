package io.infinite.orbit.entities

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table
@ToString(includeNames = true, includeFields = true, excludes = ["otp"])
class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id

    UUID guid = UUID.randomUUID()

    String otp

    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    Date creationDate

    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    Date expiryDate

    Integer attemptsCount = 0

    Integer maxAttemptsCount

    Integer durationSeconds

    Otp strip() {
        otp = null
        return this
    }

}
