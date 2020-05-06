package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.request.FriendRequest;
import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.repository.FriendRequestRepository;
import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FriendRequestDto addFriendRequest(FriendRequestDto friendRequestDto) {
        User from = modelMapper.map(userService.findUserByEmail(friendRequestDto.getFrom()), User.class);
        User to = modelMapper.map(userService.findUserByEmail(friendRequestDto.getTo()), User.class);

        FriendRequest friendRequest = FriendRequest.builder()
                .from(from)
                .to(to)
                .status(PENDING)
                .build();

        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);
        log.info("[x] Added friend request: {}", friendRequest);
        return modelMapper.map(savedFriendRequest, FriendRequestDto.class);
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

    private FriendRequestDto updateFriendRequest(FriendRequestDto friendRequestDto, FriendRequestStatus newStatus) {
        FriendRequest friendRequest = findFriendRequest(friendRequestDto);

        if (friendRequest.getStatus() == PENDING) {
            friendRequest.setStatus(newStatus);
            log.info("[x] Friend request {} was updated with new status {}", friendRequestDto, newStatus);
            return modelMapper.map(friendRequestRepository.save(friendRequest), FriendRequestDto.class);
        }

        throw CWMException.getException(FRIEND_REQUEST,
                                        INVALID_STATE_CHANGE,
                                        friendRequestDto.getFrom(),
                                        friendRequestDto.getTo());
    }

    private FriendRequest findFriendRequest(FriendRequestDto friendRequestDto) {
        return friendRequestRepository.findById(friendRequestDto.getId())
                .orElseThrow(() -> CWMException.getException(FRIEND_REQUEST,
                                                             ENTITY_NOT_FOUND,
                                                             friendRequestDto.getFrom(),
                                                             friendRequestDto.getTo()));
    }

}
