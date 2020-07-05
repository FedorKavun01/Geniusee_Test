package com.example.geniuseetest.Controllers

import android.util.Log
import com.example.geniuseetest.Models.DetailMovie
import com.example.geniuseetest.Models.ItemFilm
import org.json.JSONObject
import java.net.URL

class APIRequest(var aim: String) : Runnable {
    var totalPages: Int = 1
    private val API_KEY: String = "870a3dab3cb1ace6052fd161ff22517d"
    private val address: String = "https://api.themoviedb.org/3"
    private val trending: String = "trending"
    private val mediaType: String = "movie"
    private val search: String = "search"
    var page: Int = 1
    private var request = ""
    private var thread = Thread(this)
    var query = ""
    private var result = ""

    //For infinite list
    init {
        MyScrollListener.apiRequest = this
    }

    //To see trends of day
    //Can be change in optionsMenu
    companion object {
        var timeWindow: String = "day"
    }

    //Fill list of trends
    fun getTrendsJSON(): Unit {
        request = "$address/$trending/$mediaType/$timeWindow?api_key=$API_KEY&page=$page"
        if (page == 1) {
            ItemFilm.allItems.clear()
        }
        ItemFilm.allItems.addAll(JSONToItemFilmList())
    }

    //Get detail movie and create object
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

    //Get list of search results
    fun getSearchMovie(): ArrayList<ItemFilm> {
        request = "$address/$search/$mediaType?api_key=$API_KEY&query=$query&page=$page"
        if (page == 1) {
            ItemFilm.searchItems.clear()
        }
        ItemFilm.searchItems.addAll(JSONToItemFilmList())
        return ItemFilm.searchItems
    }

    //JSON data to ArrayList of ItemFilm
    private fun JSONToItemFilmList(): ArrayList<ItemFilm> {
        thread = Thread(this)
        var itemsList: ArrayList<ItemFilm> = ArrayList()
        thread.start()
        thread.join()

        var jsonObject: JSONObject = JSONObject(result)
        totalPages = jsonObject.optInt("total_pages")
        var films = jsonObject.optJSONArray("results").let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }

        for (film in films) {
            jsonObject = JSONObject(film.toString())
            val id = jsonObject.optLong("id")
            val name = jsonObject.optString("title")
            val description = jsonObject.optString("overview")
            val poster = jsonObject.optString("poster_path")

            itemsList.add(ItemFilm(id, name, "\t$description", poster))
        }
        return itemsList
    }

    //Get title of movie
    private fun getName(items: List<String>) : String {
        var result = ""
        for (item in items) {
            var jsonObject = JSONObject(item)
            val name = jsonObject.optString("name")
            result += "$name\n"
        }
        if (result.length > 1) {
            result = result.removeRange(result.length - 1, result.length)
        }
        return result
    }

    //To read JSON from API
    override fun run() {
        var url = URL(request)
        result = url.readText()
    }

}
