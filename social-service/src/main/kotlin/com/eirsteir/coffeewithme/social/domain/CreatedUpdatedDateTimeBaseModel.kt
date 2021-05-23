package com.eirsteir.coffeewithme.social.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class CreatedUpdatedDateTimeBaseModel(
    @CreationTimestamp
    val createdDateTime: Date? = null,
    @UpdateTimestamp
    val updatedDateTime: Date? = null,
)