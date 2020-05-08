package config;

import com.eirsteir.coffeewithme.config.PropertiesConfig;
import com.eirsteir.coffeewithme.exception.CWMException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CWMExceptionTestConfig {

    @Bean
    public CWMException cwmException(){
        return new CWMException(propertiesConfig());
    }

    @Bean
    public PropertiesConfig propertiesConfig() {
        return new PropertiesConfig();
    }

}
