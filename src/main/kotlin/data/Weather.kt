package data

import annotations.Serializable

@Serializable
data class Weather(
    val temperature: Float?,
    val windSpeed: Float?,
    val weatherCode: Int?
) {
    constructor() : this(null, null, null)

    override fun toString(): String {
        return "{\n" +
                "\"temperature\": $temperature,\n" +
                "\"windSpeed\": $windSpeed,\n" +
                "\"weatherCode\": $weatherCode\n" +
                "}"
    }
}
