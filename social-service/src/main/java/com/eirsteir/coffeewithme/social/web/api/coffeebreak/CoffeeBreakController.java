package com.eirsteir.coffeewithme.social.web.api.coffeebreak;


import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService;
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/coffee-breaks")
@RestController
public class CoffeeBreakController {

    @Autowired
    private CoffeeBreakService coffeeBreakService;

    @PostMapping
    CoffeeBreakDetails registerCoffeeBreak(@Valid @RequestBody CoffeeBreakRequest coffeeBreakRequest,
                                           @AuthenticationPrincipal UserDetailsImpl principal) {

        return coffeeBreakService.registerCoffeeBreak(coffeeBreakRequest, principal);
    }


}
