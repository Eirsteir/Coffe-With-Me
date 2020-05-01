package com.eirsteir.coffeewithme.controllers;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("integrationtest")
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8090)
                                                                .extensions(CaptureStateTransformer.class));

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = User.builder()
                .username("alex")
                .build();

        userRepository.save(user);

        stubFor(get(urlPathMatching("/oauth/authorize.*"))
                        .willReturn(aResponse()
                                            .withStatus(302)
                                            .withHeader("Location", "http://localhost:8080/login/oauth2/code/my-oauth-client?code=my-acccess-code&state=${state}")
                                            .withTransformers("CaptureStateTransformer")
                        )
        );
        stubFor(post(urlPathMatching("/oauth/token"))
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("{\"access_token\":\"my-access-token\"" +
                                                              ", \"token_type\":\"Bearer\"" +
                                                              ", \"expires_in\":\"3600\"" +
                                                              "}")
                        )
        );


    }

    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200()
            throws Exception {
        stubFor(get(urlPathMatching("/api/v1/users"))
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("{\"sub\":\"my-user-id\"" +
                                                              ",\"name\":\"Mark Hoogenboom\"" +
                                                              ", \"email\":\"mark.hoogenboom@example.com\"" +
                                                              "}")
                        )
        );
//        assertThat(testClient.get("/some/thing").statusCode(), is(200));
//
//        mockMvc.perform(get("/users")
//                            .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].username", is("alex")));
    }

}