package com.example.geniuseetest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream
import java.lang.Exception
import java.net.URL

data class ItemFilm(var id: String, var name: String, var description: String, var posterURL: String) {

    var poster: Bitmap? = null

    companion object {
        var allItems: ArrayList<ItemFilm> = ArrayList()
    }

    init {
        posterURL = "https://image.tmdb.org/t/p/w500$posterURL"
//        ImageBitmapGetter().execute(posterURL)
        allItems.add(this)
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