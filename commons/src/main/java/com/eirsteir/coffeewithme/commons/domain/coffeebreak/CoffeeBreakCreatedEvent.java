package com.eirsteir.coffeewithme.commons.domain.coffeebreak;

import com.eirsteir.coffeewithme.commons.domain.notification.AbstractEntityNotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeBreakCreatedEvent  extends AbstractEntityNotificationEvent implements CoffeeBreakEvent {

    private CoffeeBreakDetails coffeeBreakDetails;

}
