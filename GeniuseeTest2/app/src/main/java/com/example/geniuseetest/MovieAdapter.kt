package com.example.geniuseetest

import android.app.Service
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

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
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filterResults = FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                var tmpList: ArrayList<ItemFilm> = ArrayList()

                var constraint0 = constraint.toString().toLowerCase()
                for (itemFilm in ItemFilm.allItems) {
                    if (itemFilm.name.toLowerCase().contains(constraint0)) {   //add description
                        tmpList.add(itemFilm)
                    }
                }

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