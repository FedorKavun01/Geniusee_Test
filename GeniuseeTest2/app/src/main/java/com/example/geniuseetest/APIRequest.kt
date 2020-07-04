package com.example.geniuseetest

import android.util.Log
import org.json.JSONObject
import java.net.URL

class APIRequest : Runnable {
    val API_KEY: String = "870a3dab3cb1ace6052fd161ff22517d"
    val API_READACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NzBhM2RhYjNjYjFhY2U2MDUyZmQxNjFmZjIyNTE3ZCIsInN1YiI6IjVlZmY1OWZlYTI4NGViMDAzNjkyYzE4YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.bW6caQ5TQqBB11oDiQ3uCfYuapbC4R9NWScRGXNOj4k"
    val address: String = "https://api.themoviedb.org/3/trending"
    val mediaType: String = "movie"
    var timeWindow: String = "day"
    var request = "$address/$mediaType/$timeWindow?api_key=$API_KEY"
    var result = ""

    fun getTrendsJSON(): Unit {
        var thread = Thread(this)
        thread.start()
        thread.join()
        Log.d("mytag", "getTrendsJSON: " + result)
        var jsonObject: JSONObject = JSONObject(result)
        var films = jsonObject.optJSONArray("results").let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
        Log.d("mytag", "getTrendsJSON: " + films)
        for (film in films) {
            jsonObject = JSONObject(film.toString())
            val id = jsonObject.optString("id")
            val name = jsonObject.optString("title")
            val description = jsonObject.optString("overview")
            val poster = jsonObject.optString("poster_path")

            ItemFilm(id, name, description, poster)

            Log.d("mytag", "getTrendsJSON: " + id + " " + name + "\n " + description + "\n" + poster)
        }
    }

    override fun run() {
        var url = URL(request)
        result = url.readText()
    }
}
