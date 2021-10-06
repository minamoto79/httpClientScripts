package com.example.apps.app3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
@ComponentScan("com.example.apps.app3")
class App3Controller {
    @RequestMapping("/app3", method = [RequestMethod.POST])
    fun index() = buildString {
        appendLine("app3")
    }
}

fun main(vararg args: String) {
    runApplication<Application3>(*args){
        setDefaultProperties(mapOf("server.port" to "7070"))
    }
}

@SpringBootApplication
open class Application3
