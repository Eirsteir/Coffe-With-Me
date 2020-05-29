package com.eirsteir.coffeewithme.commons.domain.coffeebreak;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeBreakCreatedEvent implements CoffeeBreakEvent {

    private CoffeeBreakDetails coffeeBreakDetails;

}
