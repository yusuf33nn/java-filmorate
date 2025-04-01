package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.PositiveDuration;

public class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Long> {

    @Override
    public boolean isValid(Long duration, ConstraintValidatorContext context) {
        return duration > 0L;
    }
}
