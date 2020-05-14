package io.infinite.orbit.entities


import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table
@ToString(includeNames = true, includeFields = true)
class ReconciliationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id

    Date downloadDate

    String crmId
    Date date
    String state
    String substate
    String code
    String crmFinal
    String trans
    String sum
    String service
    String market
    String dealer
    String branch
    String id_point
    String account
    String tender
    String payment_type

}
