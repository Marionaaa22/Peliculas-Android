package com.mariona.act_pelis_favoritas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mariona.act_pelis_favoritas.databinding.ActivityMovieDetailsBinding
import com.mariona.act_pelis_favoritas.models.Movie

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide() // Evita error si la barra de acción es null

        movie = intent.getSerializableExtra("movie") as? Movie ?: return // Manejo seguro

        binding.movieAdult.text = "Adult: ${movie.adult}"
        binding.movieFavorite.text = "Favorite: ${movie.favorite}"
        binding.movieGenere.text = "Genre: "

        movie.genreIDS.forEach { genreId ->
            binding.movieGenereIds.append("$genreId, ") // Agrega los géneros correctamente
        }

        binding.movieOriginalLanguage.text = "Original Language: ${movie.originalLanguage}"
        binding.movieTitle.text = "Original Title: ${movie.originalTitle}"
        binding.movieOverview.text = "Overview: ${movie.overview}"
        binding.moviePopularity.text = "Popularity: ${movie.popularity}"
        binding.movieYear.text = "Release Date: ${movie.releaseDate}"
        binding.movieVoteAverage.text = "Vote Average: ${movie.voteAverage}"
        binding.movieVoteCount.text = "Vote Count: ${movie.voteCount}"
        binding.moviePoints.text = "My Score: ${movie.myScore}"

        val circularProgressDrawable = CircularProgressDrawable(this).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        val requestOption = RequestOptions()
            .circleCrop()
            .placeholder(circularProgressDrawable)

        Glide.with(this)
            .load(movie.posterPath)
            .apply(requestOption)
            .into(binding.movieImage)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
