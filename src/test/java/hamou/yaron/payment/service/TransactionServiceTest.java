package hamou.yaron.payment.service;

import hamou.yaron.payment.controller.TransactionDoesNotExist;
import hamou.yaron.payment.crypt.Base64Crypt;
import hamou.yaron.payment.crypt.Crypt;
import hamou.yaron.payment.dao.TransactionDao;
import hamou.yaron.payment.model.Transaction;
import hamou.yaron.payment.model.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionDao dao;

    TransactionService service;

    Crypt crypt;

    @Captor
    ArgumentCaptor<Transaction> transactionCaptor;

    @BeforeEach
    void setUp() {
        crypt = new Base64Crypt();
        service = new TransactionService(dao, crypt);
    }

    @Test
    public void whenSavingTransaction_thenReturnCorrectResponse() {
        Transaction transaction = getTransaction();

        TransactionResponse expected = new TransactionResponse(true, null);

        Mockito.when(dao.save(any(Transaction.class))).thenReturn(true);

        TransactionResponse actual = service.save(transaction);

        Mockito.verify(dao).save(any(Transaction.class));
        assertEquals(expected, actual);
    }

    @Test
    public void whenSavingAlreadySavedTransaction_thenReturnCorrectResponse() {
        Transaction transaction = getTransaction();

        Map<String, String> errors = new HashMap<>();
        errors.put("transaction " + transaction.getInvoice(), "has already been processed");
        TransactionResponse expected = new TransactionResponse(false, errors);

        Mockito.when(dao.save(any(Transaction.class)))
                .thenReturn(true)
                .thenReturn(false);

        service.save(transaction);
        TransactionResponse actual = service.save(transaction);

        Mockito.verify(dao, Mockito.times(2)).save(any(Transaction.class));
        assertEquals(expected, actual);
    }

    @Test
    public void whenGettingTransaction_thenReturnCorrectResponse() {
        Transaction transaction = getTransaction();
        service.encrypt(transaction);

        Mockito.when(dao.getByInvoice(eq("1")))
                .thenReturn(transaction);

        Transaction actual = service.getByInvoice("1");

        Mockito.verify(dao).getByInvoice(any());
        service.decrypt(transaction);
        service.mask(transaction);
        assertEquals(transaction, actual);
    }

    @Test
    public void whenGettingTransactionAndInvoiceDoesNotExist_thenReturnCorrectResponse() {
        Mockito.when(dao.getByInvoice(eq("1")))
                .thenReturn(null);

        Exception exception = assertThrows(TransactionDoesNotExist.class, () -> service.getByInvoice("1"));

        String expectedMessage = "transaction 1 does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(dao).getByInvoice(any());
    }

    @Test
    public void whenSavingTransaction_thenSavedTransactionIsBase64Encrypted() {
        Transaction transaction = getTransaction();

        service.save(transaction);

        Mockito.verify(dao).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();

        assertEquals(crypt.encrypt(transaction.getCardHolder().getName()), savedTransaction.getCardHolder().getName());
        assertEquals(crypt.encrypt(transaction.getCard().getPan()), savedTransaction.getCard().getPan());
        assertEquals(crypt.encrypt(transaction.getCard().getExpiry()), savedTransaction.getCard().getExpiry());
        assertNull(savedTransaction.getCard().getCvv());
    }

    private Transaction getTransaction() {
        Transaction transaction = new Transaction("1");
        transaction.getCardHolder().setName("First Last");
        transaction.getCard().setPan("4200000000000000");
        transaction.getCard().setExpiry("0624");
        transaction.getCard().setCvv("789");
        return transaction;
    }
}