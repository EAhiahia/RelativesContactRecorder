package com.limboooo.contactrecorder.repository.room

import androidx.room.*
import com.limboooo.contactrecorder.repository.room.entity.NormalDataSet
import com.limboooo.contactrecorder.repository.room.entity.RelativesInfoWhole
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Transaction
    @Query("SELECT * FROM RelativesBaseInfo")
    suspend fun getAllContact(): Flow<List<RelativesInfoWhole>>

    @Transaction
    @Query("SELECT * FROM RelativesBaseInfo WHERE name = :name")
    suspend fun findByName(name: String): Flow<RelativesInfoWhole>

    //查询目标关键字是否存在
    @Query("SELECT * FROM Names WHERE name=:what")
    suspend fun isNameExist(what: String): Int

    @Query("SELECT * FROM Exchanges WHERE money=:what")
    suspend fun isMoneyExist(what: String): Int

    @Query("SELECT * FROM Exchanges WHERE thing=:what")
    suspend fun isThingExist(what: String): Int

    @Query("SELECT * FROM Phones WHERE phone=:what")
    suspend fun isPhoneExist(what: String): Int

    @Query("SELECT * FROM Emails WHERE email=:what")
    suspend fun isEmailExist(what: String): Int

    //返回每个的rowId
    @Transaction
    @Insert
    suspend fun insert(vararg contact: RelativesInfoWhole): List<Long>

    //返回值代表删除的行数
    @Transaction
    @Delete
    suspend fun delete(vararg contact: RelativesInfoWhole): Int

    //返回值代表更新的行数
    @Transaction
    @Update
    suspend fun update(vararg contact: RelativesInfoWhole): Int

    @Transaction
    @Query("SELECT * FROM NormalDataSet ORDER BY moneys ASC")
    suspend fun getNormalDataSet(): Flow<NormalDataSet>

    @Transaction
    @Update
    fun updateNormalSet(new: NormalDataSet)
}