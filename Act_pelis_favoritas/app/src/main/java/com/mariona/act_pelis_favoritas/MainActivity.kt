package com.mariona.act_pelis_favoritas

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mariona.act_pelis_favoritas.databinding.ActivityMainBinding
import com.mariona.act_pelis_favoritas.models.MovieElement
import com.mariona.act_pelis_favoritas.retrofit.MovieDbConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private var moviesList: List<MovieElement> = emptyList()
    private val movieDbService = MovieDbConnection.movieDbService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        setupRecyclerView()
        getMovies()
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter(emptyList(), ::deleteFavoriteMovie, ::editScore)
        binding.recyclerViewMovies.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_title_asc -> {
                sortMovies("title", "asc")
                true
            }
            R.id.action_sort_title_desc -> {
                sortMovies("title", "desc")
                true
            }
            R.id.action_sort_rating_asc -> {
                sortMovies("score", "asc")
                true
            }
            R.id.action_sort_rating_desc -> {
                sortMovies("score", "desc")
                true
            }
            R.id.action_weather -> {
                startActivity(Intent(this, WeatherActivity::class.java))
                true
            }
            R.id.action_add -> {
                showToast("Funcionalidad de agregar no implementada aún")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMovies() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { movieDbService.getMovies() }
                if (response.isSuccessful) {
                    response.body()?.let {
                        moviesList = it
                        moviesAdapter.updateData(moviesList)
                    } ?: showToast("La lista de películas es nula")
                } else {
                    showToast("Error al obtener la lista de películas")
                }
            } catch (e: Exception) {
                showToast("Error de conexión al obtener películas")
            }
        }
    }

    private fun sortMovies(sortBy: String, order: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    when (sortBy) {
                        "title" -> if (order == "asc") movieDbService.listMoviesByTitleAsc() else movieDbService.listMoviesByTitleDesc()
                        "score" -> if (order == "asc") movieDbService.listMoviesByScoreAsc() else movieDbService.listMoviesByScoreDesc()
                        else -> null
                    }
                }
                if (response?.isSuccessful == true) {
                    response.body()?.let {
                        moviesList = it
                        moviesAdapter.updateData(moviesList)
                    } ?: showToast("Error al ordenar películas")
                } else {
                    showToast("Error al ordenar películas")
                }
            } catch (e: Exception) {
                showToast("Error de conexión al ordenar películas")
            }
        }
    }

    private fun deleteFavoriteMovie(movieId: Long) {
        AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que deseas eliminar esta película?")
            .setPositiveButton("Sí") { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        movieDbService.deleteFavoriteMovie(movieId)
                        getMovies()
                    } catch (e: Exception) {
                        showToast("Error al eliminar la película")
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun editScore(movie: MovieElement) {
        val editTextScore = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf(InputFilter.LengthFilter(2))
        }

        AlertDialog.Builder(this)
            .setView(editTextScore)
            .setMessage("Modificar puntuación de la película:")
            .setPositiveButton("OK") { dialog, _ ->
                val score = editTextScore.text?.trim()?.toIntOrNull()
                if (score != null && score in 1..10) {
                    lifecycleScope.launch {
                        try {
                            val updatedMovie = movie.copy(myScore = score)
                            val response = withContext(Dispatchers.IO) {
                                movieDbService.updateScore(movie.id, updatedMovie)
                            }
                            if (response.isSuccessful) {
                                getMovies()
                            } else {
                                showToast("Error al actualizar la puntuación")
                            }
                        } catch (e: Exception) {
                            showToast("Error de conexión al actualizar la puntuación")
                        }
                    }
                } else {
                    showToast("La puntuación debe ser un número entre 1 y 10")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
