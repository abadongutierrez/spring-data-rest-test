package com.jabaddon.learning.springdatarest.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.CorsRegistry

@SpringBootApplication
class SpringDataRestTestApplication

fun main(args: Array<String>) {
    runApplication<SpringDataRestTestApplication>(*args)
}

@Table("users")
data class User(
    @Id val id: Long?,
    val username: String,
    val password: String,
    val enabled: Boolean

)

@Table("roles")
data class Role(
    @Id val id: Long?,
    val name: String
)

/*
Why not a composite key?
https://github.com/spring-projects/spring-data-relational/issues/574
 */

@Table("user_roles")
data class UserRole(
    @Id val id: Long?,
    val userId: Long,
    val roleId: Long,
)

@RepositoryRestResource
interface UserRepository : CrudRepository<User, Long>, PagingAndSortingRepository<User, Long>

@RepositoryRestResource
interface RoleRepository : CrudRepository<Role, Long>, PagingAndSortingRepository<Role, Long> {
    fun findByNameLike(name: String, p: Pageable): Page<Role>
}

@RepositoryRestResource
interface UserRoleRepository : CrudRepository<UserRole, Long>, PagingAndSortingRepository<UserRole, Long>

@Component
class CustomizedRestMvcConfiguration : RepositoryRestConfigurer {
    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration, cors: CorsRegistry) {
        config.exposeIdsFor(User::class.java, Role::class.java, UserRole::class.java)
        config.repositoryDetectionStrategy = RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED
        config.setBasePath("/api")

        config.exposureConfiguration.forDomainType(User::class.java)
            .withItemExposure { metdata, httpMethods ->
                httpMethods.disable(
                    HttpMethod.POST,
                    HttpMethod.PUT,
                    HttpMethod.DELETE
                )
            }
            .withCollectionExposure { metdata, httpMethods ->
                httpMethods.disable(
                    HttpMethod.POST,
                    HttpMethod.PUT,
                    HttpMethod.DELETE
                )
            }
    }
}