package com.mariona.act_pelis_favoritas


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mariona.act_pelis_favoritas.databinding.MovieDetailsBinding

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: MovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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