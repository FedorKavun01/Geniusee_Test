package com.example.geniuseetest.Controllers

import android.util.Log
import com.example.geniuseetest.Models.DetailMovie
import com.example.geniuseetest.Models.ItemFilm
import org.json.JSONObject
import java.net.URL

class APIRequest : Runnable {
    val API_KEY: String = "870a3dab3cb1ace6052fd161ff22517d"
    val API_READACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NzBhM2RhYjNjYjFhY2U2MDUyZmQxNjFmZjIyNTE3ZCIsInN1YiI6IjVlZmY1OWZlYTI4NGViMDAzNjkyYzE4YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.bW6caQ5TQqBB11oDiQ3uCfYuapbC4R9NWScRGXNOj4k"
    val address: String = "https://api.themoviedb.org/3"
    val trending: String = "trending"
    val mediaType: String = "movie"
    val search: String = "search"
    var page: Int = 1
    var request = ""
    var thread = Thread(this)
    var result = ""

    fun getTrendsJSON(timeWindow: String): Unit {
        request = "$address/$trending/$mediaType/$timeWindow?api_key=$API_KEY&page=$page"
        if (page == 1) {
            ItemFilm.allItems.clear()
        }
        ItemFilm.allItems.addAll(JSONToItemFilmList())
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

        return DetailMovie(
            itemFilm,
            genres,
            releaseDate,
            directors
        )
    }

    fun getSearchMovie(query: String): ArrayList<ItemFilm> {
        request = "$address/$search/$mediaType?api_key=$API_KEY&query=$query&page=$page"
        if (page == 1) {
            ItemFilm.searchItems.clear()
        }
        ItemFilm.searchItems.addAll(JSONToItemFilmList())
        return ItemFilm.searchItems
    }

    private fun JSONToItemFilmList(): ArrayList<ItemFilm> {
        thread = Thread(this)
        var itemsList: ArrayList<ItemFilm> = ArrayList()
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

            itemsList.add(ItemFilm(id, name, "\t$description", poster))

            Log.d("mytag", "getTrendsJSON: " + id + " " + name + "\n " + description + "\n" + poster)
        }
        return itemsList
    }

    fun getName(items: List<String>) : String {
        var result = ""
        for (item in items) {
            var jsonObject = JSONObject(item)
            val name = jsonObject.optString("name")
            result += "$name\n"
        }
        result = result.removeRange(result.length - 1, result.length)
        return result
    }

    override fun run() {
        var url = URL(request)
        result = url.readText()
    }

}