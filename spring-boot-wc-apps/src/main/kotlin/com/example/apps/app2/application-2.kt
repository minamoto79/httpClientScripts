package com.example.apps.apps2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.net.URI

@RestController
@ComponentScan("com.example.apps.app2")
class App2Controller(){
    @RequestMapping("/app2", method = [RequestMethod.POST])
    suspend fun index() = buildString {
        val responseSpec = WebClient.create().post().uri(URI("http://localhost:7070/app3")).retrieve()
        val app3response = responseSpec.awaitBody<String>()
        appendLine("app2, $app3response")
    }
}

fun main(vararg args: String) {
    runApplication<Application2>(*args) {
        setDefaultProperties(mapOf(
            "logging.level.org.springframework.web" to "DEBUG"
        ))
    }
}

@SpringBootApplication
open class Application2
