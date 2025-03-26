package com.mariona.act_pelis_favoritas.viewModels

import android.content.Context
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariona.act_pelis_favoritas.models.MovieDBMovies
import com.mariona.act_pelis_favoritas.models.Movies
import com.mariona.act_pelis_favoritas.retrofit.Connection
import kotlinx.coroutines.launch

class MovieDBViewModel: ViewModel(){

    private val _moviesListLoading = MutableLiveData(false)
    val moviesListLoading: LiveData<Boolean> get() = _moviesListLoading

    private val _movies = MutableLiveData<List<Movies>>(emptyList())
    val movies: LiveData<List<Movies>> get() = _movies

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    public fun loadMovies() {
        viewModelScope.launch {
            _moviesListLoading.value = true
            _error.value = null

            var resposta = Connection.movieDBService.searchMovies(query, "es-ES", "title", 1)

            if (resposta.isSuccessful) {
                var results = resposta.body()
                _movies.value = results!!.results
            } else {
                _error.value = "ERROR CODE: ${resposta.code()}"
            }

            _moviesListLoading.value = false
        }
    }

    public fun addFavorite(movie: MovieDBMovies, context: Context) {
        _error.value = null
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        var dialog = AlertDialog.Builder(context)

    }
}