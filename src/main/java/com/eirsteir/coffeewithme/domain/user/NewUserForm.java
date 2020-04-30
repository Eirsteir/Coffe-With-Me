package com.eirsteir.coffeewithme.domain.user;

import com.eirsteir.coffeewithme.validation.constraints.FieldsValueMatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "Username may not be blank")
    private String username;

    @Email
    @NotBlank(message = "Email address may not be blank")
    private String email;

    @Email
    @NotBlank(message = "Confirm email address may not be blank")
    private String verifyEmail;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @NotBlank
    private String verifyPassword;

}