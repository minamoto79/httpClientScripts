package com.example.app.app2

import com.example.app.common.Application1OperationClient
import com.example.app.common.Application2OperationClient
import io.micronaut.http.annotation.Controller
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.Micronaut.build
import javax.inject.Inject

@Controller("/")
class Application2Controller: Application1OperationClient {
    @Client
    @Inject
    lateinit var client: Application2OperationClient

    override fun application2() = buildString {
        val application3 = client.application3()
        appendLine("app2, $application3")
    }
}



fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages(
            "com.example.app.app2",
            "com.example.app.common")
        .start()
}

