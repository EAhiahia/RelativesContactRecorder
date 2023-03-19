package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NormalKey(
    @PrimaryKey(true) val id: Int,
    //基本信息
    val normalKey: String,
)