package com.eirsteir.coffeewithme.web.request;

import com.eirsteir.coffeewithme.validation.FieldsValueMatch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be more than 8 characters")
    private String password;

    @NotBlank(message = "Password is required")
    private String verifyPassword;

    @NotBlank(message = "Name is required")
    private String name;
}