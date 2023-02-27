package com.limboooo.contactrecorder.repository.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize

//人亲（钱）往来的信息
@Parcelize
@Entity
data class Exchanges(
    @PrimaryKey(true) val uid: Int?,
    val ownerUid: Int?,
    val time: String?,
    val thing: String,
    val money: String
) : Parcelable