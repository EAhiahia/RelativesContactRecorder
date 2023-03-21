package com.limboooo.contactrecorder.repository.room.entity.whole

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoneyReceivedBack(
    @PrimaryKey(true) val id: Int,
    var ownerId: Int,
    val time: String,
    val thing: String,
    val money: String
)