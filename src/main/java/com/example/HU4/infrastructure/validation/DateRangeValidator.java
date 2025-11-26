package main.java.com.example.HU4.infrastructure.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDate();
        this.endDateField = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object start = new BeanWrapperImpl(value).getPropertyValue(startDateField);
        Object end = new BeanWrapperImpl(value).getPropertyValue(endDateField);
        if (start != null && end != null) {
            return ((LocalDateTime) start).isBefore((LocalDateTime) end);
        }
        return true;
    }
}