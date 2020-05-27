package com.eirsteir.coffeewithme.authservice.service;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.domain.RoleType;
import com.eirsteir.coffeewithme.authservice.repository.AccountRepository;
import com.eirsteir.coffeewithme.authservice.web.request.UserRegistrationRequest;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.exception.EntityType;
import com.eirsteir.coffeewithme.commons.exception.ExceptionType;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;


@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private DomainEventPublisher domainEventPublisher;
    private AccountRepository accountRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public AccountServiceImpl(DomainEventPublisher domainEventPublisher, AccountRepository accountRepository) {
        this.domainEventPublisher = domainEventPublisher;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account registerAccount(UserRegistrationRequest registrationRequest) {
        Optional<Account> user = accountRepository.findByEmail(registrationRequest.getEmail());

        if (user.isPresent())
            throw CWMException.getException(
                    EntityType.ACCOUNT, ExceptionType.DUPLICATE_ENTITY, user.get().getId().toString());

        Account account1 = createAccount(registrationRequest);
        log.debug("[x] Created account {}", account1);
        Account account = accountRepository.save(account1);
        log.info("[x] Registered account: {}", account);

        ResultWithEvents<Account> accountWithEvents = Account.createAccount(account);
        domainEventPublisher.publish(Account.class, account.getId(), accountWithEvents.events);
        log.info("[x] Publishing {} to {}", Account.class, accountWithEvents);

        return account;
    }

    private Account createAccount(UserRegistrationRequest registrationRequest) {
        Role basicRole = roleService.getOrCreateRole(RoleType.ROLE_USER);
        return Account.builder()
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .roles(Collections.singletonList(basicRole))
                .password(encoder.encode(registrationRequest.getPassword()))
                .build();
    }

}
