package io.github.lordraydenmk.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object Todos : Table() {
    val todoId: Column<UUID> = uuid("todoId")
    val title: Column<String> = varchar("title", 255)
    val completed: Column<Boolean> = bool("completed")
    val order: Column<Int?> = integer("order").nullable()

    override val primaryKey = PrimaryKey(todoId)
}