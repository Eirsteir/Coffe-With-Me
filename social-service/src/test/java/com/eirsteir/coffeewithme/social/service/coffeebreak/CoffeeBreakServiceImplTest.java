package com.eirsteir.coffeewithme.social.service.coffeebreak;

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakDetails;
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.coffeebreak.CoffeeBreak;
import com.eirsteir.coffeewithme.social.domain.university.Campus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.CampusRepository;
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;


@Import({ModelMapperConfig.class})
@ExtendWith(SpringExtension.class)
class CoffeeBreakServiceImplTest {

    public static final long REQUESTER_ID = 1L;
    public static final Set<Long> ADDRESSEE_IDS = Set.of(2L, 3L);
    public static final long CAMPUS_ID = 10L;

    private UserDetailsImpl currentUserDetails;
    private User currentUser;
    private Set<User> addressees;
    private UserDetails requesterUserDetails;
    private Set<UserDetails> addresseesUserDetails;
    private Campus campus;
    private CoffeeBreak coffeeBreak;

    @Autowired
    private CoffeeBreakService coffeeBreakService;

    @MockBean
    private CoffeeBreakRepository coffeeBreakRepository;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private UserService userService;

    @MockBean
    private CampusRepository campusRepository;

    @TestConfiguration
    static class CoffeeBreakServiceImplTestContextConfiguration {

        @Bean
        public CoffeeBreakService coffeeBreakService() {
            return new CoffeeBreakServiceImpl();
        }
    }

    @BeforeEach
    void setUp() {
        currentUserDetails = new UserDetailsImpl();
        currentUserDetails.setId(REQUESTER_ID);

        currentUser = User.builder().id(currentUserDetails.getId()).build();
        addressees = ADDRESSEE_IDS.stream()
                .map(id -> User.builder().id(id).build())
                .collect(Collectors.toSet());
        requesterUserDetails = UserDetails.builder().id(REQUESTER_ID).build();
        addresseesUserDetails = ADDRESSEE_IDS.stream()
                .map(id -> UserDetails.builder().id(id).build())
                .collect(Collectors.toSet());

        campus = new Campus();
        campus.setId(10L);

        coffeeBreak = CoffeeBreak.builder()
                .requester(currentUser)
                .addressees(addressees)
                .campus(campus)
                .scheduledTo(LocalTime.now())
                .build();

        when(userService.findUserById(REQUESTER_ID))
                .thenReturn(currentUser);
        when(friendshipService.findFriendsAtUniversity(currentUser))
                .thenReturn(addressees);
        when(campusRepository.findById(CAMPUS_ID))
                .thenReturn(Optional.ofNullable(campus));
        when(coffeeBreakRepository.save(Mockito.any(CoffeeBreak.class)))
                .thenAnswer(i -> i.getArgument(0, CoffeeBreak.class));
    }

    @Test
    void testRegisterCoffeeBreakWhenValid_thenReturnRegisteredCoffeeBreakAddressedToFriendsAtSameUniversity() {
        CoffeeBreakRequest request = CoffeeBreakRequest.builder()
                .campusId(campus.getId())
                .build();

        CoffeeBreakDetails savedCoffeeBreakDetails = coffeeBreakService.registerCoffeeBreak(request, currentUserDetails);

        assertThat(savedCoffeeBreakDetails.getRequester()).isEqualTo(requesterUserDetails);
        assertThat(savedCoffeeBreakDetails.getAddressees()).isEqualTo(addresseesUserDetails);
    }

    @Test
    void testRegisterCoffeeBreakWhenRequesterNotFound_thenThrowException() {
        when(userService.findUserById(REQUESTER_ID))
                .thenThrow(CWMException.EntityNotFoundException.class);

        CoffeeBreakRequest request = CoffeeBreakRequest.builder()
                .build();

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> coffeeBreakService.registerCoffeeBreak(request, currentUserDetails));
    }

    @Test
    void testRegisterCoffeeBreakWhenCampusIsNull_thenRegisterWithCampusIsNull() {
        when(campusRepository.findById(CAMPUS_ID))
                .thenReturn(Optional.empty());

        CoffeeBreakRequest request = CoffeeBreakRequest.builder()
                .build();
        coffeeBreak.setCampus(null);

        CoffeeBreakDetails savedCoffeeBreakDetails = coffeeBreakService.registerCoffeeBreak(request, currentUserDetails);

        assertThat(savedCoffeeBreakDetails.getCampus()).isNull();
    }

    @Test
    void testRegisterCoffeeBreakWhenScheduledToIsNotSet_thenSetToNow() {
        CoffeeBreakRequest request = CoffeeBreakRequest.builder()
                .campusId(campus.getId())
                .build();

        CoffeeBreakDetails savedCoffeeBreakDetails = coffeeBreakService.registerCoffeeBreak(request, currentUserDetails);
        LocalTime now = LocalTime.now();

        assertThat(savedCoffeeBreakDetails.getScheduledTo()).isBefore(now);
    }

    @Test
    void testRegisterCoffeeBreakWhenScheduledToIsIn30Minutes_thenSetTo30MinutesFromNow() {
        LocalTime in5MinutesFromNow = LocalTime.now().plusMinutes(5);
        coffeeBreak.setScheduledTo(in5MinutesFromNow);

        CoffeeBreakRequest request = CoffeeBreakRequest.builder()
                .scheduledToInMinutes(5L)
                .campusId(campus.getId())
                .build();

        CoffeeBreakDetails savedCoffeeBreakDetails = coffeeBreakService.registerCoffeeBreak(request, currentUserDetails);

        assertThat(savedCoffeeBreakDetails.getScheduledTo()).isEqualToIgnoringSeconds(in5MinutesFromNow);
    }
}