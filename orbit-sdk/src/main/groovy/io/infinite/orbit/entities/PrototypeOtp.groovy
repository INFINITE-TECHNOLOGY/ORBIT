package io.infinite.orbit.entities


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

    Integer durationSeconds

    Integer maxAttemptsCount

    Integer length

}
