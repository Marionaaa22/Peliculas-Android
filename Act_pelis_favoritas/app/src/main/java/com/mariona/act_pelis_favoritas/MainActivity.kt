package com.mariona.act_pelis_favoritas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mariona.act_pelis_favoritas.adapter.movieAdapter
import com.mariona.act_pelis_favoritas.databinding.ActivityMainBinding
import com.mariona.act_pelis_favoritas.viewModels.MainViewModel
import com.mariona.act_pelis_favoritas.viewModels.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }
    private lateinit var binding: ActivityMainBinding
    private val moviesAdapter = movieAdapter(
        emptyList(),
        this,
        { viewModel.onMovieClicked(it, this) },
        { viewModel.onMovieDeleted(it, this) },
        { viewModel.onMovieUpdated(it, this) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewMovies.adapter = moviesAdapter

        viewModel.moviesListLoading.observe(this) { cargando ->
            binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
        }

        viewModel.movies.observe(this) { movies ->
            moviesAdapter.movies = movies
            moviesAdapter.notifyDataSetChanged()
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.view.apply {
                    setBackgroundColor(Color.RED)
                    findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
                        setTextColor(Color.WHITE)
                        textSize = 24f
                    }
                }
                snackbar.show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_title_asc -> {
                viewModel.titleASC()
                true
            }
            R.id.action_sort_title_desc -> {
                viewModel.titleDESC()
                true
            }
            R.id.action_sort_rating_asc -> {
                viewModel.myscoreASC()
                true
            }
            R.id.action_sort_rating_desc -> {
                viewModel.myscoreDESC()
                true
            }
            R.id.action_weather -> {
                startActivity(Intent(this, WeatherActivity::class.java))
                true
            }
            R.id.action_add -> {
                // Implementar funcionalidad de agregar película
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}