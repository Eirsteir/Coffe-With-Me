package com.eirsteir.coffeewithme.web.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {

    @Email(message = "Must be a well formed email.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    private String password;

    @NotBlank(message = "Name cannot be blank.")
    private String name;
}