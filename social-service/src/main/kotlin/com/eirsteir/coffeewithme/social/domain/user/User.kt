package com.eirsteir.coffeewithme.social.domain.user

import com.eirsteir.coffeewithme.commons.domain.user.UserDetails
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.domain.CreatedUpdatedDateTimeBaseModel
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import com.eirsteir.coffeewithme.social.domain.university.University
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.time.LocalDateTime
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.persistence.*

@Entity
data class User(
    @Id
    val id: Long? = null,
    @Column(unique = true)
    val email: String? = null,
    var nickname: String? = null,
    val name: String? = null,
    val lastLogin: LocalDateTime? = null,
    @ManyToOne
    @JoinColumn(name = "university_id")
    val university: University? = null,
    @JsonIgnore
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "id.requester",
        cascade = [javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE],
        orphanRemoval = true
    )
    @Cascade(
        CascadeType.SAVE_UPDATE
    )
    var friends: Set<Friendship> = setOf()
) : CreatedUpdatedDateTimeBaseModel() {

    fun addFriend(friend: User, status: FriendshipStatus): Friendship {
        val friendship = Friendship(requester = this, addressee = friend, status = status)
        friends.plus(friendship)
        return friendship
    }

    fun getFriendships() = friends

    @JvmName("getFriends1")
    fun getFriends(): Set<User> =
        friends.stream()
            .filter { it != null }
            .filter(getFriendshipPredicate(FriendshipStatus.ACCEPTED))
            .map(getFriendshipUserFunction())
            .collect(Collectors.toSet())


    private fun getFriendshipUserFunction(): Function<Friendship, User> =
        Function<Friendship, User> { friendship: Friendship ->
            if (friendship.addressee?.id == id)
                return@Function friendship.requester
            friendship.addressee
        }

    fun getFiendsAtUniversity(): Set<User> =
        getFriends()
            .stream()
            .filter {  it.university == university }
            .collect(Collectors.toSet())

    fun removeFriendship(friendship: Friendship?) = friends.minus(friendship)

    companion object {
        private fun getFriendshipPredicate(status: FriendshipStatus): Predicate<Friendship> {
            return Predicate<Friendship> { friendship: Friendship -> friendship.status === status }
        }
    }
}


fun User.toUserDetails() = UserDetails(
    id = this.id!!,
    name = this.name!!,
    nickname = this.nickname!!
)