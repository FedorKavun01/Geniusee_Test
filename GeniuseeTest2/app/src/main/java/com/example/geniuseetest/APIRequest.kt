package com.example.geniuseetest

import android.util.Log
import org.json.JSONObject
import java.net.URL

class APIRequest : Runnable {
    val API_KEY: String = "870a3dab3cb1ace6052fd161ff22517d"
    val API_READACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NzBhM2RhYjNjYjFhY2U2MDUyZmQxNjFmZjIyNTE3ZCIsInN1YiI6IjVlZmY1OWZlYTI4NGViMDAzNjkyYzE4YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.bW6caQ5TQqBB11oDiQ3uCfYuapbC4R9NWScRGXNOj4k"
    val address: String = "https://api.themoviedb.org/3"
    val trending: String = "trending"
    val mediaType: String = "movie"
    var timeWindow: String = "day"
    var request = ""
    var thread = Thread(this)
    var result = ""

    fun getTrendsJSON(): Unit {
        request = "$address/$trending/$mediaType/$timeWindow?api_key=$API_KEY"
        thread.start()
        thread.join()
        Log.d("mytag", "getTrendsJSON: " + result)
        var jsonObject: JSONObject = JSONObject(result)
        var films = jsonObject.optJSONArray("results").let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
        Log.d("mytag", "getTrendsJSON: " + films)
        for (film in films) {
            jsonObject = JSONObject(film.toString())
            val id = jsonObject.optLong("id")
            val name = jsonObject.optString("title")
            val description = jsonObject.optString("overview")
            val poster = jsonObject.optString("poster_path")

            ItemFilm(id, name, description, poster)

            Log.d("mytag", "getTrendsJSON: " + id + " " + name + "\n " + description + "\n" + poster)
        }
    }

    fun getDetails(id: Long, itemFilm: ItemFilm?): DetailMovie {
        request = "$address/$mediaType/$id?api_key=$API_KEY"
        thread.start()
        thread.join()
        Log.d("mytag", "getDetails: " + result)
        var jsonObject = JSONObject(result)
        val genresList = jsonObject.optJSONArray("genres").let { 0.until(it.length()).map { i -> it.optString(i) } }
        val directorsList = jsonObject.optJSONArray("production_companies").let { 0.until(it.length()).map { i -> it.optString(i) } }
        val releaseDate = jsonObject.optString("release_date")

        val genres = getName(genresList)
        val directors = getName(directorsList)

        return DetailMovie(itemFilm, genres, releaseDate, directors)
    }

    fun getName(items: List<String>) : String {
        var result = ""
        for (item in items) {
            var jsonObject = JSONObject(item)
            val name = jsonObject.optString("name")
            result += "$name\n"
        }
        return result
    }

    override fun run() {
        var url = URL(request)
        result = url.readText()
    }
}
