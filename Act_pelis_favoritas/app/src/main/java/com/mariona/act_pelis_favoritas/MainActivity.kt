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
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var moviesList: List<MovieElement>
    private val movieDbService = MovieDbConnection.movieDbService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        moviesAdapter = MoviesAdapter(emptyList(), ::deleteFavoriteMovie, ::editScore)
        binding.recyclerViewMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewMovies.layoutManager = layoutManager

        getMovies()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            val response = withContext(Dispatchers.IO) {
                movieDbService.getMovies()
            }
            if (response.isSuccessful) {
                val movies = response.body()
                if (movies != null) {
                    moviesList = movies
                    moviesAdapter.updateData(moviesList)
                } else {
                    Toast.makeText(this@MainActivity, "La lista de películas es nula", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Error al obtener la lista de películas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sortMovies(sortBy: String, order: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = withContext(Dispatchers.IO) {
                when (sortBy) {
                    "title" -> if (order == "asc") movieDbService.listMoviesByTitleAsc() else movieDbService.listMoviesByTitleDesc()
                    "score" -> if (order == "asc") movieDbService.listMoviesByScoreAsc() else movieDbService.listMoviesByScoreDesc()
                    else -> throw IllegalArgumentException("Ordenación no válida: $sortBy")
                }
            }
            if (response.isSuccessful) {
                val sortedMovies = response.body()
                if (sortedMovies != null) {
                    moviesList = sortedMovies
                    moviesAdapter.updateData(moviesList)
                } else {
                    Toast.makeText(this@MainActivity, "La lista de películas ordenada es nula", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Error al obtener la lista de películas ordenada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteFavoriteMovie(movieId: Long) {
        AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que deseas eliminar esta película?")
            .setPositiveButton("Sí") { dialog, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    movieDbService.deleteFavoriteMovie(movieId)
                    getMovies()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun editScore(movie: MovieElement) {
        val builder = AlertDialog.Builder(this)
        val editTextScore = EditText(this)
        editTextScore.inputType = InputType.TYPE_CLASS_NUMBER
        editTextScore.filters = arrayOf(InputFilter.LengthFilter(2))

        builder.setView(editTextScore)
            .setMessage("Modificar puntuació de la pelicula:")
            .setPositiveButton("OK") { dialog, _ ->
                val scoreText = editTextScore.text.toString()
                val score = scoreText.toIntOrNull()

                if (score != null && score in 1..10) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val updatedMovie = movie.copy(myScore = score)
                        val response = withContext(Dispatchers.IO) {
                            movieDbService.updateScore(movie.id, updatedMovie)
                        }
                        if (response.isSuccessful) {
                            getMovies()
                        } else {
                            Toast.makeText(this@MainActivity, "Error al actualizar la puntuació", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "La puntuació ha de ser un número entre 1 i 10", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}