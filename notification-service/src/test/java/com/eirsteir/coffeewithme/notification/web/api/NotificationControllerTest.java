package com.eirsteir.coffeewithme.notification.web.api;

import com.eirsteir.coffeewithme.notification.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.notification.service.NotificationService;
import com.eirsteir.coffeewithme.notification.testconfig.MessageTemplateUtilTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;



@Import({MessageTemplateUtilTestConfig.class, ModelMapperConfig.class})
@TestPropertySource("classpath:exception.properties")
@WebMvcTest(NotificationController.class)
@ExtendWith(SpringExtension.class)
class FriendshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
    }

}