package com.example.geniuseetest.Models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream
import java.lang.Exception
import java.net.URL

data class ItemFilm(var id: Long, var name: String, var description: String, var posterURL: String) {

    var poster: Bitmap? = null

    companion object {
        var allItems: ArrayList<ItemFilm> = ArrayList()
        var searchItems: ArrayList<ItemFilm> = ArrayList()

        fun findItem(id: Long): ItemFilm? {
            var item = allItems.find { it.id == id }
            if (item == null) {
                item = searchItems.find { it.id == id }
            }
            return item
        }
    }

    init {
        posterURL = "https://image.tmdb.org/t/p/w500$posterURL"
    }


    class ImageBitmapGetter2(var imgView: ImageView, var item: ItemFilm) : AsyncTask<String, Void, Bitmap?>() {

        override fun doInBackground(vararg params: String?): Bitmap? {
            var url = params[0]
            var bitmap: Bitmap? = null
            try {
                val inputStream: InputStream = URL(url).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                Log.d("mytage", "doInBackground: " + e)
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            imgView.setImageBitmap(result)
            item.poster = result
        }
    }


//    inner class ImageBitmapGetter : AsyncTask<String, Void, Bitmap?>() {
//        override fun doInBackground(vararg params: String?): Bitmap? {
//            var url = params[0]
//            var bitmap: Bitmap? = null
//            try {
//                val inputStream: InputStream = URL(url).openStream()
//                bitmap = BitmapFactory.decodeStream(inputStream)
//            } catch (e: Exception) {
//                Log.d("mytage", "doInBackground: " + e)
//            }
//            return bitmap
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//            super.onPostExecute(result)
//            poster = result
//        }
//    }
}