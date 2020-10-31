package com.eirsteir.coffeewithme.authservice.web.request;

import com.eirsteir.coffeewithme.authservice.validation.ValidPassword;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class RegistrationRequest {

  @Email(message = "Must be a well formed email")
  @NotBlank(message = "Email is required")
  private String email;

  @ValidPassword private String password;

  @NotBlank(message = "Name is required")
  private String name;
}
