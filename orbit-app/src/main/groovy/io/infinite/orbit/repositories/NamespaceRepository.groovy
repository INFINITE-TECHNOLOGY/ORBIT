package io.infinite.orbit.repositories

import io.infinite.orbit.entities.Namespace
import io.infinite.orbit.entities.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface NamespaceRepository extends JpaRepository<Template, Long> {

    @Query("""select t from Namespace n
    join n.templates t
    where n.name = :namespace
    and t.name = :templateName
    and t.templateType = :templateType
    and t.language = :language""")
    Set<Template> matchPriorityOne(
            @Param("templateName") String templateName,
            @Param("namespace") String namespace,
            @Param("templateType") String templateType,
            @Param("language") String language
    )

    @Query("""select t from Namespace n
    join n.templates t
    where n.name = :namespace
    and t.name = :templateName
    and t.templateType = :templateType
    and t.language is null""")
    Set<Template> matchPriorityTwo(
            @Param("templateName") String templateName,
            @Param("namespace") String namespace,
            @Param("templateType") String templateType
    )

    Namespace findByName(String name)

}
