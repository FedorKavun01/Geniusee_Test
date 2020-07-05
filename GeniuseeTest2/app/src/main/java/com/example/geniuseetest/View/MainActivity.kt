package com.example.geniuseetest.View

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView

import androidx.appcompat.widget.SearchView
import com.example.geniuseetest.Controllers.APIRequest
import com.example.geniuseetest.Controllers.MovieAdapter
import com.example.geniuseetest.Controllers.MyScrollListener
import com.example.geniuseetest.R

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    var apiRequestAim = "trends"
    lateinit var movieLV : ListView
    lateinit var mAdapter: MovieAdapter
    lateinit var apiRequest: APIRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiRequest = APIRequest(apiRequestAim)
        apiRequest.getTrendsJSON()
        movieLV = findViewById(R.id.movieLV)
        mAdapter = MovieAdapter(this)
        movieLV.adapter = mAdapter
        movieLV.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, DetailActivity().javaClass)
            intent.putExtra("id", id)
            startActivity(intent)
         })

        movieLV.setOnScrollListener(MyScrollListener())
    }

    //Add SearchView and Menu to select time
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        var searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = "Type something here..."
        searchView.apply { setSearchableInfo(searchManager.getSearchableInfo(componentName)) }

        searchView.setOnQueryTextListener(this)

        menu.add(0, 0, 0, "Day")
        menu.add(0, 1, 0, "Week")

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        apiRequest = APIRequest(apiRequestAim)
        when (item.itemId){
            0 -> {
                APIRequest.timeWindow = "day"
                apiRequest.getTrendsJSON()
                mAdapter.notifyDataSetChanged()
            }
            1 -> {
                APIRequest.timeWindow = "week"
                apiRequest.getTrendsJSON()
                mAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter.filter.filter(newText)
        return true
    }


}