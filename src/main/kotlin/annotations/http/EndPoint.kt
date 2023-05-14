package annotations.http

import io.ktor.http.*
import utils.EndPointMethod

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class EndPoint(val endPointMethod: EndPointMethod, val path: String)
