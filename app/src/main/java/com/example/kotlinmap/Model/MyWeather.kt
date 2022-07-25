package com.example.kotlinmap.Model

class MyWeather constructor(
    val coord: Coord?=null,
    var weather:List<Weather>?=null,
    var base:String="",
    val main: Main?=null,
    var visiblity:Int=0,
    val wind: Wind?=null,
    val clouds: Clouds?=null,
    var dt:Int=0,
    val sys: Sys?=null,
    var timezone:Int=0,
    var id:Int=0,
    var name:String="",
    var cod:Int=0
) {
}