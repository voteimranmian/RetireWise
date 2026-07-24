package com.retirewise.authentication

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

/**
 * Health-check-only skeleton for the Phase 3 backend account service
 * (docs/RELEASE_PLAN.md). Proves the AWS deploy pipeline (see infra/) works
 * end to end; it does not implement any real authentication, session, or
 * consent logic yet — that is explicitly deferred to the next slice, once
 * this is deployable and real OAuth credentials exist.
 */
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/health") {
            call.respond(HealthResponse(status = "ok"))
        }
    }
}

@Serializable
data class HealthResponse(val status: String)
