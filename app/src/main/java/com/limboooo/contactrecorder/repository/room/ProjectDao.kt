package com.limboooo.contactrecorder.repository.room

import androidx.room.*
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesBaseInfo
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesInfoWhole
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    //获取所有人的基本信息
    @Query("SELECT * FROM RelativesBaseInfo")
    suspend fun getMainList(): Flow<List<RelativesBaseInfo>>

    // 删除某个人的信息
    @Delete
    fun deleteOneUser(one: RelativesBaseInfo)

    // 查询某个人
    @Query("SELECT * FROM RelativesBaseInfo WHERE id = :id")
    fun getOneUser(id: Int): RelativesInfoWhole

    //查询下拉选项中的name
    @Query("SELECT name FROM Names WHERE ownerUid = 1000000")
    suspend fun getNormalNames(): Flow<List<String>>

    //查询下拉选项中的email
    @Query("SELECT email FROM Emails WHERE ownerUid = 1000000")
    suspend fun getNormalEmails(): Flow<List<String>>

    //查询下拉选项中的phone
    @Query("SELECT phone FROM Phones WHERE ownerUid = 1000000")
    suspend fun getNormalPhones(): Flow<List<String>>

    //查询下拉选项中的thing
    @Query("SELECT thing FROM Things WHERE ownerUid = 1000000")
    suspend fun getNormalThings(): Flow<List<String>>

    //查询下拉选项中的money
    @Query("SELECT money FROM Moneys WHERE ownerUid = 1000000")
    suspend fun getNormalMoneys(): Flow<List<String>>
}