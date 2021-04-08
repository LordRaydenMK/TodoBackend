package io.github.lordraydenmk.data

import io.github.lordraydenmk.domain.TodoId
import io.github.lordraydenmk.domain.TodoItem
import io.github.lordraydenmk.domain.TodoRepository

class TodoInMemoryRepository : TodoRepository {

    private val todos: MutableMap<TodoId, TodoItem> = mutableMapOf()

    override suspend fun getAll(): List<TodoItem> = todos.values.toList()

    override suspend fun createTodo(todoItem: TodoItem): TodoItem = synchronized(this) {
            todoItem.also { todos[todoItem.id] = todoItem }
        }

    override suspend fun deleteAll() {
        todos.clear()
    }

    override suspend fun getById(id: TodoId): TodoItem? = synchronized(this) {
        todos[id]
    }

    override suspend fun deleteById(id: TodoId) {
        todos.remove(id)
    }

    override suspend fun updateTodo(id: TodoId, todo: TodoItem): TodoItem {
        todos.remove(id)
        todos[todo.id] = todo
        return todo
    }
}