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
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public FriendshipServiceImpl(DomainEventPublisher domainEventPublisher,
                                 FriendshipRepository friendshipRepository) {
        this.domainEventPublisher = domainEventPublisher;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public List<UserDetailsDto> getFriends(UserDetailsDto UserDetailsDto) {
        return getAllFriendshipsWithStatus(UserDetailsDto, FriendshipStatus.ACCEPTED);
    }

    @Override
    public List<UserDetailsDto> getAllFriendshipsWithStatus(UserDetailsDto UserDetailsDto, FriendshipStatus status) {
        User user = userService.findUserById(UserDetailsDto.getId());
        List<User> friends = findFriends(user.getId(), status);

        return convertUserListToDto(friends);
    }

    @Override
    public List<UserDetailsDto> getFriendsOfWithStatus(UserDetailsDto UserDetailsDto, FriendshipStatus status) {
        User user = userService.findUserById(UserDetailsDto.getId());

        return convertUserListToDto(userRepository.findFriendsOfWithStatus(user.getId(), status));
    }

    private List<UserDetailsDto> convertUserListToDto(List<User> users) {
        return users.stream()
                .map(user -> modelMapper.map(user, UserDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findFriends(Long id, FriendshipStatus status) {
        List<User> friendsDtoStream = userRepository.findFriendsFromWithStatus(id, status);
        List<User> friendsOfDtoStream = userRepository.findFriendsOfWithStatus(id, status);

        return concatFriendsCollections(friendsDtoStream, friendsOfDtoStream)
                .collect(Collectors.toList());
    }

    @Override
    public Set<User> findFriendsAtUniversity(User user) {
        Set<User> friends = userRepository.findFriendsFromWithStatusAndUniversity(user.getId(),
                                                                                  FriendshipStatus.ACCEPTED,
                                                                                  user.getUniversity());
        Set<User> friendsOf = userRepository.findFriendsOfWithStatusAndUniversity(user.getId(),
                                                                                  FriendshipStatus.ACCEPTED,
                                                                                  user.getUniversity());
        return concatFriendsCollections(friends, friendsOf)
                .collect(Collectors.toSet());
    }

    private Stream<User> concatFriendsCollections(Collection<User> friends, Collection<User> friendsOf) {
        return Stream.concat(friends.stream(), friends.stream());
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
                friendshipDto.getRequesterId(), friendshipDto.getAddresseeId())
                .orElseThrow(() -> CWMException.getException(EntityType.FRIENDSHIP,
                                                             ExceptionType.ENTITY_NOT_FOUND,
                                                             friendshipDto.getRequesterId().toString(),
                                                             friendshipDto.getAddresseeId().toString()));

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
                                        friendshipDto.getRequesterId().toString(),
                                        friendshipDto.getAddresseeId().toString());
    }

    private Friendship getFriendshipToUpdate(FriendshipDto friendshipDto) {
        Long requesterId = friendshipDto.getRequesterId();
        Long addresseeId = friendshipDto.getAddresseeId();

        return friendshipRepository.
                findByIdRequesterIdAndIdAddresseeId(requesterId, addresseeId)
                    .orElseThrow(() -> CWMException.getException(EntityType.FRIENDSHIP,
                                                                 ExceptionType.ENTITY_NOT_FOUND,
                                                                 friendshipDto.getRequesterId().toString(),
                                                                 friendshipDto.getAddresseeId().toString()));
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
