package hamou.yaron.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Objects;

public class Transaction {

    @NotEmpty(message = "invoice is required")
    private String invoice;

    @Positive(message = "amount should be a positive integer")
    private int amount;

    @NotEmpty(message = "currency is required")
    private String currency;

    @Valid
    @JsonProperty(value = "cardholder")
    private CardHolder cardHolder = new CardHolder();

    @Valid
    private Card card = new Card();

    public Transaction() {
    }

    public Transaction(String invoice) {
        this.invoice = invoice;
    }

    public Transaction(String invoice,
                       int amount,
                       String currency,
                       CardHolder cardHolder,
                       Card card) {
        this.invoice = invoice;
        this.amount = amount;
        this.currency = currency;
        this.cardHolder = cardHolder;
        this.card = card;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CardHolder getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(CardHolder cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction transaction = (Transaction) o;
        return amount == transaction.amount &&
                Objects.equals(invoice, transaction.invoice) &&
                Objects.equals(currency, transaction.currency) &&
                Objects.equals(cardHolder, transaction.cardHolder) &&
                Objects.equals(card, transaction.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoice, amount, currency, cardHolder, card);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "invoice='" + invoice + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", cardHolder=" + cardHolder +
                ", card=" + card +
                '}';
    }
}
