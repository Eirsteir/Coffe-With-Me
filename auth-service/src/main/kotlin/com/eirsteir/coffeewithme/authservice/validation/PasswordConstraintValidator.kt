package com.eirsteir.coffeewithme.authservice.validation

import com.google.common.base.Joiner
import org.passay.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordConstraintValidator : ConstraintValidator<ValidPassword, String> {
    override fun initialize(arg0: ValidPassword) {}

    override fun isValid(password: String?, context: ConstraintValidatorContext): Boolean {
        val validator = PasswordValidator(
            listOf(
                LengthRule(8, 64), QwertySequenceRule(3, false), WhitespaceRule()
            )
        )
        val result = validator.validate(PasswordData(password))
        if (result.isValid)
            return true

        context.disableDefaultConstraintViolation()
        context
            .buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result)))
            .addConstraintViolation()
        return false
    }
}