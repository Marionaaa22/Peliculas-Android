package com.mariona.act_pelis_favoritas


import android.app.Activity
import android.os.Bundle
import com.mariona.act_pelis_favoritas.databinding.ActivityMovieDetailsBinding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mariona.act_pelis_favoritas.models.Movie

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        supportActionBar!!.hide()
        val view: binding.root
        setContentView(view)

        movie = intent.getSerializableExtra("movie") as Movie
        binding.titleTextView.text = "Title: ${movie.title}"
        binding.originalTitleTextView.text = "Original Title: ${movie.originalTitle}"
        binding.originalLanguageTextView.text = "Original Language: ${movie.originalLanguage}"
        binding.idTextView.text = "ID: ${movie.id}"
        binding.overviewTextView.text = "Overview: ${movie.overview}"
        binding.popularityTextView.text = "Popularity: ${movie.popularity}"
        binding.releaseDateTextView.text = "Release Date: ${movie.releaseDate}"
        binding.voteAverageTextView.text = "Vote Average: ${movie.voteAverage}"
        binding.voteCountTextView.text = "Vote Count: ${movie.voteCount}"
        binding.myScoreTextView.text = "My Score: ${movie.myScore}"
        binding.adultTextView.text = "Adult: ${if (movie.adult) "Yes" else "No"}"
        binding.backButton.setOnClickListener {
            finish()
        }




        val titulo = intent.getStringExtra("titulo")
        val tituloOriginal = intent.getStringExtra("tituloOriginal")
        val lenguajeOriginal = intent.getStringExtra("lenguajeOriginal")
        val adulto = intent.getBooleanExtra("adulto", false)
        val id = intent.getLongExtra("id", 0L)
        val descripcion = intent.getStringExtra("descripcion")
        val popularidad = intent.getDoubleExtra("popularidad", 0.0)
        val fecha = intent.getStringExtra("fecha")
        val votos = intent.getDoubleExtra("votacion", 0.0)
        val numVotos = intent.getLongExtra("numVotacion", 0L)
        val puntuacion = intent.getIntExtra("puntuacion", 0)

        binding.titleTextView.text = "Title: $titulo"
        binding.originalTitleTextView.text = "Original Title: $tituloOriginal"
        binding.originalLanguageTextView.text = "Original Language: $lenguajeOriginal"
        binding.idTextView.text = "ID: $id"
        binding.overviewTextView.text = "Overview: $descripcion"
        binding.popularityTextView.text = "Popularity: $popularidad"
        binding.releaseDateTextView.text = "Release Date: $fecha"
        binding.voteAverageTextView.text = "Vote Average: $votos"
        binding.voteCountTextView.text = "Vote Count: $numVotos"
        binding.myScoreTextView.text = "My Score: $puntuacion"
        binding.adultTextView.text = "Adult: ${if (adulto) "Yes" else "No"}"

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}