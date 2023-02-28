package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Names(
    @PrimaryKey(true) val uid: Int,
    val ownerUid: Int,
    val name: String
)
