package hamou.yaron.payment.service;

import hamou.yaron.payment.controller.TransactionDoesNotExist;
import hamou.yaron.payment.dao.TransactionDao;
import hamou.yaron.payment.model.TransactionResponse;
import hamou.yaron.payment.model.dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @BeforeEach
    void setUp() {
        service = new TransactionService(dao);
    }

    @Test
    public void whenSaving_thenReturnCorrectResponse() {
        Transaction transaction = new Transaction("1");

        TransactionResponse expected = new TransactionResponse(true, null);

        Mockito.when(dao.save(eq(transaction))).thenReturn(true);

        TransactionResponse actual = service.save(transaction);

        Mockito.verify(dao).save(eq(transaction));
        assertEquals(expected, actual);
    }

    @Test
    public void whenSavingAlreadySavedTransaction_thenReturnCorrectResponse() {
        Transaction transaction = new Transaction("1");

        Map<String, String> errors = new HashMap<>();
        errors.put("transaction " + transaction.getInvoice(), "has already been processed");
        TransactionResponse expected = new TransactionResponse(false, errors);

        Mockito.when(dao.save(eq(transaction)))
                .thenReturn(true)
                .thenReturn(false);

        service.save(transaction);
        TransactionResponse actual = service.save(transaction);

        Mockito.verify(dao, Mockito.times(2)).save(eq(transaction));
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetting_thenReturnCorrectResponse() {
        Transaction transaction = new Transaction("1");

        Mockito.when(dao.getByInvoice(eq("1")))
                .thenReturn(transaction);

        Transaction actual = service.getByInvoice("1");

        Mockito.verify(dao).getByInvoice(any());
        assertEquals(transaction, actual);
    }

    @Test
    public void whenGettingAndInvoiceDoesNotExist_thenReturnCorrectResponse() {
        Mockito.when(dao.getByInvoice(eq("1")))
                .thenReturn(null);

        Exception exception = assertThrows(TransactionDoesNotExist.class, () -> service.getByInvoice("1"));

        String expectedMessage = "transaction 1 does not exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(dao).getByInvoice(any());
    }
}