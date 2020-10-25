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
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipServiceImpl;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import com.eirsteir.coffeewithme.testconfig.BaseUnitTestClass;
import com.eirsteir.coffeewithme.testconfig.EventuateTestConfig;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@Import({ModelMapperConfig.class, BaseUnitTestClass.class, EventuateTestConfig.class})
@ExtendWith(SpringExtension.class)
class FriendshipServiceImplTest extends BaseUnitTestClass {

    private static final String REQUESTER_NICKNAME = "requester";
    private static final String ADDRESSEE_NICKNAME = "addressee";

    private Friendship friendshipRequested;
    private Friendship acceptedFriendship;
    private FriendshipId friendshipId;
    private User requester;
    private User addressee;
    private FriendRequest friendRequest;
    private UserDetailsDto requesterDto;

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
        @Autowired
        private DomainEventPublisher domainEventPublisher;

        @Autowired
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

        acceptedFriendship = Friendship.builder()
                .requester(requester)
                .addressee(addressee)
                .status(ACCEPTED)
                .build();

        friendRequest = FriendRequest.builder()
                .requesterId(requester.getId())
                .addresseeId(addressee.getId())
                .build();

        requesterDto = modelMapper.map(requester, UserDetailsDto.class);

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

        assertThat(savedFriendshipDto.getRequester().getId()).isEqualTo(requester.getId());
        assertThat(savedFriendshipDto.getAddressee().getId()).isEqualTo(addressee.getId());
        assertThat(savedFriendshipDto.getStatus()).isEqualTo(REQUESTED);
    }

    @Test
    void testRegisterFriendshipWhenDuplicate() {
        when(friendshipRepository.existsById(friendshipId))
                .thenReturn(true);

        assertThatExceptionOfType(CWMException.DuplicateEntityException.class)
                .isThrownBy(() -> friendshipService.registerFriendship(friendRequest));
    }

    @Test
    void testRegisterFriendshipWhenFriendshipIdReversedIsDuplicate() {
        when(friendshipRepository.existsById(friendshipId))
                .thenReturn(true);

        assertThatExceptionOfType(CWMException.DuplicateEntityException.class)
                .isThrownBy(() -> friendshipService.registerFriendship(friendRequest));
    }

    @Test
    void testRemoveFriendshipWhenNotFound() {
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.empty());

        FriendshipDto friendshipDto = modelMapper.map(friendshipRequested, FriendshipDto.class);

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> friendshipService.removeFriendship(friendshipDto));
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

        FriendshipDto friendshipDto = modelMapper.map(friendshipRequested, FriendshipDto.class);

        friendshipDto.setStatus(newStatus);
        FriendshipDto acceptedFriendship = friendshipService.updateFriendship(friendshipDto);

        assertThat(acceptedFriendship.getStatus()).isEqualTo(newStatus);
    }

    @Test
    void testUpdateFriendshipFromAcceptedToBlocked() {
        Friendship friendshipAccepted = friendshipRequested.setStatus(ACCEPTED);
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), addressee.getId()))
                .thenReturn(Optional.of(friendshipAccepted));

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requester(modelMapper.map(friendshipAccepted.getRequester(), UserDetailsDto.class))
                .addressee(modelMapper.map(friendshipAccepted.getAddressee(), UserDetailsDto.class))
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
                .thenReturn(Optional.of(friendshipAccepted));

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requester(modelMapper.map(friendshipAccepted.getRequester(), UserDetailsDto.class))
                .addressee(modelMapper.map(friendshipAccepted.getAddressee(), UserDetailsDto.class))
                .status(friendshipAccepted.getStatus())
                .build();
        friendshipDto.setStatus(ACCEPTED);

        assertThatExceptionOfType(CWMException.InvalidStatusChangeException.class)
                .isThrownBy(() -> friendshipService.updateFriendship(friendshipDto));
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
                .thenReturn(Optional.of(friendshipAccepted));

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requester(modelMapper.map(friendshipAccepted.getRequester(), UserDetailsDto.class))
                .addressee(modelMapper.map(friendshipAccepted.getAddressee(), UserDetailsDto.class))
                .status(friendshipAccepted.getStatus())
                .build();

        friendshipDto.setStatus(newStatus);

        assertThatExceptionOfType(CWMException.InvalidStatusChangeException.class)
                .isThrownBy(() -> friendshipService.updateFriendship(friendshipDto));
    }

    @Test
    void testUpdateFriendshipWhenNotFound() {
        when(friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), addressee.getId()))
                .thenReturn(Optional.empty());

        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requester(modelMapper.map(friendshipRequested.getRequester(), UserDetailsDto.class))
                .addressee(modelMapper.map(friendshipRequested.getAddressee(), UserDetailsDto.class))
                .status(friendshipRequested.getStatus())
                .build();

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> friendshipService.updateFriendship(friendshipDto));
    }

    @Test
    void testFindFriendshipsOfWhenUserNotFound() {
        when(userService.findUserById(requester.getId()))
                .thenThrow(CWMException.EntityNotFoundException.class);

        List<FriendshipDto> friendshipsFound = friendshipService.findFriendshipsOf(requesterDto);

        assertThat(friendshipsFound).isEmpty();
    }

    @Test
    void testFindFriendsWhenUserHasFriendshipsReturnsFriendshipDtosOfOnlyAcceptedFriendships() {
        Friendship acceptedFriendship = Friendship.builder()
                .requester(requester)
                .addressee(addressee)
                .status(ACCEPTED)
                .build();

        when(friendshipRepository.findByUserAndStatus(requester.getId(), ACCEPTED))
                .thenReturn(List.of(acceptedFriendship));

        List<FriendshipDto> friendsFound = friendshipService.findFriendshipsOf(requesterDto);

        assertThat(friendsFound).hasSize(1);
    }

    @Test
    void testGetFriendsWhenUserHasNoFriendshipsReturnsEmptyList() {
        when(friendshipRepository.findByUserAndStatus(requester.getId(), ACCEPTED))
                .thenReturn(new ArrayList<>());

        List<FriendshipDto> friendsFound = friendshipService.findFriendshipsOf(requesterDto);

        assertThat(friendsFound).isEmpty();
    }

    @Test
    void testFindFriendshipsWithStatusRequestedReturnsFriendshipsWithStatusRequested() {
        when(friendshipRepository.findByUserAndStatus(requester.getId(), ACCEPTED))
                .thenReturn(List.of(acceptedFriendship));

        List<FriendshipDto> friendsFound = friendshipService.findFriendshipsOf(requesterDto);

        assertThat(friendsFound).hasSize(1);
    }

}