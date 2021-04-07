package io.github.lordraydenmk

import io.github.lordraydenmk.data.TodoInMemoryRepository
import io.github.lordraydenmk.domain.TodoId
import io.github.lordraydenmk.domain.TodoItem
import io.github.lordraydenmk.http.routes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.HttpHeaders.AccessControlAllowHeaders
import io.ktor.http.HttpHeaders.AccessControlAllowMethods
import io.ktor.http.HttpHeaders.AccessControlAllowOrigin
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Used in application.conf
fun Application.module() {
    val repo = TodoInMemoryRepository()
    runBlocking { repo.createTodo(TodoItem(TodoId(UUID.randomUUID()), "Deploy to Heroku", false, "", 0)) }
    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(HttpStatusCode.NotFound, "Not Found!")
        }
        exception<Throwable> { t ->
            call.respond(HttpStatusCode.InternalServerError, "Oooops, we fucked up ${t.message}")
        }
    }
    install(CallLogging) {
        level = Level.INFO
    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        allowCredentials = true
        anyHost()
    }
    install(ContentNegotiation) {
        json()
    }
    routing {
        routes(repo)
    }
}