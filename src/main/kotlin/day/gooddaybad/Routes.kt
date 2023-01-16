package day.gooddaybad

import day.gooddaybad.routes.meRoutes
import day.gooddaybad.routes.packRoutes
import day.gooddaybad.routes.stewardRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Application.routes() {
    routing {
        get("/hi") {
            call.respond(mapOf("hi" to true))
        }
        static("/static") {
            resources("static")
        }
        meRoutes()
        packRoutes()
        stewardRoutes()
    }
}

fun PipelineContext<*, ApplicationCall>.me() = call.principal<PersonPrincipal>()!!.person
