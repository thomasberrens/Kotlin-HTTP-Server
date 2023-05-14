package utils

import annotations.ReadAs
import annotations.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaField

class JsonSerializer {

    inline fun <reified T> parseToObject(json: JsonObject): T {
        return fillObject(T::class, json) as T;
    }

     fun fillObject(kClass: KClass<*>, json: JsonObject): Any {

         val properties = kClass.memberProperties

         val deserializedInstance = kClass.createInstance()

         properties.forEach { kCallable ->


             var propertyName = kCallable.name.lowercase(Locale.getDefault())

             if (kCallable.javaField?.isAnnotationPresent(ReadAs::class.java)!!) {

                 val readAs = kCallable.javaField?.getAnnotation(ReadAs::class.java)?.name ?: return@forEach

                 propertyName = readAs.lowercase(Locale.getDefault())

                 println("dealt with readAs annotation, new propertyname: $propertyName")
             }

             val propertyType = kCallable.returnType

             try {
                 val jsonProperty = getJsonProperty(propertyName, json)

                 val stringifiedProperty = jsonProperty.toString();


                 val property = deserializedInstance!!::class.java.getDeclaredField(kCallable.name)
                 property.isAccessible = true

                 val decodedProperty = decodeFromString(propertyType, stringifiedProperty)

                 property.set(deserializedInstance, decodedProperty)


             } catch (e: Exception) {
                 println(e.message)
             }

         }

         return deserializedInstance
     }

    fun getJsonProperty(propertyName: String, json: JsonObject): JsonElement {

       val property = json.filter { it.key.lowercase(Locale.getDefault()) == propertyName }

        if (property.isEmpty()) {
            throw Exception("Property $propertyName not found in json object")
        }

        return property.values.first()
    }

    // TODO: add support for arrays
    fun decodeFromString(propertyType: KType, stringifiedProperty: String): Any {
        if (propertyType.classifier is KClass<*>) {

            val clazz = propertyType.classifier as KClass<*>

            if (clazz.hasAnnotation<Serializable>()) {
                println("We deal with a nested class")

                val jsonObject = Json.parseToJsonElement(stringifiedProperty).jsonObject

                return fillObject(clazz, jsonObject)
            }
        }

        return when (propertyType.classifier) {
            Int::class -> stringifiedProperty.toInt()
            Float::class -> stringifiedProperty.toFloat()
            String::class -> stringifiedProperty
            Double::class -> stringifiedProperty.toDouble()
            else -> throw Exception("Property type $propertyType not supported")
        }
    }
}