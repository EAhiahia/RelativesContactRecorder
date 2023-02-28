package com.limboooo.contactrecorder.repository.room.entity.whole

import androidx.room.Embedded
import androidx.room.Relation

data class RelativesInfoWhole(
    @Embedded val baseInfo: RelativesBaseInfo,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val phones: List<Phones>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val emails: List<Emails>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val moneyReceived: List<Exchanges>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val moneyGave: List<Exchanges>
)
