package com.example.geniuseetest

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView

import android.widget.SimpleAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var movieLV : ListView
    lateinit var mAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APIRequest().getTrendsJSON()
        movieLV = findViewById(R.id.movieLV)
        mAdapter = MovieAdapter(this)
        movieLV.adapter = mAdapter
        movieLV.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, DetailActivity().javaClass)
            intent.putExtra("id", id)
            startActivity(intent)
            Log.d("tagID", "onCreate: " + id)
         })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        var searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = "Type something here..."
        searchView.apply { setSearchableInfo(searchManager.getSearchableInfo(componentName)) }

//        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mAdapter.filter.filter(newText)
        return true
    }


}