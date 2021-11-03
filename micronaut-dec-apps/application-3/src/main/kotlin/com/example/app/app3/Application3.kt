package com.example.app.app2

import com.example.app.common.Application2OperationClient
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.Micronaut.build

@Controller("/")
class Application2Controller: Application2OperationClient {
    @Post("/app-3")
    override fun application3() = buildString {
        appendLine("app3")
    }
}

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages(
            "com.example.app.app3",
            "com.example.app.common")
        .start()
}

