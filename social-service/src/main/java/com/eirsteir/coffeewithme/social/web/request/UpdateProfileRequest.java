package com.eirsteir.coffeewithme.social.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProfileRequest {

    @NotBlank(message = "Nickname is required")
    private String nickname;

    @NotBlank(message = "University id is required")
    private Long universityId;

    @NotBlank(message = "University id is required")
    private Long universityId;

}
