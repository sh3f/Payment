package hamou.yaron.payment.controller;

import hamou.yaron.payment.model.Transaction;
import hamou.yaron.payment.model.TransactionResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @PostMapping(value = "/transaction", consumes = "application/json")
    TransactionResponse processTransaction(@RequestBody Transaction transaction) {
        return new TransactionResponse(true, null);
    }

    @GetMapping("/transaction/{invoice}")
    Transaction getTransaction(@PathVariable String invoice) {
        return new Transaction(invoice);
    }
}
