package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.PrimaryKey

data class NormalKey(
    @PrimaryKey(true) val uid: Int,
    //基本信息
    val normalKey: String,
)