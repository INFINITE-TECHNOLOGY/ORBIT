package io.infinite.orbit.entities

import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table
@ToString(includeNames = true, includeFields = true)
class Namespace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id

    @Column(unique=true)
    String name

    @OneToOne(fetch = FetchType.EAGER)
    PrototypeOtp prototypeOtp

    @OneToMany(fetch = FetchType.EAGER)
    Set<Template> templates = new HashSet<Template>()

}
