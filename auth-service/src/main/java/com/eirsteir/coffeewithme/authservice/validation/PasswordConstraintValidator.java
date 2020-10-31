package com.eirsteir.coffeewithme.authservice.validation;

import com.google.common.base.Joiner;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.passay.*;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public void initialize(final ValidPassword arg0) {}

  @Override
  public boolean isValid(final String password, ConstraintValidatorContext context) {

    PasswordValidator validator =
        new PasswordValidator(
            Arrays.asList(
                new LengthRule(8, 64), new QwertySequenceRule(3, false), new WhitespaceRule()));
    RuleResult result = validator.validate(new PasswordData(password));

    if (result.isValid()) return true;

    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result)))
        .addConstraintViolation();
    return false;
  }
}
