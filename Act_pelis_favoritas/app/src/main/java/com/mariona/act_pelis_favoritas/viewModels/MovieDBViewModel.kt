package com.mariona.act_pelis_favoritas.viewModels

import android.content.Context
import android.content.DialogInterface
import android.graphics.Movie
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mariona.act_pelis_favoritas.models.MovieDBMovies
import com.mariona.act_pelis_favoritas.models.Movies
import com.mariona.act_pelis_favoritas.retrofit.Connection
import kotlinx.coroutines.launch

class MovieDBViewModel: ViewModel() {

    private val _moviesListLoading = MutableLiveData(false)
    val moviesListLoading: LiveData<Boolean> get() = _moviesListLoading

    private val _movies = MutableLiveData<List<MovieDBMovies>>(emptyList())
    val movies: LiveData<List<MovieDBMovies>> get() = _movies

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadMovies(query: String) {
        viewModelScope.launch {
            _moviesListLoading.value = true
            _error.value = null

            val resposta = Connection.movieDBService.searchMovies(query, "es-ES", "title", 1)

            if (resposta.isSuccessful) {
                val results = resposta.body()
                _movies.value = results!!.results
            } else {
                _error.value = "ERROR CODE: ${resposta.code()}"
            }

            _moviesListLoading.value = false
        }
    }

    fun addFavorite(movie: MovieDBMovies, context: Context) {
        _error.value = null
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Afexir pelicula")
        dialog.setMessage("Escriu la nova puntuació de la pel·lícula")
        dialog.setView(input)
        dialog.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val points = input.text.toString().toLong()
            if (points in 1..10) {
                val updatedMovie = Movies(
                    movie.adult, movie.backdropPath, movie.favorite, movie.genreIDS, movie.id,
                    movie.originalLanguage, movie.originalTitle, movie.overview, movie.popularity,
                    movie.posterPath, movie.releaseDate, movie.title, movie.video, movie.voteAverage,
                    movie.voteCount, points
                )
                viewModelScope.launch {
                    try {
                        Connection.service.newMovie(updatedMovie)
                    } catch (e: Exception) {
                        _error.value = "Error de red: ${e.message}"
                    }
                }
            } else {
                _error.value = "La puntuación debe estar entre 1 y 10"
            }
        })

        dialog.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })

        dialog.show()
    }
}

@Suppress("UNCHECKED_CAST")
class MovieDBViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDBViewModel() as T
    }
}