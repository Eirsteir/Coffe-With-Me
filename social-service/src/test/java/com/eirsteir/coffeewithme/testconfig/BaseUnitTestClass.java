package com.eirsteir.coffeewithme.testconfig;

import com.eirsteir.coffeewithme.commons.exception.MessageTemplateUtil;
import com.eirsteir.coffeewithme.commons.exception.PropertiesConfig;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


/**
 * Setup mock for PropertiesConfig and MessageTemplateUtil
 * to avoid NullPointerException when running tests
 * that throw a custom exception.
 */
@TestConfiguration
public class BaseUnitTestClass {

    @Mock
    private static PropertiesConfig propertiesConfig;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @TestConfiguration
    static class BaseUnitTestClassTestConfiguration {

        @Bean()
        public MessageTemplateUtil messageTemplateUtil() {
            return new MessageTemplateUtil(propertiesConfig);
        }
    }
}
