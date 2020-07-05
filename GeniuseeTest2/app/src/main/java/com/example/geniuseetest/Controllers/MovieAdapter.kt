package com.example.geniuseetest.Controllers

import android.app.Service
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.geniuseetest.Models.ItemFilm
import com.example.geniuseetest.R

class MovieAdapter(ctx: Context) : BaseAdapter(), Filterable {

    var layoutInflater: LayoutInflater = ctx.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var movieFilter: MovieFilter? = null
    var filteredList: ArrayList<ItemFilm> = ItemFilm.allItems

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var filmItem: ItemFilm = getItem(position) as ItemFilm

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item, parent, false)
            view as View
        }

        if (filmItem.poster == null) {
            ItemFilm.ImageBitmapGetter2(view.findViewById<ImageView>(R.id.poster), filmItem).execute(filmItem.posterURL)
        } else{
            view.findViewById<ImageView>(R.id.poster).setImageBitmap(filmItem.poster)
        }

        view.findViewById<TextView>(R.id.tvName).setText(filmItem.name)
        view.findViewById<TextView>(R.id.tvShortDescription).setText(filmItem.description)
        Log.d("mytesttag", "getView: " + (view.findViewById(R.id.tvShortDescription) as TextView).text)

        return view
    }

    override fun getItem(position: Int): Any {
        return filteredList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return filteredList.get(position).id
    }

    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        if (movieFilter == null) {
            movieFilter = MovieFilter()
        }
        return movieFilter as MovieFilter
    }

    inner class MovieFilter: Filter() {

        var apiRequest = APIRequest()

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filterResults = FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                var constraint0 = constraint.toString().toLowerCase()
                var tmpList: ArrayList<ItemFilm> = apiRequest.getSearchMovie(constraint0)

                filterResults.count = tmpList.size
                filterResults.values = tmpList
            } else {
                filterResults.count = ItemFilm.allItems.size
                filterResults.values = ItemFilm.allItems
            }

            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null) {
                filteredList = results.values as ArrayList<ItemFilm>
            }
            notifyDataSetChanged()

        }
    }

}