package com.arctouch.codechallenge.home

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar
import com.arctouch.codechallenge.api.ApiUtils

interface HomeActivityViewPresenter {
    fun onCreate()
    fun onResume()
    fun setOnBackPressed(backMore: AppCompatImageView)

    fun initGenres(apiUtils: ApiUtils)
    fun initList(progressBar: ProgressBar, recyclerView: RecyclerView, apiUtils: ApiUtils)
    fun initScrollListener(recyclerView: RecyclerView)
    fun loadMore(apiUtils: ApiUtils, recyclerView: RecyclerView)
    fun getActivity(): FragmentActivity
}