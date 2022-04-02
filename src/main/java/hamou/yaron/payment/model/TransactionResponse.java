package hamou.yaron.payment.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    private boolean approved;

    private Map<String, String> errors;

    public TransactionResponse(boolean approved, Map<String, String> errors) {
        this.approved = approved;
        this.errors = errors;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionResponse that = (TransactionResponse) o;
        return Objects.equals(approved, that.approved) && Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approved, errors);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "approved='" + approved + '\'' +
                ", errors=" + errors +
                '}';
    }
}
