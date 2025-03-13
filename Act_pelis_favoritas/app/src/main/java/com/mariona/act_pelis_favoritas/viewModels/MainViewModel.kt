package com.mariona.act_pelis_favoritas.viewModels

import android.content.Context
import android.content.Intent
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariona.act_pelis_favoritas.models.Movie
import com.mariona.act_pelis_favoritas.retrofit.MovieDbConnection
import kotlinx.coroutines.launch
import okhttp3.Response
import java.io.IOException

class MainViewModel : ViewModel() {

    var titleASC: Boolean = true
    var titleDESC: Boolean = false
    var myscoreASC: Boolean = false
    var myscoreDESC: Boolean = false

    private val _moviesListLoading = MutableLiveData(false)
    val moviesListLoading: LiveData<Boolean> get() = _moviesListLoading

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    val movies: LiveData<List<Movie>> get() = _movies

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _moviesListLoading.value = true
            _error.value = null

            try {
                lateinit var resposta: Response<List<Movie>>

                if (titleASC) {
                    resposta = MovieDbConnection.service.listMovies("title", "asc")
                } else if (titleDESC) {
                    resposta = MovieDbConnection.service.listMovies("title", "desc")
                } else if (myscoreASC) {
                    resposta = MovieDbConnection.service.listMovies("my_score", "asc")
                } else if (myscoreDESC) {
                    resposta = MovieDbConnection.service.listMovies("my_score", "desc")
                }

                if (resposta.isSuccessful) {
                    _movies.value = resposta.body()
                } else {
                    _error.value = "ERROR CODE: ${resposta.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Unknown error: ${e.message}"
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _moviesListLoading.value = false
            }
        }
    }

    fun filterMovie(){
        val filterMovies = _movies.value?.filter { it.favorite }
        _movies.value = filterMovies
    }

    fun OnMovieClicked(movie: Movie, context: Context){
        val i = Intent(context, MovieDetailActivity::class.java)
        i.putExtra("movie", movie)
        context.startActivity(i)
    }

    fun onMovieUpdated(movie: Movie, context: Context){
        _error.value = null
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        var dialog = AlertDialog.Builder(context)
        dialog.setTitle("Actualitzar pelicula")
        dialog.setMessage("Escribe la nueva puntuación")
        dialog.setView(input)
        dialog.setPositiveButton("Aceptar") { _, _ ->
            var points = input.text.toString().toLong()
            movie.myScore = points
            if(points.toInt() in 1..10){
                viewModelScope.launch {
                    MovieDbConnection
                }
            } else {
                _error.value = "La puntuación debe estar entre 1 y 10"

            }
        }
    }
}