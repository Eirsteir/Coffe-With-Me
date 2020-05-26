package com.eirsteir.coffeewithme.authservice.repository;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

}
