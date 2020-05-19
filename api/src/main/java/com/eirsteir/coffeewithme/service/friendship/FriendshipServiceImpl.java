package com.eirsteir.coffeewithme.service.friendship;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.FriendRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> getFriends(UserDto userDto) {
        return getAllFriendshipsWithStatus(userDto, FriendshipStatus.ACCEPTED);
    }

    @Override
    public List<UserDto> getAllFriendshipsWithStatus(UserDto userDto, FriendshipStatus status) {
        User user = userService.findUserById(userDto.getId());
        List<User> friends = findFriends(user.getId(), status);

        return convertUserListToDto(friends);
    }

    @Override
    public List<UserDto> getFriendsOfWithStatus(UserDto userDto, FriendshipStatus status) {
        User user = userService.findUserById(userDto.getId());

        return convertUserListToDto(userRepository.findFriendsOfWithStatus(user.getId(), status));
    }

    private List<UserDto> convertUserListToDto(List<User> users) {
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findFriends(Long id, FriendshipStatus status) {
        Stream<User> friendsDtoStream = userRepository.findFriendsFromWithStatus(id, status)
                .stream();

        Stream<User> friendsOfDtoStream = userRepository.findFriendsOfWithStatus(id, status)
                .stream();

        return Stream.concat(friendsDtoStream, friendsOfDtoStream)
                .collect(Collectors.toList());
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

        log.info("[x] Friendship was updated to {}: {}", friendshipDto.getStatus(), friendshipToUpdate);
        return modelMapper.map(updatedFriendship, FriendshipDto.class);
    }

}
