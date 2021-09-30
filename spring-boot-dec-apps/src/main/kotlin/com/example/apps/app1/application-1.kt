package com.example.apps.app1

import com.example.apps.app2.App2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject



@RestController
class App1Controller @Autowired constructor(private val app2: App2){
    @RequestMapping("/app1", method = [RequestMethod.GET])
    fun index() = buildString {
        val app2response = app2.app2()
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
@EnableFeignClients(basePackages =  ["com.example.apps.app2"])
open class Application1
