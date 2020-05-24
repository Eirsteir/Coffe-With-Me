package com.eirsteir.coffeewithme.authservice.repository;

import com.eirsteir.coffeewithme.authservice.domain.UserCredentials;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends CrudRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByEmail(String email);

}
