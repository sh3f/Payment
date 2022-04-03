package hamou.yaron.payment.service;

import hamou.yaron.payment.audit.AuditTransaction;
import hamou.yaron.payment.controller.TransactionDoesNotExist;
import hamou.yaron.payment.dao.TransactionDao;
import hamou.yaron.payment.crypt.Crypt;
import hamou.yaron.payment.model.Transaction;
import hamou.yaron.payment.model.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {

    TransactionDao transactionDao;
    Crypt crypt;
    AuditTransaction audit;

    public TransactionService(TransactionDao transactionDao, Crypt crypt, AuditTransaction audit) {
        this.transactionDao = transactionDao;
        this.crypt = crypt;
        this.audit = audit;
    }

    public TransactionResponse save(Transaction transaction) {
        Transaction transactionToBeSaved = new Transaction(transaction);
        encrypt(transactionToBeSaved);

        mask(transaction);
        audit.auditMaskedTransaction(transaction);

        if (!transactionDao.save(transactionToBeSaved)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("transaction " + transactionToBeSaved.getInvoice(), "has already been processed");
            return new TransactionResponse(false, errors);
        }

        return new TransactionResponse(true, null);
    }

    public void encrypt(Transaction transaction) {
        transaction.getCardHolder().setName(crypt.encrypt(transaction.getCardHolder().getName()));
        transaction.getCard().setPan(crypt.encrypt(transaction.getCard().getPan()));
        transaction.getCard().setExpiry(crypt.encrypt(transaction.getCard().getExpiry()));
        transaction.getCard().setCvv(null);
    }

    public Transaction getByInvoice(String invoice) {
        Transaction savedTransaction = transactionDao.getByInvoice(invoice);
        if (savedTransaction == null)
            throw new TransactionDoesNotExist("transaction " + invoice + " does not exist");

        Transaction transaction = new Transaction(savedTransaction);
        decrypt(transaction);
        mask(transaction);
        return transaction;
    }

    public void mask(Transaction transaction) {
        transaction.getCardHolder().setName("******");
        transaction.getCard().setExpiry("****");
        transaction.getCard().setPan("************" + transaction.getCard().getPan().substring(12));
        transaction.getCard().setCvv(null);
    }

    public void decrypt(Transaction transaction) {
        transaction.getCardHolder().setName(crypt.decrypt(transaction.getCardHolder().getName()));
        transaction.getCard().setPan(crypt.decrypt(transaction.getCard().getPan()));
        transaction.getCard().setExpiry(crypt.decrypt(transaction.getCard().getExpiry()));
    }
}
