package com.example.geniuseetest

import android.graphics.Bitmap

data class DetailMovie(var itemFilm: ItemFilm?, var genres: String, var releaseDate: String, var directors: String) {
    var poster = itemFilm?.poster
    var name = itemFilm?.name
    var description = itemFilm?.description
}