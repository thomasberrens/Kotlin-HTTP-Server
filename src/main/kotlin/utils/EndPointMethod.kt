package utils

import io.ktor.http.*

enum class EndPointMethod {
    GET,
    POST,
    PUT,
    DELETE
}

fun EndPointMethod.toHttpMethod(): HttpMethod {
    return when (this) {
        EndPointMethod.GET -> HttpMethod.Get
        EndPointMethod.POST -> HttpMethod.Post
        EndPointMethod.PUT -> HttpMethod.Put
        EndPointMethod.DELETE -> HttpMethod.Delete
    }
}