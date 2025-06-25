package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            ValidationException.class,
            DuplicateKeyException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public ErrorResponse handleValidationException(final Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(ValidationException.class.getSimpleName(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(NotFoundException.class.getSimpleName(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(RuntimeException.class.getSimpleName(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorResponse handleHandlerMethodValidationException(final HandlerMethodValidationException e) {
        var errorMessage = e.getValueResults().getFirst().getResolvableErrors().getFirst().getDefaultMessage();
        log.error(errorMessage, e);
        return new ErrorResponse(HandlerMethodValidationException.class.getSimpleName(), errorMessage);
    }
}
