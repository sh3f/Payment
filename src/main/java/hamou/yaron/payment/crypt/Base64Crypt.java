package hamou.yaron.payment.crypt;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64Crypt implements Crypt {

    @Override
    public String encrypt(String s) {
        String sWithoutSpaces = s.replaceAll("\\s", "+");
        return Base64.getEncoder().encodeToString(sWithoutSpaces.getBytes());
    }

    @Override
    public String decrypt(String s) {
        String s1 = new String(Base64.getDecoder().decode(s));
        return s1.replaceAll("[+]", " ");
    }
}
