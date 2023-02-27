package com.limboooo.contactrecorder.repository.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * 和某个亲戚往来的信息
 * 不定义tableName的话，类名默认作为表名
 */
@Parcelize
@Entity
data class RelativesBaseInfo(
    @PrimaryKey(true)  val uid: Int?,
    //基本信息
    val name: String,
    var moneyReceivedWhole: Int,
    var moneyGaveWhole: Int
) : Parcelable