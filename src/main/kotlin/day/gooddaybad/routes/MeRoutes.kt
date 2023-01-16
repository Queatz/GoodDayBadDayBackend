package day.gooddaybad.routes

import day.gooddaybad.db
import day.gooddaybad.db.Person
import day.gooddaybad.db.allPacks
import day.gooddaybad.db.me
import day.gooddaybad.me
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class MePostBody(val password: String)

fun Route.meRoutes() {
    authenticate {
        get("/me") {
            call.respond(me())
        }
        get("/me/packs") {
            call.respond(db.allPacks(me().id!!))
        }
        post("/me") {
            val meUpdate = call.receive<Person>()
            val me = me()

            if (meUpdate.name != null) {
                me.name = meUpdate.name
                call.respond(db.update(me))
            } else {
                call.respond(HttpStatusCode.BadRequest.description("Nothing to do"))
            }
        }
    }

    post("/connect") {
        val password = call.receive<MePostBody>().password
        val me = db.me(password) ?: db.insert(Person(password = password))
        call.respond(me)
    }
}
