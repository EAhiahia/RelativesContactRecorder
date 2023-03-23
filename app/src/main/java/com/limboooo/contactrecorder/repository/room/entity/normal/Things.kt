package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Things(
    @PrimaryKey(true) val id: Long,
    var ownerId: Long,
    var thing: String
)
