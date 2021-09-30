package com.example.apps.apps2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

@Configuration
@ComponentScan("com.example.apps.apps2")
open class Application2Configuration {
    @Bean
    open fun restTemplate(builder: RestTemplateBuilder): RestTemplate =
        builder.rootUri("http://localhost:7070").build()
}

@RestController
class App2Controller(private val restTemplate2: RestTemplate){
    @RequestMapping("/app2", method = [RequestMethod.POST])
    fun index() = buildString {
        val app3response = restTemplate2.postForObject<String>("/app3", null)
        appendLine("Greeting from app2, app3 $app3response")
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
