package com.eirsteir.coffeewithme.notification.web.api;

import com.eirsteir.coffeewithme.notification.config.ModelMapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import({ModelMapperConfig.class})
@WebMvcTest(NotificationController.class)
@ExtendWith(SpringExtension.class)
class NotificationControllerTest {

  @BeforeEach
  void setUp() {}
}
