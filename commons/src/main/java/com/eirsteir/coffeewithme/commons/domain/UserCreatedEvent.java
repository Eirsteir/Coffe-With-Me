package com.eirsteir.coffeewithme.commons.domain;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserCreatedEvent extends AbstractUserAccountEvent {

    public UserCreatedEvent(Long accountId) {
        super(accountId);
    }

}
