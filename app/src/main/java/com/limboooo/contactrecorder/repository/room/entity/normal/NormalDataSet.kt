package com.limboooo.contactrecorder.repository.room.entity.normal

import androidx.room.Embedded
import androidx.room.Relation
import com.limboooo.contactrecorder.repository.room.entity.whole.Emails
import com.limboooo.contactrecorder.repository.room.entity.whole.Phones

data class NormalDataSet(
    @Embedded val key: NormalKey,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val names: List<Names>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val moneys: List<Moneys>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val things: List<Things>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val phones: List<Phones>,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ownerUid"
    )
    val emails: List<Emails>
)