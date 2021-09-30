package com.example.apps.app1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject


@Configuration
@ComponentScan("com.example.apps.app1")
open class Application1Configuration {
    @Bean
    open fun restTemplate(builder: RestTemplateBuilder): RestTemplate =
        builder.rootUri("http://localhost:8080").build()
}

@RestController
class App1Controller(private val restTemplate1: RestTemplate){
    @RequestMapping("/app1", method = [RequestMethod.GET])
    fun index() = buildString {
        val app2response = restTemplate1.postForObject<String>("/app2")
        appendLine("Greeting from app1, app2 $app2response")
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
