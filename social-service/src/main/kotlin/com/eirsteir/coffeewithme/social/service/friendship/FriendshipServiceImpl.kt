package com.eirsteir.coffeewithme.social.service.friendship

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto
import com.eirsteir.coffeewithme.commons.exception.APIException
import com.eirsteir.coffeewithme.commons.exception.EntityType
import com.eirsteir.coffeewithme.commons.exception.ExceptionType
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.domain.user.toUserDetails
import com.eirsteir.coffeewithme.social.dto.FriendshipDto
import com.eirsteir.coffeewithme.social.repository.FriendshipRepository
import com.eirsteir.coffeewithme.social.service.user.UserService
import com.eirsteir.coffeewithme.social.web.request.FriendRequest
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.publisher.ResultWithEvents
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import javax.transaction.Transactional

private val logger = KotlinLogging.logger {  }

@Service
@Transactional
class FriendshipServiceImpl(
    private val domainEventPublisher: DomainEventPublisher,
    private val userService: UserService,
    private val friendshipRepository: FriendshipRepository,
    private val modelMapper: ModelMapper,
) : FriendshipService {

    override fun findFriendshipsOf(user: UserDetailsDto) =
        findAllFriendshipsWithStatus(user, FriendshipStatus.ACCEPTED)

    override fun findAllFriendshipsWithStatus(user: UserDetailsDto, status: FriendshipStatus) =
        friendshipRepository.findByUserAndStatus(user.id!!, status)
            .map { modelMapper.map(it, FriendshipDto::class.java) }

    override fun findFriendshipsOf(id: Long, status: FriendshipStatus) =
        friendshipRepository.findByUserAndStatus(id, status)
            .map { modelMapper.map(it, FriendshipDto::class.java) }

    override fun getFriendsCount(userId: Long)=
        friendshipRepository.countByUserId(userId)

    override fun findFriendshipsAtUniversity(user: User) =
        friendshipRepository.findByUserAndStatusAndUniversity(
            user.id!!, FriendshipStatus.ACCEPTED, user.university!!
        ).map { modelMapper.map(it, FriendshipDto::class.java) }


    override fun registerFriendship(friendRequest: FriendRequest): FriendshipDto {
        val requester = userService.findUserById(friendRequest.requesterId)
        val addressee = userService.findUserById(friendRequest.addresseeId)

        val id = FriendshipId(requester = requester, addressee = addressee)
        if (friendshipExists(id))
            throw APIException.of(
                EntityType.FRIENDSHIP,
                ExceptionType.DUPLICATE_ENTITY,
                friendRequest.requesterId.toString(),
                friendRequest.addresseeId.toString()
        )

        val friendship = requester.addFriend(addressee, FriendshipStatus.REQUESTED)
        logger.info("[x] Registered friendship: {}", friendship)

        val user = friendship.requester!!.toUserDetails()
        publish(Friendship.createFriendRequest(friendship, user))

        return modelMapper.map(friendship, FriendshipDto::class.java)
    }

    override fun friendshipExists(friendshipId: FriendshipId) =
        friendshipRepository.existsById(friendshipId)

    override fun removeFriendship(friendshipDto: FriendshipDto) {
        val friendship = friendshipRepository
            .findByIdRequesterIdAndIdAddresseeId(
                friendshipDto.requester.id!!, friendshipDto.addressee.id!!
            )
            .orElseThrow {
                APIException.of(
                    EntityType.FRIENDSHIP, ExceptionType.ENTITY_NOT_FOUND,
                    friendshipDto.requester.id.toString(),
                    friendshipDto.addressee.id.toString()
                )
            }
        friendshipRepository.delete(friendship)
        logger.info("[x] Removed friendship: {}", friendshipDto)
    }

    override fun updateFriendship(friendshipDto: FriendshipDto): FriendshipDto {
        val friendshipToUpdate: Friendship = findFriendship(friendshipDto)
        if (isValidStatusChange(friendshipToUpdate.status!!, friendshipDto.status!!)) return updateFriendship(
            friendshipDto,
            friendshipToUpdate
        )
        throw APIException.of(
            EntityType.FRIENDSHIP, ExceptionType.INVALID_STATUS_CHANGE,
            friendshipDto.requester.id.toString(),
            friendshipDto.addressee.id.toString()
        )
    }

    private fun findFriendship(friendshipDto: FriendshipDto): Friendship {
        val requesterId = friendshipDto.requester.id!!
        val addresseeId = friendshipDto.addressee.id!!
        return friendshipRepository
            .findByIdRequesterIdAndIdAddresseeId(requesterId, addresseeId)
            .orElseThrow {
                APIException.of(
                    EntityType.FRIENDSHIP, ExceptionType.ENTITY_NOT_FOUND,
                    friendshipDto.requester.id.toString(),
                    friendshipDto.addressee.id.toString()
                )
            }
    }

    private fun isValidStatusChange(oldStatus: FriendshipStatus, newStatus: FriendshipStatus) =
        oldStatus === FriendshipStatus.REQUESTED
                || (oldStatus === FriendshipStatus.ACCEPTED
                && newStatus === FriendshipStatus.BLOCKED)

    private fun updateFriendship(
        friendshipDto: FriendshipDto, friendshipToUpdate: Friendship
    ): FriendshipDto {
        friendshipToUpdate.status = friendshipDto.status
        val updatedFriendship = friendshipRepository.save(friendshipToUpdate)

        if (updatedFriendship.status === FriendshipStatus.ACCEPTED) {
            val addressee = updatedFriendship.addressee!!.toUserDetails()
            val friendshipWithEvents = Friendship.createFriendRequestAccepted(friendshipToUpdate, addressee)
            publish(friendshipWithEvents)
        }

        logger.info("[x] Friendship was updated to ${friendshipDto.status}: $friendshipToUpdate")
        return modelMapper.map(updatedFriendship, FriendshipDto::class.java)
    }

    private fun publish(friendshipWithEvents: ResultWithEvents<Friendship>) {
        logger.info("[x] Publishing {} to {}", friendshipWithEvents, Friendship::class.java)
        domainEventPublisher.publish(
            Friendship::class.java, friendshipWithEvents, friendshipWithEvents.events
        )
    }

}