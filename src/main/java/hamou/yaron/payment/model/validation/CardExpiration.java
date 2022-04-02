package hamou.yaron.payment.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CardExpirationValidator.class)
public @interface CardExpiration {
    String message() default "Expiration invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
