package hamou.yaron.payment.crypt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base64CryptTest {

    Crypt crypt = new Base64Crypt();

    @Test
    public void whenEncrypting_thenDecryptingProducesSameResult() {
        String value = "abc";
        String encrypted = crypt.encrypt(value);
        assertNotEquals(value, encrypted);

        String decrypted = crypt.decrypt(encrypted);
        assertEquals(value, decrypted);
    }

    @Test
    public void whenEncryptingSpace_thenDecryptingProducesSameResult() {
        String value = "First Last";
        String encrypted = crypt.encrypt(value);
        assertNotEquals(value, encrypted);

        String decrypted = crypt.decrypt(encrypted);
        assertEquals(value, decrypted);
    }
}