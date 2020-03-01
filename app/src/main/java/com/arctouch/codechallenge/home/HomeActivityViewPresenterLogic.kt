package com.arctouch.codechallenge.home

import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.arctouch.codechallenge.api.ApiUtils
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivityViewPresenterLogic constructor(private val homeActivityViewPresenter: HomeActivityViewPresenter) :
        HomeActivityViewPresenter {
    var moviesWithGenres: MutableList<Movie> = mutableListOf()
    var isLoading = false
    var currentPage: Long = 1
    val apiUtils: ApiUtils = ApiUtils()


    override fun onCreate() {
        homeActivityViewPresenter.initGenres(apiUtils)
        homeActivityViewPresenter.initScrollListener(homeActivityViewPresenter.getActivity().recyclerView)
    }

    override fun onResume() {

    }

    override fun setOnBackPressed(backMore: AppCompatImageView) {
        backMore.setOnClickListener {
            getActivity().finish()
        }
    }

    override fun initGenres(apiUtils: ApiUtils) {
        apiUtils.api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    initList(homeActivityViewPresenter.getActivity().progressBar, homeActivityViewPresenter.getActivity().recyclerView, apiUtils)
                }
    }

    override fun initList(progressBar: ProgressBar, recyclerView: RecyclerView, apiUtils: ApiUtils) {
        apiUtils.api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }.toMutableList()
                    recyclerView.adapter = HomeAdapter(moviesWithGenres)
                    progressBar.visibility = View.GONE
                }
    }

    override fun initScrollListener(recyclerView: RecyclerView) {
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
                        loadMore(apiUtils = ApiUtils(), recyclerView = recyclerView)
                        isLoading = true
                    }
                }
            }
        })
    }

    override fun loadMore(apiUtils: ApiUtils, recyclerView: RecyclerView) {
        currentPage += 1

        apiUtils.api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, currentPage, "")
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

    override fun getActivity(): FragmentActivity {
        return homeActivityViewPresenter.getActivity()
    }

}