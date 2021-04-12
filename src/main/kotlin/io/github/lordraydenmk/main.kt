package io.github.lordraydenmk

import io.github.lordraydenmk.data.TodoExposedRepository
import io.github.lordraydenmk.http.routes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Used in application.conf
fun Application.module() {
    val repo = TodoExposedRepository()
    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(HttpStatusCode.NotFound, "Not Found!")
        }
        exception<Throwable> { t ->
            call.respond(HttpStatusCode.InternalServerError, "Oooops, we fucked up ${t.message}")
        }
    }
    install(CallLogging) {
        level = Level.DEBUG
    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.ContentType)
        anyHost()
    }
    install(ContentNegotiation) {
        json()
    }
    routing {
        routes(repo)
    }
}