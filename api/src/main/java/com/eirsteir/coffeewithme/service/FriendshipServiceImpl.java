package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.UserRelationId;
import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.service.util.CWMModelMapper;
import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.FriendshipRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Collection;

import static com.eirsteir.coffeewithme.exception.EntityType.FRIEND_REQUEST;
import static com.eirsteir.coffeewithme.exception.ExceptionType.DUPLICATE_ENTITY;

@Slf4j
@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private CWMModelMapper modelMapper;

    public FriendshipDto addFriendship(FriendRequestDto friendRequestDto) {

        if (friendRequestDto.getStatus() == FriendshipStatus.ACCEPTED)
            return registerFriendship(friendRequestDto.getFrom(), friendRequestDto.getTo());

        throw CWMException.getException(EntityType.FRIENDSHIP,
                                        ExceptionType.INVALID_STATE,
                                        friendRequestDto.getFrom().toString(),
                                        friendRequestDto.getTo().toString(),
                                        friendRequestDto.getStatus().getStatus());
    }

    private FriendshipDto registerFriendship(Long fromId, Long toId) {
        User fromUser = modelMapper.map(userService.findUserById(fromId), User.class);
        User toUser = modelMapper.map(userService.findUserById(toId), User.class);

        if (friendshipExists(fromUser, toUser))
            throw CWMException.getException(FRIEND_REQUEST,
                                            DUPLICATE_ENTITY,
                                            fromId.toString(),
                                            toId.toString());

        UserRelationId userRelationId = UserRelationId.builder()
                .requester(fromUser)
                .addressee(toUser)
                .build();
        Friendship friendshipModel = friendshipRepository.save(Friendship.builder()
                                                            .id(userRelationId)
                                                            .build());

        return modelMapper.map(friendshipModel);
    }

    private boolean friendshipExists(User from, User to) {
        return true;
    }

    @Override
    public FriendshipDto registerFriendship(FriendshipRequest friendshipRequest) {
        return null;
    }

    @Override
    public void removeFriendship(FriendRequestDto friendRequestDto) {

    }

    @Override
    public FriendshipDto acceptFriendship(FriendshipDto friendshipDto) {
        return null;
    }

    @Override
    public FriendRequestDto blockFriendShip(FriendshipDto friendshipDto) {
        return null;
    }

    @Override
    public Collection<UserDto> findFriendsOf(UserDto userDto) {
        return null;
    }
}
