package com.eirsteir.coffeewithme.commons.domain.user;

import com.eirsteir.coffeewithme.commons.domain.account.AbstractUserAccountEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsEvent extends AbstractUserAccountEvent {

  public UserAlreadyExistsEvent(Long accountId) {
    super(accountId);
  }
}
