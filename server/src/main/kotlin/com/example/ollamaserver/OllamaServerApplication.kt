package com.example.ollamaserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OllamaServerApplication

fun main(args: Array<String>) {
    runApplication<OllamaServerApplication>(*args)
}