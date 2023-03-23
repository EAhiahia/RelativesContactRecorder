package com.limboooo.contactrecorder.repository.room

import androidx.room.*
import com.limboooo.contactrecorder.repository.normalDataOwnerId
import com.limboooo.contactrecorder.repository.room.entity.normal.Moneys
import com.limboooo.contactrecorder.repository.room.entity.normal.Names
import com.limboooo.contactrecorder.repository.room.entity.normal.Things
import com.limboooo.contactrecorder.repository.room.entity.whole.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    //获取所有baseInfo
    @Query("SELECT * FROM RelativesBaseInfo")
    fun getAllBaseInfo(): Flow<MutableList<RelativesBaseInfo>>

    //删除某个用户
    @Transaction
    fun deleteOneUser(one: RelativesBaseInfo) {
        //获得整体
        val whole = getOneUser(one.id)
        whole?.let {
            deleteBaseInfo(one)
            deletePhones(it.phones)
            deleteEmails(it.emails)
            deleteReceived(it.moneyReceived)
            deleteReceivedBack(it.moneyReceivedBack)
            deleteGave(it.moneyGave)
            deleteGaveBack(it.moneyGaveBack)
        }
    }

    @Query("SELECT * FROM Phones WHERE ownerId = :id")
    fun queryTargetPhones(id: Long): List<Phones>

    @Delete
    fun deleteBaseInfo(one: RelativesBaseInfo)

    @Delete
    fun deleteGaveBack(moneyGaveBack: MutableList<MoneyGaveBack>)

    @Delete
    fun deleteGave(moneyGave: MutableList<MoneyGave>)

    @Delete
    fun deleteReceivedBack(moneyReceivedBack: MutableList<MoneyReceivedBack>)

    @Delete
    fun deleteReceived(moneyReceived: MutableList<MoneyReceived>)

    @Delete
    fun deleteEmails(emails: MutableList<Emails>)

    @Delete
    fun deletePhones(phones: MutableList<Phones>)

    //新增一个User
    @Transaction
    fun insertOneUser(one: RelativesInfoWhole) {
        deleteOneUser(one.baseInfo)
        val id = insertOneBaseInfo(one.baseInfo)
        one.phones.forEach {
            it.ownerId = id
        }
        one.emails.forEach {
            it.ownerId = id
        }
        one.moneyGave.forEach {
            it.ownerId = id
        }
        one.moneyGaveBack.forEach {
            it.ownerId = id
        }
        one.moneyReceived.forEach {
            it.ownerId = id
        }
        one.moneyReceivedBack.forEach {
            it.ownerId = id
        }
        insertPhones(one.phones)
        insertEmails(one.emails)
        insertReceived(one.moneyReceived)
        insertReceivedBack(one.moneyReceivedBack)
        insertGave(one.moneyGave)
        insertGaveBack(one.moneyGaveBack)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGaveBack(back: List<MoneyGaveBack>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReceivedBack(back: List<MoneyReceivedBack>)

    //插入baseInfo，如果有重复的，覆盖之后id不会变
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneBaseInfo(baseInfo: RelativesBaseInfo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhones(phones: List<Phones>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmails(emails: List<Emails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReceived(received: List<MoneyReceived>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGave(gave: List<MoneyGave>)

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhone(phone: Phones)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmail(email: Emails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertThing(thing: Things)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertName(name: Names)

    @Transaction
    @Query("SELECT * FROM RelativesBaseInfo WHERE id = :id")
    fun getOneUser(id: Long): RelativesInfoWhole?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOriginDownList() {
        insertThing(Things(0, normalDataOwnerId, "结婚"))
        insertThing(Things(0, normalDataOwnerId, "丧事"))
        insertThing(Things(0, normalDataOwnerId, "搬家"))
        insertThing(Things(0, normalDataOwnerId, "结婚"))
        insertThing(Things(0, normalDataOwnerId, "满月酒"))
        insertThing(Things(0, normalDataOwnerId, "新生儿"))
        insertMoney(Moneys(0, normalDataOwnerId, "100"))
        insertMoney(Moneys(0, normalDataOwnerId, "200"))
        insertMoney(Moneys(0, normalDataOwnerId, "300"))
        insertMoney(Moneys(0, normalDataOwnerId, "500"))
        insertMoney(Moneys(0, normalDataOwnerId, "1000"))
    }

    @Insert
    fun insertMoney(money: Moneys)
}