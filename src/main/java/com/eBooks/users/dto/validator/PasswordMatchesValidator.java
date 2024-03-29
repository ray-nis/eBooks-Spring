package com.eBooks.users.dto.validator;

import com.eBooks.users.dto.UserSignupDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        UserSignupDto userSignupDto = (UserSignupDto)o;
        return userSignupDto.getPassword().equals(userSignupDto.getMatchingPassword());
    }
}