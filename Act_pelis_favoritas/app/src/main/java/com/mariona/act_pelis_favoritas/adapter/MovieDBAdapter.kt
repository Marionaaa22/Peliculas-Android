package com.mariona.act_pelis_favoritas.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mariona.act_pelis_favoritas.R
import com.mariona.act_pelis_favoritas.databinding.MovieCardBinding
import com.mariona.act_pelis_favoritas.models.MovieDBMovies
import com.mariona.act_pelis_favoritas.models.Movies
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MovieDBAdapter(
    var movies: List<MovieDBMovies> = emptyList(),
    private val mContext: Context,
    private val onAddClicked: (MovieDBMovies) -> Unit
) : RecyclerView.Adapter<MovieDBAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.movie_card, parent, false), mContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        val binding = MovieCardBinding.bind(holder.itemView)

        binding.movieAdd.setOnClickListener {
            onAddClicked(movie)
        }
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(val view: View, private val mContext: Context) : RecyclerView.ViewHolder(view) {

        private val binding = MovieCardBinding.bind(view)

        fun bind(movie: MovieDBMovies) {
            binding.movieTitle.text = movie.title
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(movie.releaseDate, formatter)
            binding.movieYear.text = date.year.toString()
            binding.moviePoints.text = movie.voteAverage.toString()

            val circularProgressDrawable = CircularProgressDrawable(mContext)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val requestOption = RequestOptions()
                .placeholder(circularProgressDrawable)

            Glide.with(binding.movieImage)
                .load(movie.posterPath)
                .apply(requestOption)
                .into(binding.movieImage)
        }
    }
}