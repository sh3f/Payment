package hamou.yaron.payment.dao;

import hamou.yaron.payment.model.Transaction;

public interface TransactionDao {

    boolean save(Transaction transaction);

    Transaction getByInvoice(String invoice);
}
