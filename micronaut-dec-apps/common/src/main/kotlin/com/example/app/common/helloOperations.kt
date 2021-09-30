package com.example.app.common

import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.validation.Validated
import javax.inject.Named

@Client("http://localhost:9090/")
interface Application1OperationClient {
    @Post("/app-2")
    fun application2() :String
}

@Client("http://localhost:7070/")
interface Application2OperationClient {
    @Post("/app-3")
    fun application3() :String
}
