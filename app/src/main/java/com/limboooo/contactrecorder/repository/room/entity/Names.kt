package com.limboooo.contactrecorder.repository.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Names(
    @PrimaryKey(true) val uid: Int?,
    val ownerUid: Int?,
    val name: String
) : Parcelable
