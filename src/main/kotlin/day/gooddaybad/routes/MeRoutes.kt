package day.gooddaybad.routes

import day.gooddaybad.db
import day.gooddaybad.db.Person
import day.gooddaybad.me
import day.gooddaybad.randomToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.meRoutes() {
    authenticate {
        /**
         * Returns the authenticated person
         */
        get("/me") {
            call.respond { me() ?: HttpStatusCode.NotFound }
        }
    }

    /**
     * Creates a new person
     */
    post("/me") {
        call.respond {
            db.insert(Person(token = randomToken()))
        }
    }
}
