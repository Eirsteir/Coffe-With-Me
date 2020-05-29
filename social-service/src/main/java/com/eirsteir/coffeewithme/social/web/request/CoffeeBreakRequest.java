package com.eirsteir.coffeewithme.social.web.request;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Positive;


@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeBreakRequest {

    @Positive(message = "Must be a positive number")
    private Long scheduledToInMinutes;
    private Long campusId;

}
