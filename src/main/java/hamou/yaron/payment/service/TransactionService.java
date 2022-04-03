package hamou.yaron.payment.service;

import hamou.yaron.payment.dao.TransactionDao;
import hamou.yaron.payment.model.dto.Transaction;
import hamou.yaron.payment.model.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {

    TransactionDao transactionDao;

    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public TransactionResponse save(Transaction transaction) {
        if (!transactionDao.save(transaction)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("transaction " + transaction.getInvoice() + " ", " has already been processed");
            return new TransactionResponse(false, errors);
        }
        return new TransactionResponse(true, null);
    }
}
