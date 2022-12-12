package com.example.myfitv2.Fragments

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myfitv2.Activities.CalendarActivity
import com.example.myfitv2.R


interface HasToolbar {
    val toolbar: Toolbar?
}

interface HasBackButton

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    abstract val titleRes: Int?

    val activityToolbar: Toolbar
        get() = (requireActivity() as CalendarActivity).binding.activityToolbar

    override fun onStart() {
        super.onStart()
        if (this is HasToolbar) {
            activityToolbar.makeGone()
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        }

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (this is HasBackButton) {
            actionBar?.title = if (titleRes != null) context?.getString(titleRes!!) else ""
            actionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            actionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onStop() {
        super.onStop()
        if (this is HasToolbar) {
            activityToolbar.makeVisible()
            (requireActivity() as AppCompatActivity).setSupportActionBar(activityToolbar)
        }

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (this is HasBackButton) {
            actionBar?.title = context?.getString(R.string.activity_title_view)
        }
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
