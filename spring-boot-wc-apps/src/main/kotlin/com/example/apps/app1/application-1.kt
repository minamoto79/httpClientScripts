package com.example.apps.app1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.net.URI


@RestController
class App1Controller(){
    @GetMapping("/app1")
    suspend fun index() = buildString {
        val responseSpec = WebClient.create().post().uri(URI("http://localhost:8080/app2")).retrieve()
        val app2response = responseSpec.awaitBody<String>()
        appendLine("Greeting from app1, $app2response")
    }
}

fun main(vararg args: String) {
    runApplication<Application1>(*args) {
        setDefaultProperties(mapOf(
            "server.port" to "9090",
            "logging.level.org.springframework.web" to "DEBUG"
        ))
    }
}

@SpringBootApplication
open class Application1
