package com.eirsteir.coffeewithme.social.service.coffeebreak;

import com.eirsteir.coffeewithme.social.dto.CoffeeBreakDto;
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest;

public interface CoffeeBreakService {

  CoffeeBreakDto registerCoffeeBreak(CoffeeBreakRequest coffeeBreakDto);

}
