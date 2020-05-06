package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.request.FriendRequest;
import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.repository.FriendRequestRepository;
import com.eirsteir.coffeewithme.service.util.CWMModelMapper;
import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus.*;
import static com.eirsteir.coffeewithme.exception.EntityType.FRIEND_REQUEST;
import static com.eirsteir.coffeewithme.exception.ExceptionType.ENTITY_NOT_FOUND;
import static com.eirsteir.coffeewithme.exception.ExceptionType.INVALID_STATE_CHANGE;

@Slf4j
@Service
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private CWMModelMapper modelMapper;

    @Override
    public FriendRequestDto addFriendRequest(FriendRequestDto friendRequestDto) {
        User fromUser = modelMapper.map(userService.findUserById(friendRequestDto.getFrom()), User.class);
        User toUser = modelMapper.map(userService.findUserById(friendRequestDto.getTo()), User.class);

        if (friendRequestExists(fromUser, toUser))
            return updateExistingFriendRequest(friendRequestDto, fromUser, toUser);


        FriendRequest friendRequest = FriendRequest.builder()
                .from(fromUser)
                .to(toUser)
                .status(PENDING)
                .build();

        log.info("[x] Added friend request: {}", friendRequest);
        return modelMapper.map(friendRequestRepository.save(friendRequest));
    }

    private FriendRequestDto updateExistingFriendRequest(FriendRequestDto friendRequestDto,
                                                         User fromUser,
                                                         User toUser) {

        FriendRequest friendRequest = friendRequestRepository.findByFromAndTo(fromUser, toUser)
                .orElseThrow(() -> CWMException.getException(FRIEND_REQUEST,
                                                             ENTITY_NOT_FOUND,
                                                             friendRequestDto.getFrom()
                                                                     .toString(),
                                                             friendRequestDto.getTo()
                                                                     .toString()));

        return updateFriendRequest(modelMapper.map(friendRequest), PENDING);
    }

    private boolean friendRequestExists(User from, User to) {
        return friendRequestRepository.existsByFromAndTo(from, to);
    }

    @Override
    public FriendRequestDto acceptFriendRequest(FriendRequestDto friendRequestDto) {
        return updateFriendRequest(friendRequestDto, ACCEPTED);
    }

    @Override
    public FriendRequestDto rejectFriendRequest(FriendRequestDto friendRequestDto) {
        return updateFriendRequest(friendRequestDto, REJECTED);
    }

    @Override
    public FriendRequestDto cancelFriendRequest(FriendRequestDto friendRequestDto) {
        return updateFriendRequest(friendRequestDto, CANCELLED);
    }

    private FriendRequestDto updateFriendRequest(FriendRequestDto friendRequestDto, FriendRequestStatus status) {
        FriendRequest friendRequest = findFriendRequest(friendRequestDto);

        if (friendRequest.getStatus() == PENDING || status == PENDING) {
            friendRequest.setStatus(status);
            log.info("[x] Friend request {} was updated with new status {}", friendRequestDto, status);
            return modelMapper.map(friendRequestRepository.save(friendRequest));
        }

        throw CWMException.getException(FRIEND_REQUEST,
                                        INVALID_STATE_CHANGE,
                                        friendRequestDto.getFrom().toString(),
                                        friendRequestDto.getTo().toString(),
                                        status.getStatus());
    }

    private FriendRequest findFriendRequest(FriendRequestDto friendRequestDto) {
        return friendRequestRepository.findById(friendRequestDto.getId())
                .orElseThrow(() -> CWMException.getException(FRIEND_REQUEST,
                                                             ENTITY_NOT_FOUND,
                                                             friendRequestDto.getFrom().toString(),
                                                             friendRequestDto.getTo().toString()));
    }

}
