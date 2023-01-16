package day.gooddaybad.routes

import day.gooddaybad.db
import day.gooddaybad.db.Person
import day.gooddaybad.db.PromptPack
import day.gooddaybad.db.allPacks
import day.gooddaybad.db.inactivePeople
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable

private const val stewardPassword = "i am steward"

@Serializable
data class PersonPacks(val person: Person, val packs: List<PromptPack>)

fun Route.stewardRoutes() {
    get("steward") {
        if (!steward()) return@get

        val people = db.inactivePeople

        call.respond(people.map {
            PersonPacks(it.also { it.password = null }, db.allPacks(it.id!!))
        })
    }
    get("steward/trust/{id}/{trust}") {
        if (!steward()) return@get

        val person = db.document(Person::class, call.parameters["id"]!!)
        val trust = call.parameters["trust"]!!.toBoolean()

        if (person == null) {
            call.respond(mapOf("error" to "Person not found"))
            return@get
        }

        if ((person.active == true) == trust) {
            call.respond(mapOf("error" to "Person is already ${if (trust) "active" else "inactive"}"))
            return@get
        }

        person.active = trust
        db.update(person)

        if (!trust) {
            db.allPacks(person.id!!).forEach { promptPack ->
                promptPack.active = false
                db.update(promptPack)
            }
        }

        call.respond(mapOf("success" to true))
    }
}

private suspend fun PipelineContext<*, ApplicationCall>.steward(): Boolean {
    if (call.parameters["password"] != stewardPassword) {
        call.respond(HttpStatusCode.NotFound)
        return false
    }

    return true
}
