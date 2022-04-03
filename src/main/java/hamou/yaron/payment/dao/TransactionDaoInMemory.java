package hamou.yaron.payment.dao;

import hamou.yaron.payment.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionDaoInMemory implements TransactionDao {

    Map<String, Transaction> db = new ConcurrentHashMap<>();

    @Override
    public boolean save(Transaction transaction) {
        if (db.containsKey(transaction.getInvoice()))
            return false;
        db.put(transaction.getInvoice(), transaction);
        return true;
    }

    @Override
    public Transaction getByInvoice(String invoice) {
        if (!db.containsKey(invoice))
            return null;
        return db.get(invoice);
    }
}
