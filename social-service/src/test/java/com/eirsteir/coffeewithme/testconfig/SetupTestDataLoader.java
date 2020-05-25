package com.eirsteir.coffeewithme.testconfig;

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Slf4j
@Component
public class SetupTestDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final String REQUESTER_EMAIL = "requester@test.com";
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";
    public static final String OTHER_USER_USERNAME = "other-user";
    public static final String JOHN_EMAIL = "john@doe.com";
    public static final String TOM_EMAIL = "tom@doe.com";

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        User defaultUser = User.builder()
                .email("user")
                .username("default")
                .build();

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

        User userJohn = User.builder()
                .name("John Doe")
                .email(JOHN_EMAIL)
                .username("johndoe")
                .build();

        User userTom = User.builder()
                .name("Tom Doe")
                .email(TOM_EMAIL)
                .build();

        log.debug("[x] Preloading {}", userRepository.saveAll(Arrays.asList(addressee, otherUser, userJohn, userTom)));

        defaultUser.addFriend(addressee, FriendshipStatus.ACCEPTED);
        log.debug("[x] Preloading {}", userRepository.save(defaultUser));

        requester.addFriend(addressee, FriendshipStatus.ACCEPTED);
        requester.addFriend(otherUser, FriendshipStatus.REQUESTED);
        log.debug("[x] Preloading {}", userRepository.save(requester));


        alreadySetup = true;
    }

}
