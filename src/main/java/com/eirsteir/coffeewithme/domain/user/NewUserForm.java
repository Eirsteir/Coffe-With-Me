package com.eirsteir.coffeewithme.domain.user;

import com.eirsteir.coffeewithme.validation.constraints.FieldsValueMatch;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "verifyPassword",
                message = "Passwords do not match!"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "verifyEmail",
                message = "Email addresses do not match!"
        )
})
public class NewUserForm {

    @Email
    @NotBlank(message = "Email address may not be blank")
    private String email;

    @Email
    @NotBlank(message = "Confirm email address may not be blank")
    private String confirmEmailAddress;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @NotBlank
    private String verifyPassword;

}