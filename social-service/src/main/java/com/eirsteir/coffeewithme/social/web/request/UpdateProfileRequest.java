package com.eirsteir.coffeewithme.social.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProfileRequest {

  @NotBlank(message = "Field is required")
  private String nickname;

  @NotNull(message = "Field is required")
  @Positive(message = "Must be a positive integer")
  private Long universityId;
}
