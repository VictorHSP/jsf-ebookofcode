package br.com.ebookofcode.utils;

import java.security.SecureRandom;
import java.util.Base64;

public final class RandomGeneratorUtils {

    public static String generateBase64() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[5];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
