package com.eirsteir.coffeewithme.social.web.request;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;


@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeBreakRequest {


    @NotNull(message = "Must be a valid id")
    private Long requesterId;

    @NotEmpty(message = "Must be a valid list of ids")
    @NotNull(message = "Must not be null")
    private Set<Long> addresseeIds;

    @Positive(message = "Must be a positive number")
    private Long scheduledToInMinutes; // TODO: 29.05.2020 default to now
    private Long campusId;

}
