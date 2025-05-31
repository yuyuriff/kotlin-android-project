package com.example.ollamaserver.controller

import com.example.ollamaserver.model.ClientRequest
import com.example.ollamaserver.model.OllamaGenerateRequest
import com.example.ollamaserver.model.OllamaGenerateResponse
import com.example.ollamaserver.model.ServerChatResponse
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@RestController
@RequestMapping("/api/ollama")

class OllamaController {
    private val restTemplate = RestTemplate()
    private val ollamaUrl = "http://localhost:11434/api/generate"
    private val objectMapper = jacksonObjectMapper()

    @PostMapping("/chat", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun chat(@RequestBody req:ClientRequest): ResponseEntity<ServerChatResponse> {
        val body = OllamaGenerateRequest(
            model = req.model,
            prompt = req.prompt,
            context = req.context,
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }

        val httpEntity = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(ollamaUrl, httpEntity, String::class.java)
        val ollamaResponse = objectMapper.readValue(response.body, OllamaGenerateResponse::class.java)

        return ResponseEntity
            .status(response.statusCode)
            .body(ServerChatResponse(
            response = ollamaResponse.response,
            context = ollamaResponse.context))
    }
}