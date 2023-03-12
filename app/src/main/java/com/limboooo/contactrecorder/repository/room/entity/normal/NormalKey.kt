package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.PrimaryKey

data class NormalKey(
    @PrimaryKey(true) val id: Int,
    //基本信息
    val normalKey: String,
)