package io.github.lordraydenmk.data

import io.github.lordraydenmk.domain.TodoPatch
import io.github.lordraydenmk.domain.TodoId
import io.github.lordraydenmk.domain.TodoItem
import io.github.lordraydenmk.domain.TodoRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TodoExposedRepository : TodoRepository {

    init {
        val dbUrl = System.getenv("DATABASE_URL") ?: throw DatabaseUrlMissingException
        val dbConfig = parseUrl(dbUrl)
        Database.connect(
            url = "jdbc:postgresql://${dbConfig.url}",
            driver = "org.postgresql.Driver",
            user = dbConfig.username,
            password = dbConfig.password
        )
        transaction {
            addLogger(Slf4jSqlDebugLogger)
            SchemaUtils.create(Todos)
        }
    }

    override suspend fun getAll(): List<TodoItem> = transaction {
        Todos.selectAll()
            .map(::asTodoItem)
    }

    private fun asTodoItem(row: ResultRow): TodoItem =
        TodoItem(
            row[Todos.todoId],
            row[Todos.title],
            row[Todos.completed],
            row[Todos.order]
        )

    override suspend fun createTodo(todoItem: TodoItem): TodoItem {
        transaction {
            Todos.insert {
                it[todoId] = todoItem.id
                it[title] = todoItem.title
                it[completed] = todoItem.completed
                it[order] = todoItem.order
            }
        }
        return todoItem
    }

    override suspend fun deleteAll() {
        transaction { Todos.deleteAll() }
    }

    override suspend fun getById(id: TodoId): TodoItem? = transaction {
        Todos.select { Todos.todoId eq id }
            .map(::asTodoItem)
            .also { check(it.size <= 1) { "Should contain 0-1 rows" } }
            .firstOrNull()
    }

    override suspend fun deleteById(id: TodoId) {
        val delete = transaction { Todos.deleteWhere { Todos.todoId eq id } }
        check(delete == 1) { "Should delete exactly 1 row" }
    }

    override suspend fun updateTodo(id: TodoId, todo: TodoPatch): TodoItem? {
        val update = transaction {
            Todos.update({ Todos.todoId eq id }) {
                if (todo.title != null) it[title] = todo.title
                if (todo.completed != null) it[completed] = todo.completed
                it[order] = todo.order
            }
        }
        check(update <= 1) { "Should update 0-1 rows" }
        return getById(id)
    }
}