package com.arctouch.codechallenge.home

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.widget.ProgressBar
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.ApiUtils


class HomeActivity :
        AppCompatActivity(), HomeActivityViewPresenter {

    private val homePresenter: HomeActivityViewPresenter = HomeActivityViewPresenterLogic(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        homePresenter.onCreate()
    }

    override fun onCreate() {
        homePresenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        homePresenter.onResume()
    }

    override fun setOnBackPressed(backMore: AppCompatImageView) {
        homePresenter.setOnBackPressed(backMore)
    }

    override fun initGenres(apiUtils: ApiUtils) {
        homePresenter.initGenres(apiUtils)
    }

    override fun initList(progressBar: ProgressBar, recyclerView: RecyclerView, apiUtils: ApiUtils) {
        homePresenter.initList(progressBar, recyclerView, apiUtils)
    }

    override fun initScrollListener(recyclerView: RecyclerView) {
        homePresenter.initScrollListener(recyclerView)
    }

    override fun loadMore(apiUtils: ApiUtils, recyclerView: RecyclerView) {
        homePresenter.loadMore(apiUtils, recyclerView)
    }

    override fun getActivity(): FragmentActivity {
        return this
    }


}
