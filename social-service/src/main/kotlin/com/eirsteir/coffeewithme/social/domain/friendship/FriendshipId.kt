package com.eirsteir.coffeewithme.social.domain.friendship

import com.eirsteir.coffeewithme.social.domain.user.User
import lombok.Builder
import lombok.Data
import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.ManyToOne

@Embeddable
data class FriendshipId(
    @ManyToOne
    val requester: User? = null,
    @ManyToOne
    val addressee: User? = null
) : Serializable {

    companion object {
        private const val serialVersionUID = 3966996285633364115L
    }
}