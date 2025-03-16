package com.mariona.act_pelis_favoritas.viewModels

import android.content.Context
import android.content.Intent
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.mariona.act_pelis_favoritas.models.Movie
import com.mariona.act_pelis_favoritas.retrofit.MovieDbConnection
import kotlinx.coroutines.launch
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
                val response = when {
                    titleASC -> MovieDbConnection.service.listMovies("title", "asc")
                    titleDESC -> MovieDbConnection.service.listMovies("title", "desc")
                    myscoreASC -> MovieDbConnection.service.listMovies("my_score", "asc")
                    myscoreDESC -> MovieDbConnection.service.listMovies("my_score", "desc")
                    else -> null
                }

                response?.let {
                    if (it.isSuccessful) {
                        _movies.value = it.body()
                    } else {
                        _error.value = "ERROR CODE: ${it.code()}"
                    }
                }
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Unknown error: ${e.message}"
            } finally {
                _moviesListLoading.value = false
            }
        }
    }

    fun filterMovie() {
        _movies.value = _movies.value?.filter { it.favorite }
    }

    fun onMovieClicked(movie: Movie, context: Context) {
        val intent = Intent(context, MovieDetailsActivity::class.java).apply {
            putExtra("movie", movie)
        }
        context.startActivity(intent)
    }

    fun onMovieUpdated(movie: Movie, context: Context) {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        AlertDialog.Builder(context).apply {
            setTitle("Actualizar película")
            setMessage("Escribe la nueva puntuación")
            setView(input)
            setPositiveButton("Aceptar") { _, _ ->
                val points = input.text.toString().toIntOrNull()
                if (points in 1..10) {
                    movie.myScore = points
                    viewModelScope.launch {
                        try {
                            val response = MovieDbConnection.service.updateMovie(movie.id, movie)
                            if (response.isSuccessful) {
                                loadMovies()
                            } else {
                                _error.value = "Error al actualizar: ${response.code()}"
                            }
                        } catch (e: Exception) {
                            _error.value = "Error de red: ${e.message}"
                        }
                    }
                } else {
                    _error.value = "La puntuación debe estar entre 1 y 10"
                }
            }
            setNegativeButton("Cancelar", null)
            show()
        }
    }

    fun onMovieDeleted(movie: Movie, context: Context) {
        AlertDialog.Builder(context).apply {
            setTitle("Eliminar película")
            setMessage("¿Estás seguro de que deseas eliminar esta película?")
            setPositiveButton("Sí") { dialog, _ ->
                viewModelScope.launch {
                    try {
                        val response = MovieDbConnection.service.deleteMovie(movie.id)
                        if (response.isSuccessful) {
                            loadMovies()
                        } else {
                            _error.value = "Error al eliminar: ${response.code()}"
                        }
                    } catch (e: Exception) {
                        _error.value = "Error de red: ${e.message}"
                    }
                }
                dialog.dismiss()
            }
            setNegativeButton("Cancelar", null)
            show()
        }
    }

    fun titleASC() {
        titleASC = true
        titleDESC = false
        myscoreASC = false
        myscoreDESC = false
        loadMovies()
    }

    fun titleDESC() {
        titleASC = false
        titleDESC = true
        myscoreASC = false
        myscoreDESC = false
        loadMovies()
    }

    fun myscoreASC() {
        titleASC = false
        titleDESC = false
        myscoreASC = true
        myscoreDESC = false
        loadMovies()
    }

    fun myscoreDESC() {
        titleASC = false
        titleDESC = false
        myscoreASC = false
        myscoreDESC = true
        loadMovies()
    }

    fun addMovie() {
        _error.value = "Funcionalidad de agregar no implementada aún"
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}
