package com.example.apps.app3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@FeignClient("App3Client", url = "http://localhost:7070")
interface App3 {
    @PostMapping("/app3/{age}")
    fun app3(@PathVariable("age") age: Int):String
}

@RestController
@ComponentScan("com.example.apps.app3")
class App3Controller : App3 {
    override fun app3(age: Int) = buildString {
        appendLine("Greeting from app3: $age")
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
