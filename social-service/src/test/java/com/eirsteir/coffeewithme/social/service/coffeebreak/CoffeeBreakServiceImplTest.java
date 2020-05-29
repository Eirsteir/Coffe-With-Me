package com.eirsteir.coffeewithme.social.service.coffeebreak;

import com.eirsteir.coffeewithme.commons.domain.UserDetails;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.coffeebreak.CoffeeBreak;
import com.eirsteir.coffeewithme.social.domain.university.Campus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.CoffeeBreakDto;
import com.eirsteir.coffeewithme.social.repository.CampusRepository;
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
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

    private User requester;
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

    @Autowired
    private ModelMapper modelMapper;

    @TestConfiguration
    static class CoffeeBreakServiceImplTestContextConfiguration {

        @Bean
        public CoffeeBreakService coffeeBreakService() {
            return new CoffeeBreakServiceImpl();
        }
    }

    @BeforeEach
    void setUp() {
        requester = User.builder().id(REQUESTER_ID).build();
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
                .requester(requester)
                .addressees(addressees)
                .campus(campus)
                .scheduledTo(LocalTime.now())
                .build();
    }

    @Test
    void testRegisterCoffeeBreakWhenValid_thenReturnRegisteredCoffeeBreakAddressedToFriendsAtSameUniversity() {
        when(userService.findUserById(REQUESTER_ID))
                .thenReturn(requester);
        when(friendshipService.findFriendsAtUniversity(requester))
                .thenReturn(addressees);
        when(campusRepository.findById(CAMPUS_ID))
                .thenReturn(Optional.empty());
        when(coffeeBreakRepository.save(Mockito.any(CoffeeBreak.class)))
                .thenReturn(coffeeBreak);

        CoffeeBreakRequest request = CoffeeBreakRequest.builder()
                .requesterId(REQUESTER_ID)
                .build();

        CoffeeBreakDto savedCoffeeBreakDto = coffeeBreakService.registerCoffeeBreak(request);
        LocalTime now = LocalTime.now();

        assertThat(savedCoffeeBreakDto.getRequester()).isEqualTo(requesterUserDetails);
        assertThat(savedCoffeeBreakDto.getAddressees()).isEqualTo(addresseesUserDetails);
        assertThat(savedCoffeeBreakDto.getScheduledTo()).isBefore(now);
    }

    @Test
    void testRegisterCoffeeBreakWhenRequesterNotFound_thenThrowException() {
        when(userService.findUserById(REQUESTER_ID))
                .thenThrow(CWMException.EntityNotFoundException.class);

        CoffeeBreakRequest request = CoffeeBreakRequest.builder()
                .requesterId(REQUESTER_ID)
                .build();

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> coffeeBreakService.registerCoffeeBreak(request));
    }

    @Test
    void testRegisterCoffeeBreakWhenCampusIsNull_thenRegisterWithCampusIsNull() {
    }

    @Test
    void testRegisterCoffeeBreakWhenScheduledToIsNotSet_thenSetToNow() {
    }

    @Test
    void testRegisterCoffeeBreakWhenScheduledToIsIn30Minutes_thenSetTo30MinutesFromNow() {
    }
}