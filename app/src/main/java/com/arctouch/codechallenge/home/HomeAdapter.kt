package com.arctouch.codechallenge.home

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*


class HomeAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        fun bind(movie: Movie) {

                itemView.loadItemProgressBar.visibility = View.GONE
                itemView.titleTextView.text = movie!!.title
                itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
                itemView.releaseDateTextView.text = movie.releaseDate

                Glide.with(itemView)
                        .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(itemView.posterImageView)


                itemView.setOnClickListener(View.OnClickListener { v ->
                    val intent = Intent(itemView.context, DetailsActivity::class.java)
                    intent.putExtra("title", movie.title)
                    val iterator = movie.genres
                    var genres = ""
                    iterator!!.forEach {
                        genres += it.name + " "
                    }
                    intent.putExtra("genre", genres)
                    intent.putExtra("release", movie.releaseDate)
                    intent.putExtra("note", movie.voteAverage)
                    intent.putExtra("overview", movie.overview)
                    intent.putExtra("image", movie.posterPath)
                    v.context.startActivity(intent)
                })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
            return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

}
