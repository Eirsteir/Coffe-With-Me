package com.eirsteir.coffeewithme.api.web.request;

import com.eirsteir.coffeewithme.validation.FieldsValueMatch;
import com.eirsteir.coffeewithme.validation.ValidPassword;
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
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                otherField = "verifyPassword",
                message = "Passwords do not match"
        ),
        @FieldsValueMatch(
                field = "email",
                otherField = "verifyEmail",
                message = "Email addresses do not match"
        )
})
public class UserRegistrationRequest {

    @Email(message = "Must be a well formed email")
    @NotBlank(message = "Email is required")
    private String email;

    @Email(message = "Must be a well formed email")
    @NotBlank(message = "Email is required")
    private String verifyEmail;

    @ValidPassword
    private String password;

    @ValidPassword
    private String verifyPassword;

    @NotBlank(message = "Name is required")
    private String name;
}