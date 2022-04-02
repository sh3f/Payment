package hamou.yaron.payment.controller;

import hamou.yaron.payment.model.Transaction;
import hamou.yaron.payment.model.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TransactionController {

    @PostMapping(value = "/transaction", consumes = "application/json")
    TransactionResponse processTransaction(@Valid @RequestBody Transaction transaction) {
        return new TransactionResponse(true, null);
    }

    @GetMapping("/transaction/{invoice}")
    Transaction getTransaction(@PathVariable String invoice) {
        return new Transaction();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public TransactionResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(getNameAfterDot(fieldName), errorMessage);
        });
        return new TransactionResponse(false, errors);
    }

    private String getNameAfterDot(String fieldName) {
        int lastIndexOfDot = fieldName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return fieldName.toLowerCase();
        }
        return fieldName.toLowerCase().substring(lastIndexOfDot + 1);
    }
}
