package com.eirsteir.coffeewithme.commons.domain.user;

import com.eirsteir.coffeewithme.commons.domain.account.AbstractUserAccountEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserCreatedEvent extends AbstractUserAccountEvent {

  public UserCreatedEvent(Long accountId) {
    super(accountId);
  }
}
