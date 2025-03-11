package com.mariona.act_pelis_favoritas

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KFunction1

class MoviesAdapter(
    private var movies: List<MovieElement>,
    private val deleteListener: KFunction1<Long, Unit>,
    private val editScoreListener: KFunction1<MovieElement, Unit>
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    fun updateData(newMovies: List<MovieElement>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movieId = movies[position].id
                    deleteListener(movieId)
                }
            }
            binding.editScore.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = movies[position]
                    editScoreListener(movie)
                }
            }
        }

        fun bind(movie: MovieElement) {
            binding.apply {
                title.text = movie.title
                year.text = movie.releaseDate?.take(4) ?: "Año desc."
                rating.text = movie.myScore.toString()
                Glide.with(root.context)
                    .load(movie.posterPath)
                    .into(thumb)
                delete.setImageResource(R.drawable.ic_delete)
                editScore.setImageResource(R.drawable.ic_score)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MovieDetailsActivity::class.java)
                intent.putExtra("titulo", movie.title)
                intent.putExtra("tituloOriginal", movie.originalTitle)
                intent.putExtra("lenguajeOriginal", movie.originalLanguage)
                intent.putExtra("id", movie.id)
                intent.putExtra("adulto", movie.adult)
                intent.putExtra("descripcion", movie.overview)
                intent.putExtra("popularidad", movie.popularity)
                intent.putExtra("fecha", movie.releaseDate)
                intent.putExtra("votacion", movie.voteAverage)
                intent.putExtra("numVotacion", movie.voteCount)
                intent.putExtra("puntuacion", movie.myScore)
                itemView.context.startActivity(intent)
            }
        }
    }
}