package com.eirsteir.coffeewithme.authservice.domain;


import com.eirsteir.coffeewithme.commons.domain.AccountCreatedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    private String name;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    // TODO: 26.05.2020 use UserDetails?
    public static ResultWithEvents<Account> createAccount(Account account) {
        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent(account.getId());
        return new ResultWithEvents<>(account, Collections.singletonList(accountCreatedEvent));
    }
}
