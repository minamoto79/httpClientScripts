import org.graalvm.polyglot.Context
import java.nio.charset.StandardCharsets
import java.util.*


fun main(args: Array<String>) {
    // Step 1: Evaluate JS code and extract string
    Context.newBuilder("js").allowAllAccess(true).build().use { context ->
        // JS string we want to encode like btoa(unescape(encodeURIComponent(str)))
        val jsValue = context.eval("js", "\"Привет\"")
        val javaString = jsValue.asString() // Already UTF-16 in Java

        // Step 2: Encode to Base64 (equivalent to btoa(unescape(encodeURIComponent(str))))
        val utf8Bytes = javaString.toByteArray(StandardCharsets.UTF_8)
        val base64 = Base64.getEncoder().encodeToString(utf8Bytes)

        println("JS String: $javaString")
        println("Base64 (browser-style): $base64") // should be 0J/RgNC40LLQtdGC

        // Step 3: Decode back
        val decodedBytes = Base64.getDecoder().decode(base64)
        val decoded = String(decodedBytes, StandardCharsets.UTF_8)
        println("Decoded: $decoded")
    }
}
