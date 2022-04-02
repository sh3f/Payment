package hamou.yaron.payment.controller;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;

@WebMvcTest
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToTransaction_thenCorrectResponse() throws Exception {
        String expiry = now();

        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": 1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"email@domain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"4200000000000000\"," +
                "        \"expiry\": \"" + expiry + "\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(true)));
    }

    @Test
    public void whenPostRequestToTransactionAndAllFieldsAreMissing_thenCorrectResponse() throws Exception {
        String transaction = "{}";

        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.invoice", Is.is("invoice is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.amount", Is.is("amount should be a positive integer")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.currency", Is.is("currency is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name", Is.is("name is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email", Is.is("email is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.pan", Is.is("pan is required")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.expiry", Is.is("expiry should be 4 digits long and not be in the past")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.cvv", Is.is("cvv is required")));
    }

    @Test
    public void whenPostRequestToTransactionAndAmountIsNegative_thenCorrectResponse() throws Exception {
        String expiry = now();

        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": -1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"email@domain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"4200000000000000\"," +
                "        \"expiry\": \"" + expiry + "\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.amount", Is.is("amount should be a positive integer")));
    }

    @Test
    public void whenPostRequestToTransactionAndEmailIsNotValid_thenCorrectResponse() throws Exception {
        String expiry = now();

        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": 1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"emaildomain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"4200000000000000\"," +
                "        \"expiry\": \"" + expiry + "\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email", Is.is("email should have a valid format")));
    }

    @Test
    public void whenPostRequestToTransactionAndPanIsNot16DigitsLong_thenCorrectResponse() throws Exception {
        String expiry = now();

        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": 1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"email@domain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"42\"," +
                "        \"expiry\": \"" + expiry + "\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.pan", Is.is("pan should be 16 digits long")));
    }

    @Test
    public void whenPostRequestToTransactionAndPanFailsLuhnCheck_thenCorrectResponse() throws Exception {
        String expiry = now();
        
        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": 1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"email@domain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"4200000000000001\"," +
                "        \"expiry\": \"" + expiry + "\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.pan", Is.is("pan should pass a Luhn check")));
    }

    @Test
    public void whenPostRequestToTransactionAndExpiryDateIs2Digits_thenCorrectResponse() throws Exception {
        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": 1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"email@domain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"4200000000000000\"," +
                "        \"expiry\": \"06\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.expiry", Is.is("expiry should be 4 digits long and not be in the past")));
    }

    @Test
    public void whenPostRequestToTransactionAndExpiryDateHasCharacters_thenCorrectResponse() throws Exception {
        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": 1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"email@domain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"4200000000000000\"," +
                "        \"expiry\": \"06aa\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.expiry", Is.is("expiry should be 4 digits long and not be in the past")));
    }

    @Test
    public void whenPostRequestToTransactionAndExpiryDateInPast_thenCorrectResponse() throws Exception {
        String expiry = monthAgo();

        String transaction = "" +
                "{" +
                "    \"invoice\": \"1234567\"," +
                "    \"amount\": 1299," +
                "    \"currency\": \"EUR\"," +
                "    \"cardholder\": {" +
                "        \"name\": \"First Last\"," +
                "        \"email\": \"email@domain.com\"" +
                "    }," +
                "    \"card\": {" +
                "        \"pan\": \"4200000000000000\"," +
                "        \"expiry\": \"" + expiry + "\"," +
                "        \"cvv\": \"789\"" +
                "    }" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approved", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.expiry", Is.is("expiry should be 4 digits long and not be in the past")));
    }

    private static String monthAgo() {
        return getMonthAndYear(LocalDate.now().minusMonths(1));
    }

    private static String now() {
        return getMonthAndYear(LocalDate.now());
    }

    private static String getMonthAndYear(LocalDate monthAgo) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String month = monthAgo.format(monthFormatter);

        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yy");
        String year = monthAgo.format(yearFormatter);

        return month + year;
    }
}