package com.limboooo.contactrecorder.repository.room.entity.whole

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Phones(
    @PrimaryKey(true) val id: Long,
    var ownerId: Long,
    var phone: String
)
