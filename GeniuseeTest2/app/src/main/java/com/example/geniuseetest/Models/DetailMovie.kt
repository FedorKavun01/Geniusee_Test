package com.example.geniuseetest.Models

//Detail information about movie
data class DetailMovie(var itemFilm: ItemFilm?, var genres: String, var releaseDate: String, var directors: String) {
    var poster = itemFilm?.poster
    var name = itemFilm?.name
    var description = itemFilm?.description
}