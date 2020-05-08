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
import java.util.Collection;

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

//    public FriendshipDto addFriendship(FriendRequestDto friendRequestDto) {
//
//        if (friendRequestDto.getStatus() == FriendshipStatus.ACCEPTED)
//            return registerFriendship(friendRequestDto.getFrom(), friendRequestDto.getTo());
//
//        throw CWMException.getException(EntityType.FRIENDSHIP,
//                                        ExceptionType.INVALID_STATE,
//                                        friendRequestDto.getFrom().toString(),
//                                        friendRequestDto.getTo().toString(),
//                                        friendRequestDto.getStatus().getStatus());
//    }

//    private FriendshipDto registerFriendship(Long fromId, Long toId) {
//        User fromUser = modelMapper.map(userService.findUserById(fromId), User.class);
//        User toUser = modelMapper.map(userService.findUserById(toId), User.class);
//
//        if (friendshipExists(fromUser, toUser))
//            throw CWMException.getException(FRIEND_REQUEST,
//                                            DUPLICATE_ENTITY,
//                                            fromId.toString(),
//                                            toId.toString());
//
//        UserRelationId userRelationId = UserRelationId.builder()
//                .requester(fromUser)
//                .addressee(toUser)
//                .build();
//        Friendship friendshipModel = friendshipRepository.save(Friendship.builder()
//                                                            .id(userRelationId)
//                                                            .build());
//
//        return modelMapper.map(friendshipModel);
//    }

    private boolean friendshipExists(FriendshipId friendshipId) {
        return friendshipRepository.existsById(friendshipId);
    }

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

    @Override
    public void removeFriendship(FriendshipDto friendshipDto) {

    }

    @Override
    public FriendshipDto acceptFriendship(FriendshipDto friendshipDto) {
        return null;
    }

    @Override
    public FriendshipDto blockFriendShip(FriendshipDto friendshipDto) {
        return null;
    }

    @Override
    public Collection<UserDto> findFriendsOf(UserDto userDto) {
        return null;
    }
}
