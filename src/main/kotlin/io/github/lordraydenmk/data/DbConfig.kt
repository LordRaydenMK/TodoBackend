package io.github.lordraydenmk.data

data class DbConfig(
    val url: String,
    val username: String,
    val password: String
)

/**
 * Maps the DATABASE_URL from Heroku to [DbConfig] suitable for Exposed
 */
fun parseUrl(databaseUrl: String): DbConfig {
    val startIndex = 11
    val secondColonIndex = databaseUrl.indexOf(':', startIndex)
    val atIndex = databaseUrl.indexOf('@')
    val username = databaseUrl.substring(startIndex, secondColonIndex)
    val pass = databaseUrl.substring(secondColonIndex + 1, atIndex)
    val actualUrl = databaseUrl.substring(atIndex + 1)
    return DbConfig(actualUrl, username, pass)
}