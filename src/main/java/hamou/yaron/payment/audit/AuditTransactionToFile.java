package hamou.yaron.payment.audit;

import hamou.yaron.payment.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class AuditTransactionToFile implements AuditTransaction {

    public static final String AUDIT_FILE_NAME = "audit.json";
    String fileName;

    public AuditTransactionToFile(@Value("${audit.path}") String path) {
        this.fileName = path + "/" + AUDIT_FILE_NAME;
        File yourFile = new File(this.fileName);
        try {
            yourFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void auditMaskedTransaction(Transaction transaction) {
        try {
            Files.write(
                    Paths.get(fileName),
                    (transaction.toString() + "\n").getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
