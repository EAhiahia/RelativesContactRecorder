package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Moneys(
    @PrimaryKey(true) val id: Int,
    val ownerId: Int,
    val money: Int
)
