package com.limboooo.contactrecorder.repository.room.entity.whole

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoneyGaveBack(
    @PrimaryKey(true) val id: Long,
    var ownerId: Long,
    var time: String,
    var thing: String,
    var money: String
)
