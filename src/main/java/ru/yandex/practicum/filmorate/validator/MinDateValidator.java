package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.MinDate;

import java.time.LocalDate;

public class MinDateValidator implements ConstraintValidator<MinDate, LocalDate> {

    private LocalDate minDate;

    @Override
    public void initialize(MinDate constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.min());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isBefore(minDate);
    }
}
