package com.limboooo.contactrecorder.repository.room.entity.whole

import androidx.room.Embedded
import androidx.room.Relation

data class RelativesInfoWhole(
    @Embedded val baseInfo: RelativesBaseInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val phones: MutableList<Phones>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val emails: MutableList<Emails>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val moneyReceived: MutableList<MoneyReceived>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val moneyReceivedBack: MutableList<MoneyReceivedBack>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val moneyGave: MutableList<MoneyGave>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val moneyGaveBack: MutableList<MoneyGaveBack>
)
