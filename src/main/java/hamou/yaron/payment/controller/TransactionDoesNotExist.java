package hamou.yaron.payment.controller;

public class TransactionDoesNotExist extends RuntimeException {

    public TransactionDoesNotExist(String message) {
        super(message);
    }
}
