package com.limboooo.contactrecorder.repository.room.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class NormalDataSet(
    @Embedded val keys: NormalKeys?,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val names: MutableList<String>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val moneys: MutableList<String>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val things: MutableList<String>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val phones: MutableList<String>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val emails: MutableList<String>
) : Parcelable