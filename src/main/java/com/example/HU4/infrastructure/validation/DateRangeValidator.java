package com.example.HU4.infrastructure.validation;

import com.example.HU4.infrastructure.dto.EventRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    @Override
    public void initialize(DateRange constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof EventRequest) {
            EventRequest request = (EventRequest) value;
            LocalDateTime start = request.getStartDate();
            LocalDateTime end = request.getEndDate();

            if (start != null && end != null) {
                return start.isBefore(end);
            }
        }

        return true;
    }
}