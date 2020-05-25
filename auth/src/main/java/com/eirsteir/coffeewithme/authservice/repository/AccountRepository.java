package com.eirsteir.coffeewithme.authservice.repository;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

}
