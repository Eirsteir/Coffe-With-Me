package com.eirsteir.coffeewithme.commons.domain.account;

import com.eirsteir.coffeewithme.commons.domain.user.UserEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractUserAccountEvent implements UserEvent {

  protected Long accountId;
}
