package com.eirsteir.coffeewithme.social.service.coffeebreak;

import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.repository.CampusRepository;
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@Import({ModelMapperConfig.class})
@ExtendWith(SpringExtension.class)
class CoffeeBreakServiceImplTest {

    @Autowired
    private CoffeeBreakService coffeeBreakService;

    @MockBean
    private CoffeeBreakRepository coffeeBreakRepository;

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
    }

    @Test
    void testRegisterCoffeeBreakWhenRequesterNotFound_thenThrowException() {
    }

    @Test
    void testRegisterCoffeeBreakWhenOneAddresseeNotFound_thenThrowException() {
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