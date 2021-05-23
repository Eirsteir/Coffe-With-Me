package com.eirsteir.coffeewithme.social.domain.university

import com.eirsteir.coffeewithme.social.domain.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import org.hibernate.annotations.Cascade
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class University(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @get:NotNull
    val name: String? = null,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "university")
    @JsonIgnore
    val attendees: Set<User> = setOf(),
    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "university",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE],
        orphanRemoval = true
    )
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    var campuses: Set<Campus> = setOf()
) {

    fun addCampus(campus: Campus): Campus {
        campuses.plus(campus)
        campus.university = this
        return campus
    }
}