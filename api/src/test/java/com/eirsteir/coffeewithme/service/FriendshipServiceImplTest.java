package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.service.util.CWMModelMapper;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.FriendshipRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus.REQUESTED;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
class FriendshipServiceImplTest {

    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";

    private Friendship friendship;
    private FriendshipId friendshipId;
    private User requester;
    private User addressee;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FriendshipService friendshipService;

    @MockBean
    private FriendshipRepository friendshipRepository;

    @MockBean
    private UserService userService;

    @TestConfiguration
    static class FriendshipServiceImplTestContextConfiguration {

        @Bean
        public FriendshipService friendshipService() {
            return new FriendshipServiceImpl();
        }

        @Bean
        public CWMModelMapper modelMapper() {
            return new CWMModelMapper();
        }
    }

    @BeforeEach
    void setUp() {
        requester = entityManager.persistFlushFind(User.builder()
                                                           .email(REQUESTER_USERNAME)
                                                           .build());
        addressee = entityManager.persistFlushFind(User.builder()
                                                           .email(ADDRESSEE_USERNAME)
                                                           .build());

        friendshipId = FriendshipId.builder()
                .requester(requester)
                .addressee(addressee)
                .build();

        friendship = entityManager.persistFlushFind(Friendship.builder()
                                                                       .id(friendshipId)
                                                                       .status(REQUESTED)
                                                                       .build());

        Mockito.when(friendshipRepository.save(Mockito.any(Friendship.class)))
                .thenReturn(friendship);
        Mockito.when(userService.findUserById(requester.getId()))
                .thenReturn(modelMapper.map(requester, UserDto.class));
        Mockito.when(userService.findUserById(addressee.getId()))
                .thenReturn(modelMapper.map(addressee, UserDto.class));
    }

    @Test
    void testRegisterFriendship() {
        FriendshipRequest friendshipRequest = FriendshipRequest.builder()
                .requesterId(requester.getId())
                .addresseeId(addressee.getId())
                .build();

        FriendshipDto savedFriendshipDto = friendshipService.registerFriendship(friendshipRequest);

        assertThat(savedFriendshipDto.getRequester().getId()).isEqualTo(requester.getId());
        assertThat(savedFriendshipDto.getAddressee().getId()).isEqualTo(addressee.getId());
        assertThat(savedFriendshipDto.getStatus()).isEqualTo(REQUESTED);
    }

}