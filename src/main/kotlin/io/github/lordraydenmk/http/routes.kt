package io.github.lordraydenmk.http

import io.github.lordraydenmk.data.TodoInMemoryRepository
import io.github.lordraydenmk.domain.PatchTodo
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
    get("/{id}") {
        val id = call.parameters["id"]?.toUUIDOrNull()!!
        repo.getById(id)?.let { call.respond(it.toDto(urlBuilder())) } ?: call.respond(HttpStatusCode.NotFound)
    }
    post("/") {
        val payload = call.receive<TodoItemDto>()
        if (payload.title != null) {
            val todoItem = TodoItem(UUID.randomUUID(), payload.title, payload.completed, payload.order)
            call.respond(HttpStatusCode.Created, repo.createTodo(todoItem).toDto(urlBuilder()))
        } else call.respond(HttpStatusCode.BadRequest)
    }
    patch("/{id}") {
        val id = call.parameters["id"]?.toUUIDOrNull()!!
        val payload = call.receive<TodoItemDto>()
        val patch = PatchTodo(payload.title, payload.completed, payload.order)
        val updated = repo.getById(id)?.patch(patch)
        if (updated != null) call.respond(repo.updateTodo(id, updated).toDto(urlBuilder()))
        else call.respond(HttpStatusCode.NotFound)
    }
    delete("/") {
        repo.deleteAll()
        call.respond(HttpStatusCode.NoContent)
    }
    delete("/{id}") {
        val id = call.parameters["id"]?.toUUIDOrNull()!!
        repo.deleteById(id)
        call.respond(HttpStatusCode.NoContent)
    }
}