package io.github.lordraydenmk.http

import io.github.lordraydenmk.domain.TodoItem
import io.github.lordraydenmk.domain.TodoPatch
import io.github.lordraydenmk.domain.TodoRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import java.util.*

private fun PipelineContext<Unit, ApplicationCall>.urlBuilder(): URLBuilder =
    URLBuilder.createFromCall(call)

fun Routing.routes(repo: TodoRepository) {
    route("/") {
        get {
            call.respond(repo.getAll().map { it.toDto(urlBuilder()) })
        }
        idRoute("{id}", HttpMethod.Get) { todoId ->
            repo.getById(todoId)?.let { call.respond(it.toDto(urlBuilder())) } ?: call.respond(HttpStatusCode.NotFound)
        }
        post<TodoItemDto>("") { payload ->
            if (payload.title != null) {
                val todoItem = TodoItem(UUID.randomUUID(), payload.title, payload.completed, payload.order)
                call.respond(HttpStatusCode.Created, repo.createTodo(todoItem).toDto(urlBuilder()))
            } else call.respond(HttpStatusCode.BadRequest)
        }
        idRoute("{id}", HttpMethod.Patch) { todoId ->
            val payload = call.receive<TodoItemDto>()
            val patch = TodoPatch(payload.title, payload.completed, payload.order)
            val updated = repo.updateTodo(todoId, patch)
            if (updated != null) call.respond(updated.toDto(urlBuilder()))
            else call.respond(HttpStatusCode.NotFound)
        }
        delete {
            repo.deleteAll()
            call.respond(HttpStatusCode.NoContent)
        }
        idRoute("{id}", HttpMethod.Delete) { todoId ->
            repo.deleteById(todoId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}