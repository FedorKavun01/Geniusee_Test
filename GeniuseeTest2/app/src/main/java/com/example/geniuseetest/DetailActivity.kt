package com.example.geniuseetest

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    lateinit var ivDetailPoster: ImageView
    lateinit var tvGenres: TextView
    lateinit var tvReleasedDate: TextView
    lateinit var tvFullDescription: TextView
    lateinit var tvDirectors: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail)
        fillActivity()
    }

    fun fillActivity(): Unit {
        val id: Long = intent.extras?.get("id") as Long
        val itemFilm = ItemFilm.findItem(id)
        if(itemFilm == null) {
            Toast.makeText(this, "ERROR WITH ID", Toast.LENGTH_LONG).show()
            this.finish()
        }
        var detailMovie = APIRequest().getDetails(id, itemFilm)

        ivDetailPoster = findViewById(R.id.ivMovieDetailPoster)
        tvGenres = findViewById(R.id.tvGenres)
        tvReleasedDate = findViewById(R.id.tvReleaseDate)
        tvFullDescription = findViewById(R.id.tvFullDescription)
        tvDirectors = findViewById(R.id.tvDirectors)

        ivDetailPoster.setImageBitmap(detailMovie.poster)
        tvGenres.setText(detailMovie.genres)
        tvReleasedDate.setText(detailMovie.releaseDate)
        tvFullDescription.setText(detailMovie.description)
        tvDirectors.setText(detailMovie.directors)
    }
}