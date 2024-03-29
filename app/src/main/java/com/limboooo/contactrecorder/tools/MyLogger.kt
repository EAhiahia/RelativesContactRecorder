package com.limboooo.contactrecorder.tools

import android.util.Log

object MyLogger {

    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private var level = VERBOSE

    fun v(msg: String, tag: String = "shareLog") {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(msg: String, tag: String = "shareLog") {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(msg: String, tag: String = "shareLog") {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(msg: String, tag: String = "shareLog") {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String, tag: String = "shareLog") {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }

}