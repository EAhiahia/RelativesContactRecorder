package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Names(
    @PrimaryKey(true) val id: Int,
    val ownerId: Int,
    val name: String
)
