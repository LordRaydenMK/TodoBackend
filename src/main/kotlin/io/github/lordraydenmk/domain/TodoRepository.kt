package io.github.lordraydenmk.domain

interface TodoRepository {

    suspend fun getAll(): List<TodoItem>

    suspend fun createTodo(todoItem: TodoItem): TodoItem

    suspend fun deleteAll()

    suspend fun getById(id: TodoId): TodoItem?

    suspend fun deleteById(id: TodoId)

    suspend fun updateTodo(id: TodoId, todo: TodoItem): TodoItem
}
