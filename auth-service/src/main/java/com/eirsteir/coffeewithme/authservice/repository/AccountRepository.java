package com.eirsteir.coffeewithme.authservice.repository;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByEmail(String email);
}
