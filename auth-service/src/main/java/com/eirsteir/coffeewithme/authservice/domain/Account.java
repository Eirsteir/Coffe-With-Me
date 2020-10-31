package com.eirsteir.coffeewithme.authservice.domain;

import com.eirsteir.coffeewithme.commons.domain.account.AccountCreatedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  private String email;

  private String name;

  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "account_roles",
      joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;

  public static ResultWithEvents<Account> createAccount(Account account) {
    AccountCreatedEvent accountCreatedEvent =
        new AccountCreatedEvent(account.getId(), account.getEmail(), account.getName());
    return new ResultWithEvents<>(account, Collections.singletonList(accountCreatedEvent));
  }
}
