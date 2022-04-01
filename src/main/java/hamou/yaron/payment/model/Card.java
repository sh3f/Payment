package hamou.yaron.payment.model;

import java.util.Objects;

public class Card {

    private String pan;

    private String expiry;

    private String cvv;

    public Card() {
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
        return Objects.equals(pan, card.pan) && Objects.equals(expiry, card.expiry) && Objects.equals(cvv, card.cvv);
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
