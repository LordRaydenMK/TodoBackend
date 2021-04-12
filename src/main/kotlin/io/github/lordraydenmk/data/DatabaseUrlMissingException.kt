package io.github.lordraydenmk.data

object DatabaseUrlMissingException :
    RuntimeException("Please add a DATABASE_URL env variable pointing to a PostgreSQL DB")
