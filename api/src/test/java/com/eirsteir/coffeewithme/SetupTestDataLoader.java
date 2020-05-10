package com.eirsteir.coffeewithme;


import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.role.Role;
import com.eirsteir.coffeewithme.domain.role.RoleType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.repository.RoleRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
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
    private RoleRepository roleRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        userRepository.save(User.builder()
                                    .email(REQUESTER_EMAIL)
                                    .username(REQUESTER_USERNAME)
                                    .build());

        userRepository.save(User.builder()
                                    .email(ADDRESSEE_EMAIL)
                                    .username(ADDRESSEE_USERNAME)
                                    .build());

        userRepository.save(User.builder()
                                    .email(OTHER_USER_EMAIL)
                                    .username(OTHER_USER_USERNAME)
                                    .build());


        User requester = userRepository.findByEmail(REQUESTER_EMAIL)
                .get();
        User addressee = userRepository.findByEmail(ADDRESSEE_EMAIL)
                .get();
        User otherUser = userRepository.findByEmail(OTHER_USER_EMAIL)
                .get();

        FriendshipId friendshipId = FriendshipId.builder()
                .requesterId(requester.getId())
                .addresseeId(addressee.getId())
                .build();

        friendshipRepository.save(Friendship.builder()
                                          .id(friendshipId)
                                          .requester(requester)
                                          .addressee(addressee)
                                          .status(FriendshipStatus.ACCEPTED)
                                          .build());

        FriendshipId requestedFriendshipId = FriendshipId.builder()
                .requesterId(requester.getId())
                .addresseeId(otherUser.getId())
                .build();

        friendshipRepository.save(Friendship.builder()
                                          .id(requestedFriendshipId)
                                          .requester(requester)
                                          .addressee(otherUser)
                                          .status(FriendshipStatus.REQUESTED)
                                          .build());
        alreadySetup = true;
    }

    @Transactional
    void createRoleIfNotFound(RoleType type) {
        Role role = roleRepository.findByType(type);
        if (role == null) {
            role = Role.builder()
                    .type(type)
                    .build();
            log.info("[x] Preloading " + roleRepository.save(role));
        }
    }

}
