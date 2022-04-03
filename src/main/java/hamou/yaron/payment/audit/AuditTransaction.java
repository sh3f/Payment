package hamou.yaron.payment.audit;

import hamou.yaron.payment.model.Transaction;

public interface AuditTransaction {

    void auditMaskedTransaction(Transaction transaction);
}
