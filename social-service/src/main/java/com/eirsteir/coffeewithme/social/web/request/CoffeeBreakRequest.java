package com.eirsteir.coffeewithme.social.web.request;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;


@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeBreakRequest {

    @Max(value = 60 * 2, message = "Coffee break cannot be scheduled to start in more than 2 hours")
    @Positive(message = "scheduledToInMinutes must be a positive number")
    private Long scheduledToInMinutes;
    private Long campusId;

}
