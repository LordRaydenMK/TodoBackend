package io.github.lordraydenmk.http

import io.github.lordraydenmk.domain.TodoRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.slf4j.event.Level

fun Application.testModule(repo: TodoRepository) {
    install(CallLogging) { level = Level.INFO }
    install(ContentNegotiation) { json() }
    routing { routes(repo) }
}