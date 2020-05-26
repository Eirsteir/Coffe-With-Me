package com.eirsteir.coffeewithme.commons.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent implements AccountEvent {

    private Long accountId;

}
