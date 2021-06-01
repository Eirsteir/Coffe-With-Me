package com.eirsteir.coffeewithme.social.domain.university

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import lombok.ToString
import javax.persistence.*

@Entity
data class Campus(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val name: String? = null,
    @ManyToOne
    @JoinColumn(name = "university_id")
    @ToString.Exclude
    @JsonIgnore
    var university: University? = null,
)