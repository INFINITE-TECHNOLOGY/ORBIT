package io.infinite.orbit.entities

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
@ToString(includeNames = true, includeFields = true)
class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id

    String clientId

    String name

    String language

    String channel

    String environment

    String application

    String text

}
