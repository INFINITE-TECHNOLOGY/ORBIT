package io.infinite.orbit.repositories

import io.infinite.orbit.entities.ReconciliationRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface ReconciliationRecordRepository extends JpaRepository<ReconciliationRecord, Long> {

    @Query("""select max(r.downloadDate) from ReconciliationRecord r""")
    Optional<Date> lastDownloadDate()

    @Query("""select r from ReconciliationRecord r where r.account = :account order by r.dateUtc desc""")
    List<ReconciliationRecord> findByAccount(@Param("account") String account)

}
