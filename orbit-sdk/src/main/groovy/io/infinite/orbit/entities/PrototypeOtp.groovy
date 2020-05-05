package io.infinite.orbit.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table
@ToString(includeNames = true, includeFields = true)
class PrototypeOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id

    @JsonIgnore
    @Column(unique = true)
    String namespace

    Integer durationSeconds

    Integer maxAttemptsCount

    Integer length

}
