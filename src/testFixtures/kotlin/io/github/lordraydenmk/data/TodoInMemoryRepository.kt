package io.github.lordraydenmk.data

import io.github.lordraydenmk.domain.TodoPatch
import io.github.lordraydenmk.domain.TodoId
import io.github.lordraydenmk.domain.TodoItem
import io.github.lordraydenmk.domain.TodoRepository
import io.github.lordraydenmk.domain.patch

class TodoInMemoryRepository : TodoRepository {

    private val todos: MutableMap<TodoId, TodoItem> = mutableMapOf()

    override suspend fun getAll(): List<TodoItem> = todos.values.toList()

    override suspend fun createTodo(todoItem: TodoItem): TodoItem = todoItem.also { todos[todoItem.id] = todoItem }

    override suspend fun deleteAll() {
        todos.clear()
    }

    override suspend fun getById(id: TodoId): TodoItem? = todos[id]

    override suspend fun deleteById(id: TodoId) {
        todos.remove(id)
    }

    override suspend fun updateTodo(id: TodoId, todo: TodoPatch): TodoItem? =
        todos[id]?.patch(todo)?.also { todos[id] = it }
}