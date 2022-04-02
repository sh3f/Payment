package hamou.yaron.payment.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CardExpirationValidator implements ConstraintValidator<CardExpiration, String> {

    @Override
    public void initialize(CardExpiration annotation) {
    }

    @Override
    public boolean isValid(String expiry, ConstraintValidatorContext constraintValidatorContext) {
        return expiry != null &&
                expiry.length() == 4 &&
                expiry.matches("[0-9]+") &&
                inFutureOrNow(expiry);
    }

    private boolean inFutureOrNow(String expiry) {
        LocalDate now = LocalDate.now();

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String month = now.format(monthFormatter);

        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yy");
        String year = now.format(yearFormatter);

        Date expiryDateNow = getExpiryDate(month + year);
        Date expiryDate = getExpiryDate(expiry);

        return !expiryDate.before(expiryDateNow);
    }

    private Date getExpiryDate(String expiry) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyy");
        simpleDateFormat.setLenient(false);
        try {
            return simpleDateFormat.parse(expiry);
        } catch (ParseException e) {
            throw new RuntimeException("could not parse expiry");
        }
    }
}
