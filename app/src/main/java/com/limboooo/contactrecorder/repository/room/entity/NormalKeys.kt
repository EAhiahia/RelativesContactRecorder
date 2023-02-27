package com.limboooo.contactrecorder.repository.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class NormalKeys(
    @PrimaryKey(true) val uid: Int?,
    val what: String
) : Parcelable