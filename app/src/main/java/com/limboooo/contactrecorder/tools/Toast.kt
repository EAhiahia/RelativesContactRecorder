package com.limboooo.contactrecorder.tools

import android.widget.Toast
import com.limboooo.contactrecorder.activity.ProjectApplication

fun String.showShortToast() {
    Toast.makeText(ProjectApplication.context, this, Toast.LENGTH_SHORT).show()
}

fun Int.showShortToast() {
    Toast.makeText(ProjectApplication.context, this, Toast.LENGTH_SHORT).show()
}

fun String.showLongToast() {
    Toast.makeText(ProjectApplication.context, this, Toast.LENGTH_LONG).show()
}

fun Int.showLongToast() {
    Toast.makeText(ProjectApplication.context, this, Toast.LENGTH_LONG).show()
}

@JvmName("showShortToast1")
fun showShortToast(content: String) {
    Toast.makeText(ProjectApplication.context, content, Toast.LENGTH_SHORT).show()
}

@JvmName("showLongToast1")
fun showLongToast(content: String) {
    Toast.makeText(ProjectApplication.context, content, Toast.LENGTH_LONG).show()
}