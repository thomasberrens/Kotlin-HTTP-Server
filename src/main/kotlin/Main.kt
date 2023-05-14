import annotations.http.EndPoint
import controllers.PersonController
import controllers.WeatherController
import data.Person
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import utils.EndPointMethod
import utils.JsonSerializer
import utils.toHttpMethod
import kotlin.io.println
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation


val controllers = ArrayList<KClass<*>>()

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    controllers.add(WeatherController::class)
    controllers.add(PersonController::class)

    val server = embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)

    println("Server started")

}

fun Application.module() {

    routing {
        get("/") {
            println(call.receiveText())
            call.respondText("Hello, world!")
        }

        // TODO: make this a seperate function
        controllers.forEach {controller ->
            val instance = controller.createInstance();

            controller.declaredMemberFunctions.forEach {function ->

                if(function.hasAnnotation<EndPoint>()) {
                    val annotation = function.findAnnotation<EndPoint>() ?: return@forEach

                    addRoute(annotation.endPointMethod, annotation.path) {

                        val parameterObjects = initializeParameters(function.parameters, call.receiveText())

                       val result = function.call(instance, *parameterObjects.toTypedArray())

                        if (isVoidReturnType(function)) {
                            call.respondText("[]")
                        } else {
                            call.respondText(result.toString())
                        }

                        }
                    }
                }
            }
        }
    }

fun initializeParameters(parameters: List<KParameter>, receivedText: String): ArrayList<Any> {

    val parameterObjects = ArrayList<Any>()

    if (parameters.size <= 1) return parameterObjects

    // we only accept one parameter (for now), that is the request body
    val parameter = parameters[1]

    val receivedJson = Json.parseToJsonElement(receivedText).jsonObject

    println(receivedJson)

    val initializedParameter = JsonSerializer().fillObject(parameter.type.classifier as KClass<*>, receivedJson)

    println(initializedParameter)

    parameterObjects.add(initializedParameter)


    return parameterObjects
}

fun isVoidReturnType(function: KCallable<*>): Boolean {
    return function.returnType.classifier == Unit::class
}

fun Route.addRoute(method: EndPointMethod, path: String, handler: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit) {
    route(path, method.toHttpMethod()) {
        handle {
            handler.invoke(this, Unit)
        }
    }
}