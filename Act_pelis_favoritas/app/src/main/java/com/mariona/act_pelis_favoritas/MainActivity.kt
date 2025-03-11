package com.mariona.act_pelis_favoritas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mariona.act_pelis_favoritas.databinding.ActivityMainBinding
import com.mariona.act_pelis_favoritas.model.MovieElement
import com.mariona.act_pelis_favoritas.server.MovieDbConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.EditText
import android.text.InputFilter
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val movieDbService = MovieDbConnection.movieDbService
    private val moviesAdapter by lazy { MoviesAdapter(emptyList(), ::deleteFavoriteMovie, ::editScore) }
    private var moviesList: List<MovieElement> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        setupRecyclerView()
        getMovies()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMovies.apply {
            var adapter = moviesAdapter
            var layoutManager = GridLayoutManager(this@MainActivity, 2)
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
                sortMovies("title", true)
                true
            }
            R.id.action_sort_title_desc -> {
                sortMovies("title", false)
                true
            }
            R.id.action_sort_rating_asc -> {
                sortMovies("score", true)
                true
            }
            R.id.action_sort_rating_desc -> {
                sortMovies("score", false)
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
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                withContext(Dispatchers.IO) { movieDbService.getMovies() }
            }.onSuccess { response ->
                response.body()?.let {
                    moviesList = it
                    moviesAdapter.updateData(moviesList)
                } ?: showToast("La lista de películas es nula")
            }.onFailure {
                showToast("Error al obtener la lista de películas")
            }
        }
    }

    private fun sortMovies(sortBy: String, ascending: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    when (sortBy) {
                        "title" -> if (ascending) movieDbService.listMoviesByTitleAsc() else movieDbService.listMoviesByTitleDesc()
                        "score" -> if (ascending) movieDbService.listMoviesByScoreAsc() else movieDbService.listMoviesByScoreDesc()
                        else -> throw IllegalArgumentException("Ordenación no válida")
                    }
                }
            }.onSuccess { response ->
                response.body()?.let {
                    moviesList = it
                    moviesAdapter.updateData(moviesList)
                } ?: showToast("La lista de películas ordenada es nula")
            }.onFailure {
                showToast("Error al obtener la lista de películas ordenada")
            }
        }
    }

    private fun deleteFavoriteMovie(movieId: Long) {
        AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que deseas eliminar esta película?")
            .setPositiveButton("Sí") { dialog, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    runCatching { movieDbService.deleteFavoriteMovie(movieId) }
                        .onSuccess { getMovies() }
                        .onFailure { showToast("Error al eliminar la película") }
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
                val score = editTextScore.text.toString().toIntOrNull()
                if (score in 1..10) {
                    CoroutineScope(Dispatchers.Main).launch {
                        runCatching {
                            withContext(Dispatchers.IO) { movieDbService.updateScore(movie.id, movie.copy(myScore = score)) }
                        }.onSuccess {
                            getMovies()
                        }.onFailure {
                            showToast("Error al actualizar la puntuación")
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
