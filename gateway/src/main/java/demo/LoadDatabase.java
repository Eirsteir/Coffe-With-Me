package demo;

import demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

//  @Bean
//  CommandLineRunner initDatabase(UserRepository repository) {
//    return args -> {
//      log.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
//      log.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
//    };
//  }

//  .withUser("user").password("password").roles("USER")
//            .and()
//                .withUser("admin").password("admin").roles("USER", "ADMIN", "READER", "WRITER")
//            .and()
//                .withUser("audit").password("audit").roles("USER", "ADMIN", "READER");
}
