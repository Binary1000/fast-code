package com.cnqisoft.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {EnumValue.EnumValidator.class}
)
public @interface EnumValue {

    Class<?> value();

    String message() default "invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EnumValidator implements ConstraintValidator<EnumValue, Object> {

        private EnumValue enumValue;

        @Override
        public void initialize(EnumValue enumValue) {
            this.enumValue = enumValue;
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            try {
                Method method = enumValue.value().getMethod("isPresent", value.getClass());
                return (Boolean) method.invoke(null, value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
