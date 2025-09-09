import org.graalvm.polyglot.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GraalJSBtoaExample {
    public static void main(String[] args) {
        // Step 1: Evaluate JS code and extract string
        try (Context context = Context.newBuilder("js").allowAllAccess(true).build()) {
            // JS string we want to encode like btoa(unescape(encodeURIComponent(str)))
            Value jsValue = context.eval("js", "encodeURIComponent(\"Привет\")");
            String javaString = jsValue.asString(); // Already UTF-16 in Java

            // Step 2: Encode to Base64 (equivalent to btoa(unescape(encodeURIComponent(str))))
            byte[] utf8Bytes = javaString.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.getEncoder().encodeToString(utf8Bytes);

            System.out.println("JS String: '" + javaString + "'");
            System.out.println("Base64 (browser-style): " + base64); // should be 0J/RgNC40LLQtdGC

            // Step 3: Decode back
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            String decoded = new String(decodedBytes, StandardCharsets.UTF_8);

            System.out.println("Decoded: " + decoded);
        }
    }
}