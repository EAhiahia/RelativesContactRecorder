package com.limboooo.contactrecorder.repository.room.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelativesInfoWhole(
    @Embedded val baseInfo: RelativesBaseInfo,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val phones: MutableList<Phones>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val emails: MutableList<Emails>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val moneyReceived: MutableList<Exchanges>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val moneyGave: MutableList<Exchanges>
) : Parcelable
