package controllers

import annotations.http.EndPoint
import com.github.kittinunf.fuel.Fuel
import data.MyWeatherInfo
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import utils.EndPointMethod
import utils.JsonSerializer

class WeatherController {

    @EndPoint(EndPointMethod.GET, "/weather")
    fun getWeather(): String {

        val result = Fuel.get("https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m")
            .responseString().second.body().asString("application/json")

        val jsonResponse = Json.parseToJsonElement(result).jsonObject

        val serializer = JsonSerializer();

        val weatherInfo = serializer.parseToObject<MyWeatherInfo>(jsonResponse)

        return weatherInfo.toString()
    }
}