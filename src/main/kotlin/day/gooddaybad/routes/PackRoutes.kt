package day.gooddaybad.routes

import day.gooddaybad.db
import day.gooddaybad.db.PromptPack
import day.gooddaybad.db.activePromptPacks
import day.gooddaybad.me
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.packRoutes() {
    get("packs") {
        call.respond(db.activePromptPacks)
    }

    get("packs/{id}") {
        call.respond(db.document(PromptPack::class, call.parameters["id"]!!) ?: HttpStatusCode.NotFound)
    }

    authenticate {
        post("packs") {
            call.respond(db.insert(PromptPack(person = me().id, prompts = emptyList())))
        }
        post("packs/{id}") {
            val promptPackUpdate = call.receive<PromptPack>()
            val promptPack = db.document(PromptPack::class, call.parameters["id"]!!)

            if (promptPack?.person != me().id) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }

            if (promptPackUpdate.active == true && me().active != true) {
                call.respond(HttpStatusCode.BadRequest.description("Connection is not yet trusted, cannot publish"))
                return@post
            }

            if (promptPack != null) {
                if (promptPackUpdate.name != null) {
                    promptPack.name = promptPackUpdate.name
                }
                if (promptPackUpdate.color != null) {
                    promptPack.color = promptPackUpdate.color
                }
                if (promptPackUpdate.author != null) {
                    promptPack.author = promptPackUpdate.author
                }
                if (promptPackUpdate.level != null) {
                    promptPack.level = promptPackUpdate.level
                }
                if (promptPackUpdate.active != null) {
                    promptPack.active = promptPackUpdate.active
                }
                if (promptPackUpdate.description != null) {
                    promptPack.description = promptPackUpdate.description
                }
                if (promptPackUpdate.prompts != null) {
                    promptPack.prompts = promptPackUpdate.prompts
                }
            }

            call.respond(db.update(promptPack!!))
        }
        post("packs/{id}/delete") {
            val promptPack = db.document(PromptPack::class, call.parameters["id"]!!)

            if (promptPack?.person != me().id) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }

            db.delete(promptPack!!)

            call.respond(HttpStatusCode.OK)
        }
    }
}
