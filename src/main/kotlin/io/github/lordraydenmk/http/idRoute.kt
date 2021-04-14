package io.github.lordraydenmk.http

import io.github.lordraydenmk.domain.TodoId
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun ApplicationCall.todoId(): TodoId? =
    requireNotNull(parameters["id"]) { "Parameter {id} not found! This function can only be used inside routes with path containing {id}" }
        .toUUIDOrNull()

@ContextDsl
inline fun Route.idRoute(
    path: String,
    method: HttpMethod,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(TodoId) -> Unit
): Route = route(path, method) {
    handle {
        val todoId = call.todoId()
        if (todoId == null) call.respond(HttpStatusCode.BadRequest)
        else body(todoId)
    }
}