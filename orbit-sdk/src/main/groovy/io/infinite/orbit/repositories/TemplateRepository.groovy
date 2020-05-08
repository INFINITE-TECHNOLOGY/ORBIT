package io.infinite.orbit.repositories


import io.infinite.orbit.entities.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface TemplateRepository extends JpaRepository<Template, Long> {

    @Query("""select t from Template t
    where t.name = :templateName
    and t.templateType = :templateType
    and t.language = :language""")
    Set<Template> matchPriorityOne(
            @Param("templateName") String templateName,
            @Param("templateType") String templateType,
            @Param("language") String language
    )

    @Query("""select t from Template t
    where t.name = :templateName
    and t.templateType = :templateType
    and t.language is null""")
    Set<Template> matchPriorityTwo(
            @Param("templateName") String templateName,
            @Param("templateType") String templateType
    )

}
