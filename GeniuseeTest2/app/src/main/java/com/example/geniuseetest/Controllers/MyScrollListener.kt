package com.example.geniuseetest.Controllers

import android.widget.AbsListView

class MyScrollListener : AbsListView.OnScrollListener {

    //APIRequest object that is currently using
    companion object {
        lateinit var apiRequest: APIRequest
    }

    //Use to make infinity scroll
    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (visibleItemCount + firstVisibleItem >= totalItemCount - 1 && apiRequest.page < apiRequest.totalPages) {
            apiRequest.page++
            if (apiRequest.aim.equals("trends")) {
                apiRequest.getTrendsJSON()
            } else if (apiRequest.aim.equals("search")) {
                apiRequest.getSearchMovie()
            }
            (view?.adapter as MovieAdapter).notifyDataSetChanged()
        }
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

    }
}