package com.example.apps.app2

import com.example.apps.app3.App3
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@FeignClient("App2Client", url = "http://localhost:8080")
interface App2 {
    @PostMapping("/app2/{name}")
    fun app2(@PathVariable("name") name: String): String
}

@RestController
@Import(FeignClientsConfiguration::class)
class App2Controller @Autowired constructor (val app3Client : App3 ):App2 {

    override fun app2(name: String): String = buildString {
        val app3response = app3Client.app3(42)
        appendLine("Greeting from app2 $name, app3 $app3response")
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
@EnableFeignClients(basePackages = ["com.example.apps.app3"])
open class Application2
