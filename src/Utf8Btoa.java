import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class Utf8Btoa {
    public static void main(String[] args) {
        String original = "Привет";

        // Step 1: encode to UTF-8 bytes
        byte[] utf8Bytes = original.getBytes(StandardCharsets.UTF_8);

        // Step 2: encode bytes to base64
        String base64 = Base64.getEncoder().encodeToString(utf8Bytes);

        System.out.println("Base64 (like JS btoa(unescape(encodeURIComponent))):");
        System.out.println(base64);  // should print: 0J/RgNC40LLQtdGC
    }
}