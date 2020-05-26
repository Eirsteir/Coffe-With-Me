package com.eirsteir.coffeewithme.commons.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsEvent extends AbstractUserAccountEvent {

    public UserAlreadyExistsEvent(Long accountId) {
        super(accountId);
    }

}
