package io.github.lordraydenmk.http

import io.github.lordraydenmk.data.TodoInMemoryRepository
import io.github.lordraydenmk.domain.TodoItem
import io.kotest.assertions.ktor.shouldHaveContent
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class RoutesTest : FunSpec({

    val repo = TodoInMemoryRepository()

    test("get request to root - empty repo - empty list") {
        withTestApplication({ testModule(repo) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus HttpStatusCode.OK
                response shouldHaveContent "[]"
            }
        }
    }

    test("post request to root - HTTP Created") {
        withTestApplication({ testModule(repo) }) {
            handleRequest(HttpMethod.Post, "/") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(TodoItemDto(title = "My Todo")))
            }.apply {
                response shouldHaveStatus HttpStatusCode.Created
            }
        }
    }

    test("delete to root - HTTP NoContent") {
        withTestApplication({ testModule(repo) }) {
            handleRequest(HttpMethod.Delete, "/").apply {
                response shouldHaveStatus HttpStatusCode.NoContent
            }
        }
    }

    test("get by ID - ID exists - returns the todo") {
        val id = UUID.randomUUID()
        with(repo) {
            deleteAll()
            createTodo(TodoItem(id, "My Todo"))
        }
        withTestApplication({ testModule(repo) }) {
            handleRequest(HttpMethod.Get, "/$id").apply {
                response shouldHaveStatus HttpStatusCode.OK
                response shouldHaveContent """{"title":"My Todo","completed":false,"url":"http://localhost:80/$id","order":null}"""
            }
        }
    }

    test("get by ID - ID does NOT exist - HTTP NotFound") {
        repo.deleteAll()
        withTestApplication({ testModule(repo) }) {
            handleRequest(HttpMethod.Get, "/${UUID.randomUUID()}").apply {
                response shouldHaveStatus HttpStatusCode.NotFound
            }
        }
    }

    test("getById - invalid route id - HTTP BadRequest") {
        withTestApplication({ testModule(repo) }) {
            handleRequest(HttpMethod.Get, "/not-a-valid-UUID").apply {
                response shouldHaveStatus HttpStatusCode.BadRequest
            }
        }
    }
})