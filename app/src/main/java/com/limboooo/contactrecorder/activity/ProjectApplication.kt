package com.limboooo.contactrecorder.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors
import com.limboooo.contactrecorder.repository.room.ProjectDatabase

class ProjectApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    val database: ProjectDatabase by lazy { ProjectDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        context = applicationContext
    }
}