package com.limboooo.contactrecorder.repository.room

import androidx.room.*
import com.limboooo.contactrecorder.repository.room.entity.normal.Moneys
import com.limboooo.contactrecorder.repository.room.entity.normal.Names
import com.limboooo.contactrecorder.repository.room.entity.normal.Things
import com.limboooo.contactrecorder.repository.room.entity.whole.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    //获取所有baseInfo
    @Query("SELECT * FROM RelativesBaseInfo")
    fun getAllBaseInfo(): Flow<List<RelativesBaseInfo>>

    //删除某个用户
    @Delete
    fun deleteOneUser(one: RelativesBaseInfo)

    //新增一个User
    @Transaction
    fun insertOneUser(one: RelativesInfoWhole) {
        deleteOneUser(one.baseInfo)
        val id = insertOneBaseInfo(one.baseInfo).toInt()
        insertPhones(id, one.phones)
        insertEmails(id, one.emails)
        insertReceived(id, one.moneyReceived)
        insertGave(id, one.moneyGave)
    }

    //插入baseInfo，如果有重复的，覆盖之后id不会变
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneBaseInfo(baseInfo: RelativesBaseInfo):Long

    @Insert
    fun insertPhones(id: Int, phones: List<Phones>) {
        phones.forEach {
            it.ownerId = id
        }
    }

    @Insert
    fun insertEmails(id: Int, emails: List<Emails>) {
        emails.forEach {
            it.ownerId = id
        }
    }

    @Insert
    fun insertReceived(id: Int, received: List<MoneyReceived>) {
        received.forEach {
            it.ownerId = id
        }
    }

    @Insert
    fun insertGave(id: Int, gave: List<MoneyGave>) {
        gave.forEach {
            it.ownerId = id
        }
    }

    // 查询某个人
    @Transaction
    @Query("SELECT * FROM RelativesBaseInfo WHERE name = :name")
    fun getOneUser(name: String): RelativesInfoWhole

    //查询下拉选项中的name
    @Query("SELECT * FROM Names WHERE ownerId = 1000000")
    fun getNames(): Flow<List<Names>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateNames(names: List<Names>)

    //查询下拉选项中的email
    @Query("SELECT * FROM Emails WHERE ownerId = 1000000")
    fun getEmails(): Flow<List<Emails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateEmails(names: List<Emails>)

    //查询下拉选项中的phone
    @Query("SELECT * FROM Phones WHERE ownerId = 1000000")
    fun getPhones(): Flow<List<Phones>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updatePhones(names: List<Phones>)

    //查询下拉选项中的thing
    @Query("SELECT * FROM Things WHERE ownerId = 1000000")
    fun getThings(): Flow<List<Things>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateThings(names: List<Things>)

    //查询下拉选项中的money
    @Query("SELECT * FROM Moneys WHERE ownerId = 1000000")
    fun getMoneys(): Flow<List<Moneys>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateMoneys(names: List<Moneys>)

    @Insert
    fun insertPhone(phone: Phones)
    @Insert
    fun insertEmail(email: Emails)
    @Insert
    fun insertThing(thing: Things)
    @Insert
    fun insertName(name: Names)

    @Transaction
    @Query("SELECT * FROM RelativesBaseInfo WHERE id = :id")
    fun getOneUser(id: Int): RelativesInfoWhole
}