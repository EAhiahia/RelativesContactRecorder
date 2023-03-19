package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.limboooo.contactrecorder.repository.room.entity.whole.Emails
import com.limboooo.contactrecorder.repository.room.entity.whole.Phones

data class NormalDataSet(
    @Embedded val key: NormalKey,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val names: List<Names>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val moneys: List<Moneys>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val things: List<Things>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val phones: List<Phones>,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val emails: List<Emails>
)