package hamou.yaron.payment.model;

import hamou.yaron.payment.model.validation.CardExpiration;
import org.hibernate.validator.constraints.LuhnCheck;

import javax.validation.constraints.*;
import java.util.Objects;

public class Card {

    @NotEmpty(message = "pan is required")
    @Pattern(regexp = "[\\d]{16}", message = "pan should be 16 digits long")
    @LuhnCheck(message = "pan should pass a Luhn check")
    private String pan;

    @CardExpiration(message = "expiry should be 4 digits long and not be in the past")
    private String expiry;

    @NotEmpty(message = "cvv is required")
    private String cvv;

    public Card() {
    }

    public Card(String pan, String expiry, String cvv) {
        this.pan = pan;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(pan, card.pan) &&
                Objects.equals(expiry, card.expiry) &&
                Objects.equals(cvv, card.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pan, expiry, cvv);
    }

    @Override
    public String toString() {
        return "Card{" +
                "pan='" + pan + '\'' +
                ", expiry='" + expiry + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
