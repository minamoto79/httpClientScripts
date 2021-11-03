package com.example.app.app1

import com.example.app.common.Application1OperationClient
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.Micronaut.build
import javax.inject.Inject

@Controller("/")

class Application1Controller {
    @Client
    @Inject
    lateinit  var client: Application1OperationClient
    @Get("/app-1")
    @Produces(MediaType.TEXT_PLAIN)
    fun index() = buildString {
        val application2 = client.application2()
        appendLine("app1, $application2")
    }
}



fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages(
            "com.example.app.app1",
            "com.example.app.common")
        .start()
}

