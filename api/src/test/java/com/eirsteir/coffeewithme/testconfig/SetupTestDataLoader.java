package com.eirsteir.coffeewithme.testconfig;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SetupTestDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final String REQUESTER_EMAIL = "requester@test.com";
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";
    public static final String OTHER_USER_USERNAME = "other-user";

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        User requester = User.builder()
                .email(REQUESTER_EMAIL)
                .username(REQUESTER_USERNAME)
                .build();

        User addressee = User.builder()
                .email(ADDRESSEE_EMAIL)
                .username(ADDRESSEE_USERNAME)
                .build();

        User otherUser = User.builder()
                .email(OTHER_USER_EMAIL)
                .username(OTHER_USER_USERNAME)
                .build();

        userRepository.save(addressee);
        userRepository.save(otherUser);

        requester.addFriend(addressee, FriendshipStatus.ACCEPTED);
        requester.addFriend(otherUser, FriendshipStatus.REQUESTED);
        userRepository.save(requester);

        alreadySetup = true;
    }

}
