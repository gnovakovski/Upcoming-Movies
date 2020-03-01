package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.data.Cache
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.annotation.NonNull
import android.util.Log
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie




class HomeActivity : BaseActivity() {

    var isLoading = false
    var currentPage : Long = 1
    var moviesWithGenres : MutableList<Movie>  = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                moviesWithGenres  =  it.results.map { movie ->
                     movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                 }.toMutableList()
                recyclerView.adapter = HomeAdapter(moviesWithGenres)
                progressBar.visibility = View.GONE
            }


        initScrollListener()
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(@NonNull recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView!!.layoutManager as LinearLayoutManager

                if (!isLoading) {

                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.adapter.itemCount - 1) {
                        //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })

    }

     private fun loadMore(){
         currentPage += 1

             api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, currentPage,  "")
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe {
                         moviesWithGenres.addAll(it.results.map { movie ->
                             movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                         }.toMutableList())

                         recyclerView.adapter.notifyDataSetChanged()
                         isLoading = false
                     }
         }



}
