package dev.justdrven

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    System.setProperty("token", "ahwefagwefhagwedfwahfeaw")
    System.setProperty("api_key", "testAPIKey")
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Routing \n" +
                    " - /auth | Testing auth \n" +
                    " - /auth/api_key | Testing auth with api key")
        }
        get("/auth") {
            checkIfRequestAuthorization(call)

            success(call)
        }
        get("/auth/api_key") {
            checkIfRequestAuthorization(call)
            checkIfRequestHasApiKey(call)

            success(call)
        }
    }
}

suspend fun checkIfRequestAuthorization(call: ApplicationCall) {
    val auth = call.request.authorization().toString()
    val value = (auth != "null" && auth == "Bearer ${System.getProperty("token")}")
    if (!value)
        call.respond(HttpStatusCode.Unauthorized, "Unauthorized!")
}

suspend fun checkIfRequestHasApiKey(call: ApplicationCall) {
    val auth = call.request.headers["api_key"]
    val value = (auth != "null" && auth.equals(System.getProperty("api_key")))
    if (!value)
        call.respond(HttpStatusCode.NotFound, "Missing API Key")
}

suspend fun success(call: ApplicationCall) = call.respond(HttpStatusCode.OK, "Wow, you really got in here.")

