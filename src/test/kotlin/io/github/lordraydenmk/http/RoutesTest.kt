package io.github.lordraydenmk.http

import io.github.lordraydenmk.data.TodoInMemoryRepository
import io.kotest.assertions.ktor.shouldHaveContent
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
})