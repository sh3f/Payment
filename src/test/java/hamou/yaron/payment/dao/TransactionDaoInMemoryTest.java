package hamou.yaron.payment.dao;

import hamou.yaron.payment.model.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDaoInMemoryTest {

    TransactionDao dao = new TransactionDaoInMemory();

    @Test
    public void whenSavingSameInvoice_thenReturnsFalse() {
        Transaction transaction = new Transaction("1");

        assertTrue(dao.save(transaction));
        assertFalse(dao.save(transaction));
    }

    @Test
    public void whenGettingByInvoiceAndInvoiceDoesNotExist_thenReturnsNull() {
        assertNull(dao.getByInvoice("1"));
    }

    @Test
    public void whenGettingByInvoiceAndInvoiceExists_thenReturnsTransaction() {
        Transaction transaction = new Transaction("1");

        dao.save(transaction);

        assertEquals(transaction, dao.getByInvoice("1"));
    }
}