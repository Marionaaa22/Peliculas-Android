package com.mariona.act_pelis_favoritas

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KFunction1

class MoviesAdapter(
    private var movies: List<MovieElement>,
    private val deleteListener: (Long) -> Unit,
    private val editScoreListener: (MovieElement) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    fun updateData(newMovies: List<MovieElement>) {
        val oldSize = movies.size
        movies = newMovies
        notifyItemRangeChanged(0, oldSize.coerceAtMost(newMovies.size))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieElement) {
            binding.apply {
                title.text = movie.title
                year.text = movie.releaseDate?.take(4) ?: "N/A"
                rating.text = movie.myScore.toString()

                Glide.with(root.context)
                    .load(movie.posterPath)
                    .into(thumb)

                delete.setImageResource(R.drawable.ic_delete)
                editScore.setImageResource(R.drawable.ic_score)

                delete.setOnClickListener {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        deleteListener(movie.id)
                    }
                }
                editScore.setOnClickListener {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        editScoreListener(movie)
                    }
                }
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MovieDetailsActivity::class.java).apply {
                    putExtra("titulo", movie.title)
                    putExtra("tituloOriginal", movie.originalTitle)
                    putExtra("lenguajeOriginal", movie.originalLanguage)
                    putExtra("id", movie.id)
                    putExtra("adulto", movie.adult)
                    putExtra("descripcion", movie.overview)
                    putExtra("popularidad", movie.popularity)
                    putExtra("fecha", movie.releaseDate)
                    putExtra("votacion", movie.voteAverage)
                    putExtra("numVotacion", movie.voteCount)
                    putExtra("puntuacion", movie.myScore)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
    }
}