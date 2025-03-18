package com.mariona.act_pelis_favoritas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mariona.act_pelis_favoritas.databinding.ActivityMovieDetailsBinding
import com.mariona.act_pelis_favoritas.models.Movies

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var movie: Movies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        movie = intent.getSerializableExtra("movie") as? Movies ?: return

        binding.titleTextView.text = "Title: ${movie.title ?: "Unknown"}"
        binding.originalTitleTextView.text = "Original Title: ${movie.originalTitle ?: "N/A"}"
        binding.releaseDateTextView.text = "Release Date: ${movie.releaseDate ?: "N/A"}"
        binding.adultTextView.text = "Adult: ${if (movie.adult) "Yes" else "No"}"
        binding.originalLanguageTextView.text = "Original Language: ${movie.originalLanguage ?: "N/A"}"
        binding.overviewTextView.text = "Overview: ${movie.overview ?: "No description available"}"
        binding.popularityTextView.text = "Popularity: ${movie.popularity}"
        binding.voteAverageTextView.text = "Vote Average: ${movie.voteAverage}"
        binding.voteCountTextView.text = "Vote Count: ${movie.voteCount}"
        binding.myScoreTextView.text = "My Score: ${movie.myScore}"
        binding.favoriteTextView.text = "Favorite: ${if (movie.favorite) "Yes" else "No"}"

        binding.genreTextView.text = "Genre: " + movie.genreIDS.joinToString(", ")

        val circularProgressDrawable = CircularProgressDrawable(this).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        Glide.with(this)
            .load(movie.posterPath)
            .apply(RequestOptions().placeholder(circularProgressDrawable))
            .into(binding.movieImage)

        binding.backButton.setOnClickListener { finish() }
    }
}