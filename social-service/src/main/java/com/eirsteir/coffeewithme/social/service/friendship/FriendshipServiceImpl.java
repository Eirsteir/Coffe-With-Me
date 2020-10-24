package com.eirsteir.coffeewithme.social.service.friendship;

import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.exception.EntityType;
import com.eirsteir.coffeewithme.commons.exception.ExceptionType;
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private ModelMapper modelMapper;

    public FriendshipServiceImpl(DomainEventPublisher domainEventPublisher,
                                 FriendshipRepository friendshipRepository) {
        this.domainEventPublisher = domainEventPublisher;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public List<FriendshipDto> findFriendships(UserDetailsDto UserDetailsDto) {
        return getAllFriendshipsWithStatus(UserDetailsDto, FriendshipStatus.ACCEPTED);
    }

    @Override
    public List<FriendshipDto> getAllFriendshipsWithStatus(UserDetailsDto userDetailsDto, FriendshipStatus status) {
        List<Friendship> friendships = friendshipRepository.findByUserAndStatus(userDetailsDto.getId(), status);

        return getFriendshipDtos(friendships);
    }

    @Override
    public List<FriendshipDto> findFriendships(Long id, FriendshipStatus status) {
        List<Friendship> friendships = friendshipRepository.findByUserAndStatus(id, status);

        return getFriendshipDtos(friendships);
    }

    private List<FriendshipDto> getFriendshipDtos(List<Friendship> friendships) {
        return friendships.stream()
                .map(friendship -> modelMapper.map(friendship, FriendshipDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getFriendsCount(Long userId) {
        return friendshipRepository.countByUserId(userId);
    }

    @Override
    public List<FriendshipDto> findFriendshipsAtUniversity(User user) {
        List<Friendship> friendships = friendshipRepository.findByUserAndStatusAndUniversity(user.getId(),
                                                                                             FriendshipStatus.ACCEPTED,
                                                                                             user.getUniversity());
        return getFriendshipDtos(friendships);
    }

    @Override
    public FriendshipDto registerFriendship(FriendRequest friendRequest) {
        User requester = modelMapper.map(userService.findUserById(friendRequest.getRequesterId()), User.class);
        User addressee = modelMapper.map(userService.findUserById(friendRequest.getAddresseeId()), User.class);
        FriendshipId id = FriendshipId.builder()
                .requester(requester)
                .addressee(addressee)
                .build();

        if (friendshipExists(id))
            throw CWMException.getException(EntityType.FRIENDSHIP,
                                            ExceptionType.DUPLICATE_ENTITY,
                                            friendRequest.getRequesterId().toString(),
                                            friendRequest.getAddresseeId().toString());

        Friendship friendship = requester.addFriend(addressee, FriendshipStatus.REQUESTED);
        log.info("[x] Registered friendship: {}", friendship);

        UserDetails user = userService.getUserDetails(friendship.getRequester());
        publish(Friendship.createFriendRequest(friendship, user));

        return modelMapper.map(friendship, FriendshipDto.class);
    }

    private boolean friendshipExists(FriendshipId friendshipId) {
        return friendshipRepository.existsById(friendshipId);
    }

    @Override
    public void removeFriendship(FriendshipDto friendshipDto) {
        Friendship friendship = friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                friendshipDto.getRequester().getId(),
                friendshipDto.getAddressee().getId())
                .orElseThrow(() -> CWMException.getException(EntityType.FRIENDSHIP,
                                                             ExceptionType.ENTITY_NOT_FOUND,
                                                             friendshipDto.getRequester().getId().toString(),
                                                             friendshipDto.getAddressee().getId().toString()));

        friendshipRepository.delete(friendship);
        log.info("[x] Removed friendship: {}", friendshipDto);
    }

    @Override
    public FriendshipDto updateFriendship(FriendshipDto friendshipDto) {
        Friendship friendshipToUpdate = getFriendshipToUpdate(friendshipDto);

        if (isValidStatusChange(friendshipToUpdate.getStatus(), friendshipDto.getStatus()))
            return updateFriendship(friendshipDto, friendshipToUpdate);

        throw CWMException.getException(EntityType.FRIENDSHIP,
                                        ExceptionType.INVALID_STATUS_CHANGE,
                                        friendshipDto.getRequester().getId().toString(),
                                        friendshipDto.getAddressee().getId().toString());
    }

    private Friendship getFriendshipToUpdate(FriendshipDto friendshipDto) {
        Long requesterId = friendshipDto.getRequester().getId();
        Long addresseeId = friendshipDto.getAddressee().getId();

        return friendshipRepository.
                findByIdRequesterIdAndIdAddresseeId(requesterId, addresseeId)
                    .orElseThrow(() -> CWMException.getException(EntityType.FRIENDSHIP,
                                                                 ExceptionType.ENTITY_NOT_FOUND,
                                                                 friendshipDto.getRequester().getId().toString(),
                                                                 friendshipDto.getAddressee().getId().toString()));
    }

    private boolean isValidStatusChange(FriendshipStatus oldStatus, FriendshipStatus newStatus) {
        if (oldStatus == FriendshipStatus.REQUESTED)
            return true;

        return oldStatus == FriendshipStatus.ACCEPTED && newStatus == FriendshipStatus.BLOCKED;
    }

    private FriendshipDto updateFriendship(FriendshipDto friendshipDto, Friendship friendshipToUpdate) {
        friendshipToUpdate.setStatus(friendshipDto.getStatus());
        Friendship updatedFriendship = friendshipRepository.save(friendshipToUpdate);

        if (updatedFriendship.getStatus() == FriendshipStatus.ACCEPTED) {
            UserDetails addressee = userService.getUserDetails(updatedFriendship.getAddressee());
            ResultWithEvents<Friendship> friendshipWithEvents = Friendship.createFriendRequestAccepted(
                    friendshipToUpdate, addressee);
            publish(friendshipWithEvents);
        }



        log.info("[x] Friendship was updated to {}: {}", friendshipDto.getStatus(), friendshipToUpdate);
        return modelMapper.map(updatedFriendship, FriendshipDto.class);
    }

    private void publish(ResultWithEvents<Friendship>  friendshipWithEvents) {
        log.info("[x] Publishing {} to {}", friendshipWithEvents, Friendship.class);
        domainEventPublisher.publish(Friendship.class, friendshipWithEvents, friendshipWithEvents.events);
    }

}
