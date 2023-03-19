package com.limboooo.contactrecorder.repository.room.entity.whole

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 和某个亲戚往来的信息
 * 不定义tableName的话，类名默认作为表名
 */
@Entity
data class RelativesBaseInfo(
    @PrimaryKey(true) val id: Int,
    //基本信息
    val name: String,
    var moneyReceivedWhole: Int,
    var moneyGaveWhole: Int
)