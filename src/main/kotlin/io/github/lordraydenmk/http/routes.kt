package io.github.lordraydenmk.http

import io.github.lordraydenmk.data.TodoInMemoryRepository
import io.github.lordraydenmk.domain.PatchTodo
import io.github.lordraydenmk.domain.TodoId
import io.github.lordraydenmk.domain.TodoItem
import io.github.lordraydenmk.domain.patch
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

fun Routing.routes(repo: TodoInMemoryRepository) {
    get("/") {
        call.respond(repo.getAll().map { it.toDto(urlBuilder()) })
    }
    post("/") {
        val payload = call.receive<TodoItemDto>()
        if (payload.title != null) {
            val id = payload.id?.let { UUID.fromString(it) } ?: UUID.randomUUID()
            val todoItem = TodoItem(TodoId(id), payload.title, payload.completed ?: false, payload.order ?: 0)
            call.respond(HttpStatusCode.Created, repo.createTodo(todoItem).toDto(urlBuilder()))
        } else call.respond(HttpStatusCode.BadRequest)
    }
    delete("/") {
        repo.deleteAll()
        call.respond(HttpStatusCode.NoContent)
    }
    patch("/{id}") {
        val id = UUID.fromString(call.parameters["id"]!!)
        val todoId = TodoId(id)
        val payload = call.receive<TodoItemDto>()
        val patch = PatchTodo(
            payload.id?.let { TodoId(UUID.fromString(it)) },
            payload.title,
            payload.completed,
            payload.url,
            payload.order
        )
        val updated = repo.getById(todoId)?.patch(patch)
        if (updated != null) call.respond(repo.updateTodo(todoId, updated).toDto(urlBuilder()))
        else call.respond(HttpStatusCode.NotFound)
    }
    get("/{id}") {
        val id = UUID.fromString(call.parameters["id"]!!)
        repo.getById(TodoId(id))?.let { call.respond(it.toDto(urlBuilder())) } ?: call.respond(HttpStatusCode.NotFound)
    }
    delete("/{id}") {
        val id = UUID.fromString(call.parameters["id"]!!)
        val todoId = TodoId(id)
        repo.deleteById(todoId)
        call.respond(HttpStatusCode.NoContent)
    }
}