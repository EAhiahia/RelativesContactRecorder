package com.limboooo.contactrecorder.repository.room.entity.whole

import androidx.room.Entity
import androidx.room.PrimaryKey

//人亲（钱）往来的信息
@Entity
data class Exchanges(
    @PrimaryKey(true) val uid: Int,
    val ownerUid: Int,
    val time: String,
    val thing: String,
    val money: String
)