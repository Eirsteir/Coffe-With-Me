package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.request.FriendRequest;
import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.FriendRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createFriendRequestFrom(String from, Long to) {
        User fromUser = modelMapper.map(userService.findUserByEmail(from), User.class);
        User toUser = modelMapper.map(userService.findUserById(to), User.class);

        FriendRequest friendRequest = FriendRequest.builder()
                .from(fromUser)
                .to(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);

        log.info("[x] Added friend request: {}", friendRequest);
    }
}
