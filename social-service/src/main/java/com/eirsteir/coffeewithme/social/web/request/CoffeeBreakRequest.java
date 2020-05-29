package com.eirsteir.coffeewithme.social.web.request;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


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
    private List<Long> addresseeIds;

    private Long scheduledToInMinutes; // TODO: 29.05.2020 default to now
    private Long campusId;

}
