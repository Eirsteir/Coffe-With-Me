package com.eirsteir.coffeewithme.social.service;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipServiceImpl;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@Import({ ModelMapperConfig.class})
@TestPropertySource("classpath:exception.properties")
@ExtendWith(SpringExtension.class)
class FriendshipServiceImplTest {

    private static final String REQUESTER_NICKNAME = "requester";
    private static final String ADDRESSEE_NICKNAME = "addressee";

    private Friendship friendshipRequested;
    private FriendshipId friendshipId;
    private User requester;
    private User addressee;
    private FriendRequest friendRequest;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FriendshipService friendshipService;

    @MockBean
    private FriendshipRepository friendshipRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;



    @TestConfiguration
    static class FriendshipServiceImplTestContextConfiguration {
        @MockBean
        private DomainEventPublisher domainEventPublisher;

        @MockBean
        private FriendshipRepository friendshipRepository;

        @Bean
        public FriendshipService friendshipService() {
            return new FriendshipServiceImpl(domainEventPublisher, friendshipRepository);
        }
    }

    @BeforeEach
    void setUp() {
        requester = User.builder()
                .id(1L)
               .nickname(REQUESTER_NICKNAME)
               .build();

        addressee = User.builder()
                .id(2L)
               .nickname(ADDRESSEE_NICKNAME)
               .build();

        friendshipId = FriendshipId.builder()
                .requester(requester)
                .addressee(addressee)
                .build();

        friendshipRequested = Friendship.builder()
                .requester(requester)
                .addressee(addressee)
                .status(REQUESTED)
                .build();

        friendRequest = FriendRequest.builder()
                .requesterId(requester.getId())
                .addresseeId(addressee.getId())
                .build();

        when(friendshipRepository.save(Mockito.any(Friendship.class)))
                .thenReturn(friendshipRequested);
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);
        when(userService.findUserById(addressee.getId()))
                .thenReturn(addressee);
    }

    @Test
    void testRegisterFriendship() {
        FriendshipDto savedFriendshipDto = friendshipService.registerFriendship(friendRequest);

        assertThat(savedFriendshipDto.getRequesterId()).isEqualTo(requester.getId());
        assertThat(savedFriendshipDto.getAddresseeId()).isEqualTo(addressee.getId());
        assertThat(savedFriendshipDto.getStatus()).isEqualTo(REQUESTED);
    }

    @Test
    void testRegisterFriendshipWhenDuplicate() {
        when(friendshipRepository.existsById(friendshipId))
                .thenReturn(true);

        assertThatExceptionOfType(CWMException.DuplicateEntityException.class)
                .isThrownBy(() -> friendshipService.registerFriendship(friendRequest))
                .withMessage("Requested friendship with " +
                                     "requesterId=" + friendshipId.getRequester().getId() +
                                     " and addresseeId=" + friendshipId.getAddressee().getId() +
                                     " already exists");
    }

    @Test
    void testRegisterFriendshipWhenFriendshipIdReversedIsDuplicate() {
        when(friendshipRepository.existsById(friendshipId))
                .thenReturn(true);

        assertThatExceptionOfType(CWMException.DuplicateEntityException.class)
                .isThrownBy(() -> friendshipService.registerFriendship(friendRequest))
                .withMessage("Requested friendship with " +
                                     "requesterId=" + requester.getId() +
                                     " and addresseeId=" + addressee.getId() +
                                     " already exists");
    }

    @Test
    void testRemoveFriendshipWhenNotFound() {
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.empty());

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requesterId(friendshipRequested.getRequester().getId())
                .addresseeId(friendshipRequested.getAddressee().getId())
                .status(friendshipRequested.getStatus())
                .build();

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> friendshipService.removeFriendship(friendshipDto))
                .withMessage("Requested friendship with " +
                                     "requesterId=" + friendshipDto.getRequesterId() +
                                     " and addresseeId=" + friendshipDto.getAddresseeId() +
                                     " does not exist");
    }

    static Stream<Arguments> statusChangeFromRequestedProvider() {
        return Stream.of(
                Arguments.of(REQUESTED),
                Arguments.of(ACCEPTED),
                Arguments.of(DECLINED),
                Arguments.of(BLOCKED)
        );
    }

    @ParameterizedTest
    @MethodSource("statusChangeFromRequestedProvider")
    void testUpdateFriendshipFromRequestedStatus(FriendshipStatus newStatus) {
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), addressee.getId()))
                .thenReturn(Optional.ofNullable(friendshipRequested));

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requesterId(friendshipRequested.getRequester().getId())
                .addresseeId(friendshipRequested.getAddressee().getId())
                .status(friendshipRequested.getStatus())
                .build();
        friendshipDto.setStatus(newStatus);
        FriendshipDto acceptedFriendship = friendshipService.updateFriendship(friendshipDto);

        assertThat(acceptedFriendship.getStatus()).isEqualTo(newStatus);
    }

    @Test
    void testUpdateFriendshipFromAcceptedToBlocked() {
        Friendship friendshipAccepted = friendshipRequested.setStatus(ACCEPTED);
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), addressee.getId()))
                .thenReturn(Optional.ofNullable(friendshipAccepted));

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requesterId(friendshipAccepted.getRequester().getId())
                .addresseeId(friendshipAccepted.getAddressee().getId())
                .status(friendshipAccepted.getStatus())
                .build();
        friendshipDto.setStatus(BLOCKED);
        FriendshipDto acceptedFriendship = friendshipService.updateFriendship(friendshipDto);

        assertThat(acceptedFriendship.getStatus()).isEqualTo(BLOCKED);
    }

    @Test
    void testUpdateFriendshipFromAcceptedToAccepted() {
        Friendship friendshipAccepted = friendshipRequested.setStatus(ACCEPTED);
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), addressee.getId()))
                .thenReturn(Optional.ofNullable(friendshipAccepted));

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requesterId(friendshipAccepted.getRequester().getId())
                .addresseeId(friendshipAccepted.getAddressee().getId())
                .status(friendshipAccepted.getStatus())
                .build();
        friendshipDto.setStatus(ACCEPTED);

        assertThatExceptionOfType(CWMException.InvalidStatusChangeException.class)
                .isThrownBy(() -> friendshipService.updateFriendship(friendshipDto))
                .withMessage("Invalid status change attempted for friendship with " +
                                     "requesterId=" + friendshipAccepted.getRequester().getId() +
                                     " and addresseeId=" + friendshipAccepted.getAddressee().getId());
    }

    static Stream<Arguments> invalidStatusChangeFromAcceptedProvider() {
        return Stream.of(
                Arguments.of(REQUESTED),
                Arguments.of(DECLINED)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidStatusChangeFromAcceptedProvider")
    void testUpdateFriendshipFromAcceptedStatusToInvalidStatus(FriendshipStatus newStatus) {
        Friendship friendshipAccepted = friendshipRequested.setStatus(ACCEPTED);
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), addressee.getId()))
                .thenReturn(Optional.ofNullable(friendshipAccepted));

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requesterId(friendshipAccepted.getRequester().getId())
                .addresseeId(friendshipAccepted.getAddressee().getId())
                .status(friendshipAccepted.getStatus())
                .build();
        friendshipDto.setStatus(newStatus);

        assertThatExceptionOfType(CWMException.InvalidStatusChangeException.class)
                .isThrownBy(() -> friendshipService.updateFriendship(friendshipDto))
                .withMessage("Invalid status change attempted for friendship with " +
                                     "requesterId=" + friendshipAccepted.getRequester().getId() +
                                     " and addresseeId=" + friendshipAccepted.getAddressee().getId());
    }

    @Test
    void testUpdateFriendshipWhenNotFound() {
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), addressee.getId()))
                .thenReturn(Optional.empty());

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requesterId(friendshipRequested.getRequester().getId())
                .addresseeId(friendshipRequested.getAddressee().getId())
                .status(friendshipRequested.getStatus())
                .build();

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> friendshipService.updateFriendship(friendshipDto))
                .withMessage("Requested friendship with " +
                                     "requesterId=" + friendshipRequested.getRequester().getId() +
                                     " and addresseeId=" + friendshipRequested.getAddressee().getId() +
                                     " does not exist");
    }

    @Test
    void testGetFriendsWhenUserNotFound() {
        when(userService.findUserById(requester.getId()))
                .thenThrow(CWMException.EntityNotFoundException.class);

        UserDetailsDto requesterDto = modelMapper.map(requester, UserDetailsDto.class);

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> friendshipService.getFriends(requesterDto));
    }

    @Test
    void testGetFriendsWhenUserHasFriendshipsReturnsUserDtosOfAcceptedFriendships() {
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);

        User newFriendAccepted = User.builder()
                .id(100L)
                .build();

        when(userRepository.findFriendsFromWithStatus(requester.getId(), ACCEPTED))
                .thenReturn(List.of(addressee));

        when(userRepository.findFriendsOfWithStatus(requester.getId(), ACCEPTED))
                .thenReturn(List.of(newFriendAccepted));

        UserDetailsDto requesterDto = modelMapper.map(requester, UserDetailsDto.class);
        List<UserDetailsDto> friendsFound = friendshipService.getFriends(requesterDto);
        UserDetailsDto firstFriendDto = modelMapper.map(friendshipRequested.getAddressee(), UserDetailsDto.class);

        assertThat(friendsFound).hasSize(2);
        assertThat(friendsFound).contains(firstFriendDto);
    }

    @Test
    void testGetFriendsWhenUserHasNoFriendshipsReturnsEmptyList() {
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);

        when(userRepository.findFriendsFromWithStatus(requester.getId(), ACCEPTED))
                .thenReturn(new ArrayList<>());

        when(userRepository.findFriendsOfWithStatus(requester.getId(), ACCEPTED))
                .thenReturn(new ArrayList<>());

        UserDetailsDto requesterDto = modelMapper.map(requester, UserDetailsDto.class);
        List<UserDetailsDto> friendsFound = friendshipService.getFriends(requesterDto);

        assertThat(friendsFound).isEmpty();
    }

    @Test
    void testGetFriendshipsWithStatusRequestedReturnsFriendshipsWithStatusRequested() {
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);

        User newFriend = User.builder()
                .id(100L)
                .build();

        when(userRepository.findFriendsFromWithStatus(requester.getId(), REQUESTED))
                .thenReturn(List.of(addressee));

        when(userRepository.findFriendsOfWithStatus(requester.getId(), REQUESTED))
                .thenReturn(List.of(newFriend));

        UserDetailsDto requesterDto = modelMapper.map(requester, UserDetailsDto.class);
        List<UserDetailsDto> friendRequests = friendshipService.getAllFriendshipsWithStatus(requesterDto, REQUESTED);
        UserDetailsDto firstFriendDto = modelMapper.map(friendshipRequested.getAddressee(), UserDetailsDto.class);

        assertThat(friendRequests).hasSize(2);
        assertThat(friendRequests).contains(firstFriendDto);
    }
}