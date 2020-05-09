package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.web.request.FriendshipRequest;
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

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FriendshipDto registerFriendship(FriendshipRequest friendshipRequest) {
        User requester = modelMapper.map(userService.findUserById(friendshipRequest.getRequesterId()), User.class);
        User addressee = modelMapper.map(userService.findUserById(friendshipRequest.getAddresseeId()), User.class);
        FriendshipId friendshipId = FriendshipId.builder()
                .requester(requester)
                .addressee(addressee)
                .build();

        if (friendshipExists(friendshipId))
            throw CWMException.getException(EntityType.FRIENDSHIP,
                                            ExceptionType.DUPLICATE_ENTITY,
                                            friendshipId.toString());

        Friendship registeredFriendship = friendshipRepository.save(Friendship.builder()
                .id(friendshipId)
                .status(FriendshipStatus.REQUESTED)
                .build());

        log.info("[x] Registered friendship: {}", registeredFriendship);
        return modelMapper.map(registeredFriendship, FriendshipDto.class);
    }

    private boolean friendshipExists(FriendshipId friendshipId) {
        return friendshipRepository.existsById(friendshipId);
    }

    @Override
    public void removeFriendship(FriendshipDto friendshipDto) {
        if (friendshipExists(friendshipDto.getId()))
            friendshipRepository.deleteById(friendshipDto.getId());

        throw CWMException.getException(EntityType.FRIENDSHIP,
                                        ExceptionType.ENTITY_NOT_FOUND,
                                        friendshipDto.getId().toString());
    }

    @Override
    public FriendshipDto acceptFriendship(FriendshipDto friendshipDto) {

        if (friendshipDto.getStatus() == FriendshipStatus.REQUESTED) {
            Friendship friendship = friendshipRepository.findById(friendshipDto.getId())
                    .orElseThrow(() -> CWMException.getException(EntityType.FRIENDSHIP,
                                                                 ExceptionType.ENTITY_NOT_FOUND,
                                                                 friendshipDto.getId().toString()));

            Friendship acceptedFriendship = friendshipRepository.save(friendship.setStatus(FriendshipStatus.ACCEPTED));
            log.info("[x] Friendship was accepted: {}", friendship);
            return modelMapper.map(acceptedFriendship, FriendshipDto.class);
        }

        throw CWMException.getException(EntityType.FRIENDSHIP,
                                        ExceptionType.INVALID_STATUS_CHANGE,
                                        friendshipDto.getId().toString());
    }

    @Override
    public FriendshipDto blockFriendShip(FriendshipDto friendshipDto) {
        return null;
    }

    @Override
    public List<UserDto> findFriendsOf(UserDto userDto) {
        User user = modelMapper.map(userService.findUserById(userDto.getId()), User.class);
        return friendshipRepository.findByIdRequesterOrIdAddresseeAndStatus(user, user, FriendshipStatus.ACCEPTED)
                .stream()
                .map(friend -> modelMapper.map(friend, UserDto.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
