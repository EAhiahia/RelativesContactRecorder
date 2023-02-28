package com.limboooo.contactrecorder.repository.room

import androidx.room.*
import com.limboooo.contactrecorder.repository.room.entity.normal.NormalDataSet
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesBaseInfo
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesInfoWhole
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Transaction
    @Query("SELECT * FROM RelativesBaseInfo")
    suspend fun getAll(): Flow<List<RelativesInfoWhole>>

    //返回值代表删除的行数
    //todo 不知道怎么删除关联项
    @Transaction
    @Delete
    suspend fun deleteFromAll(vararg contact: RelativesBaseInfo): Int

    @Transaction
    @Query("SELECT * FROM NormalKey")
    suspend fun getNormal():Flow<List<NormalDataSet>>

    


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