package com.example.apps.app3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@FeignClient("App3Client", url = "http://localhost:7070")
interface App3 {
    @PostMapping("/app3")
    fun app3():String
}

@RestController
@ComponentScan("com.example.apps.app3")
class App3Controller : App3 {
    @PostMapping("/app3")
    override fun app3() = buildString {
        appendLine("Greeting from app3")
    }

}

fun main(vararg args: String) {
    runApplication<Application3>(*args){
        setDefaultProperties(mapOf("server.port" to "7070"))
    }
}

@SpringBootApplication
@EnableFeignClients
open class Application3
