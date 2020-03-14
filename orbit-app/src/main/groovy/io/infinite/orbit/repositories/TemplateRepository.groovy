package io.infinite.orbit.repositories

import io.infinite.orbit.entities.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface TemplateRepository extends JpaRepository<Template, Long> {

    Set<Template> findByNameAndPartnerIdAndType(
            @Param("name") String name,
            @Param("partnerId") String partnerId,
            @Param("templateType") String templateType
    )

}
