package dev.justdrven

import dev.justdrven.managers.HashManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    val tokenSource = "token"
    val apiKeySource = "api_key"

    System.setProperty(tokenSource, "ahwefagwefhagwedfwahfeaw")
    System.setProperty(apiKeySource, "testAPIKey")

    println("Encode token: " + HashManager.encode("ahwefagwefhagwedfwahfeaw"))
    println("Encode api key: " + HashManager.encode("testAPIKey"))

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
    val token = auth.replaceFirst("Bearer ", "")

    val value = (auth != "null" && auth.startsWith("Bearer ") &&
            HashManager.decode(token) == System.getProperty("token"))
    if (!value)
        call.respond(HttpStatusCode.Unauthorized, "Unauthorized!")
}

suspend fun checkIfRequestHasApiKey(call: ApplicationCall) {
    val auth = HashManager.decode(call.request.headers["api_key"].toString())
    val value = (auth != "null" && auth == System.getProperty("api_key"))
    if (!value)
        call.respond(HttpStatusCode.NotFound, "Missing API Key")
}

suspend fun success(call: ApplicationCall) = call.respond(HttpStatusCode.OK, "Wow, you really got in here.")

