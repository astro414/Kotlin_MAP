package com.example.kotlinmap.Api

import com.example.kotlinmap.Model.MyWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {

    @Headers(
        "x-rapidapi-host: community-open-weather-map.p.rapidapi.com",
        "x-rapidapi-key: 506ed5160dmsh69b126295449edcp131494jsnc921c41673a9"
    )
    @GET("weather")
    fun getWeather(
        @Query("q") cityname:String,

        ): Call<MyWeather>
}