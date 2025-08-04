import java.security.SecureRandom;

public class ShortUrlGenerator {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom random = new SecureRandom();

    public static String generateShortKey() {
        int length = 6; // 6 chars can generate 57B unique URLS
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(BASE62.length());
            sb.append(BASE62.charAt(index));

        }
        return sb.toString();

    }
}
