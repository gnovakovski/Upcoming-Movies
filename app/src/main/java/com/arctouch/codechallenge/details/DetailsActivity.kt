package com.arctouch.codechallenge.details

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val movieImageUrlBuilder = MovieImageUrlBuilder()

        textViewTitle.text = this.intent.getStringExtra("title")
        textViewGenres.text = this.intent.getStringExtra("genre")
        textViewRelease.text = this.intent.getStringExtra("release")
        textViewNote.text = this.intent.getStringExtra("note")
        textViewOverview.text = this.intent.getStringExtra("overview")
        val image = this.intent.getStringExtra("image")
        Glide.with(this)
                .load(image?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(imageViewPoster)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
