package com.mariona.act_pelis_favoritas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mariona.act_pelis_favoritas.MovieDetailsActivity
import com.mariona.act_pelis_favoritas.R
import com.mariona.act_pelis_favoritas.models.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class movieAdapter (var movies: List<Movie> = emptyList(),
                    private val mContext: Context,
                    private val onMovieClicked: (Movie) -> Unit,
                    private val onUpdateClicked: (Movie) -> Unit,
                    private val onDeleteClicked: (Movie) -> Unit,
    ):
    RecyclerView.Adapter<movieAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.activity_movie_item, parent, false), mContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        holder.itemView.setOnClickListener {
            onMovieClicked(movie)
        }

        val binding = MovieDetailsActivity.bind(holder.itemView)
        binding.pointsUpdate.setOnClickListener {
            onUpdateClicked(movie)
        }
        binding.movieDelete.setOnClickListener {
            onDeleteClicked(movie)
        }
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(val view: View, private val mContext: Context): RecyclerView.ViewHolder(view){

        private val binding = MovieDetailsActivity.bind(view)

        fun bind(movie: Movie){
            binding.movieTitle.text = movie.title
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = LocalDate.parse(movie.releaseDate, formatter)
            binding.movieYear.text = date.year.toString()
            binding.movieScore.text = movie.myScore.toString()

            val circularProgressDrawable = CircularProgressDrawable(mContext)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val requestOption = RequestOptions()
                .circleCrop()
                .placeholder(circularProgressDrawable)

            Glide.with(binding.movieImage)
                .load(movie.posterPath)
                .apply(requestOption)
                .into(binding.movieImage)
        }
    }
}