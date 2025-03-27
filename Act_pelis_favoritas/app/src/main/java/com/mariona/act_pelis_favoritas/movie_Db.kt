package com.mariona.act_pelis_favoritas

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.mariona.act_pelis_favoritas.adapter.MovieDBAdapter
import com.mariona.act_pelis_favoritas.databinding.ActivityMovieDbBinding
import com.mariona.act_pelis_favoritas.models.MovieDBMovies
import com.mariona.act_pelis_favoritas.viewModels.MovieDBViewModel
import com.mariona.act_pelis_favoritas.viewModels.MovieDBViewModelFactory

class movie_Db : AppCompatActivity() {
    private val viewModel: MovieDBViewModel by viewModels { MovieDBViewModelFactory() }
    private lateinit var binding: ActivityMovieDbBinding
    private val movieDBAdapter = MovieDBAdapter(emptyList(), this, {addFavoriteMovie(it)})
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar!!.hide()

        binding = ActivityMovieDbBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.Movies.adapter = movieDBAdapter

        viewModel.moviesListLoading.observe(this) { cargando ->
            if (cargando) {
                binding.progressMovieDB.visibility = View.VISIBLE
            }
            else {
                binding.progressMovieDB.visibility = View.GONE
            }
        }

        viewModel.movies.observe(this) { movies ->
            if (movies!=null) {
                movieDBAdapter.movies = movies
                movieDBAdapter.notifyDataSetChanged()
            }
        }

        viewModel.error.observe(this) {
            if (it != null) {
                val snackbar = Snackbar.make(view, it,
                    Snackbar.LENGTH_LONG).setAction("Action", null)
                snackbar.setActionTextColor(Color.WHITE)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.RED)
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                textView.textSize = 28f
                snackbar.show()
            }
        }
    }

    fun addFavoriteMovie(movieDBMovies: MovieDBMovies){
        viewModel.addFavorite(movieDBMovies, this)

    }

    fun searchMovie(view: View) {
        var textQuery = binding.textQuery.text.toString()
        viewModel.loadMovies(textQuery)
    }

    fun close(view: View) {
        finish()
    }
}