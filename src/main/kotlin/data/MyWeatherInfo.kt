package data

import annotations.ReadAs
import annotations.Serializable

@Serializable
data class MyWeatherInfo(
    val latitude: Float? = null,
    val longitude: Float? = null,
    val timeZone: String? = null,
    val elevation: Float? = null,

    @field:ReadAs("generationtime_ms") val generationTime: Double? = null,
    @field:ReadAs("utc_offset_seconds") val utcOffsetSeconds: Int? = null,
    @field:ReadAs("current_weather") val currentWeather: Weather? = null,
) {
    override fun toString(): String {
        return "{\n" +
                "\"latitude\": $latitude,\n" +
                "\"longitude\": $longitude,\n" +
                "\"timeZone\": $timeZone,\n" +
                "\"elevation\": $elevation,\n" +
                "\"generationTime\": $generationTime,\n" +
                "\"utcOffsetSeconds\": $utcOffsetSeconds,\n" +
                "\"currentWeather\": $currentWeather\n" +
                "}"
    }
}
