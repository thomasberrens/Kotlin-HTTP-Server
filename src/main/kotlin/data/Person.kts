package data

import annotations.Serializable

@Serializable
data class Person(
    val name: String? = null,
    val age: Int? = null
) {
    override fun toString(): String {
        return "{\n" +
                "\"name\": $name,\n" +
                "\"age\": $age\n" +
                "}"
    }
}