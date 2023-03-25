package com.limboooo.contactrecorder.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.R
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.fragivity.NavOptions
import com.limboooo.contactrecorder.activity.ProjectApplication
import com.limboooo.contactrecorder.repository.room.ProjectDatabase
import com.limboooo.contactrecorder.repository.room.entity.normal.Names
import com.limboooo.contactrecorder.repository.room.entity.normal.Things
import com.limboooo.contactrecorder.repository.room.entity.whole.Emails
import com.limboooo.contactrecorder.repository.room.entity.whole.Phones
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesBaseInfo
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesInfoWhole
import com.limboooo.contactrecorder.tools.showShortToast
import com.nlf.calendar.Solar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val disableLog = false

fun String.showLog(tag: String = "myapp") {
    if (!disableLog) Log.d(tag, this)
}

fun NavOptions.initAnimator() {
    enterAnim = R.animator.fragment_open_enter
    exitAnim = R.animator.fragment_open_exit
    popEnterAnim = R.animator.fragment_close_enter
    popExitAnim = R.animator.fragment_close_exit
}

const val normalDataOwnerId = 1000000L

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val INSERT_DEFAULT_DOWN_LIST = booleanPreferencesKey("inserted")

/**
 * 每次更新mutableStateFlow.value会导致走流程，相当于emit
 * StateFlow用于观察，但是如果mutableStateFlow的数据和上次一样，就不会走collect，相当于多了个校验过程
 */
class ProjectViewModel : ViewModel() {

    var targetDataBackup: RelativesInfoWhole? = null

    //用于保存新的用户或者更新
    lateinit var targetData: RelativesInfoWhole

    suspend fun getInserted(): Boolean {
        return ProjectApplication.context.dataStore.data.map {
            it[INSERT_DEFAULT_DOWN_LIST] ?: false
        }.first()
    }

    var today: Solar = Solar.fromDate(Date())

    //判断是否是删除模式，控制UI显示
    var deleteMode = MutableLiveData(false)

    //数据库的操作列表
    private val dao by lazy { ProjectDatabase.getDatabase().projectDao() }

    //主页面的list
    val mainListData: MutableStateFlow<MutableList<RelativesBaseInfo>> =
        MutableStateFlow(mutableListOf())

    var mainListDataBackup = mutableListOf<RelativesBaseInfo>()

    //被删除的名单
    val deletedList = MutableLiveData(mutableListOf<RelativesBaseInfo>())

    //用于下拉栏
    lateinit var names: MutableList<String>
    lateinit var moneys: MutableList<String>
    lateinit var things: MutableList<String>
    lateinit var emails: MutableList<String>
    lateinit var phones: MutableList<String>


    init {
        viewModelScope.launch(Dispatchers.IO) {
            launchDownList()
            launch {
                dao.getAllBaseInfo().collect {
                    mainListData.value = it
                }
            }
        }
    }

    private fun CoroutineScope.launchDownList() {
        launch {
            dao.getNames().collect { list ->
                names = MutableList(list.size) {
                    list[it].name
                }
            }
        }
        launch {
            dao.getEmails().collect { list ->
                emails = MutableList(list.size) {
                    list[it].email
                }
            }
        }
        launch {
            dao.getPhones().collect { list ->
                phones = MutableList(list.size) {
                    list[it].phone
                }
            }
        }
        launch {
            dao.getThings().collect { list ->
                things = MutableList(list.size) {
                    list[it].thing
                }
            }
        }
        launch {
            dao.getMoneys().collect { list ->
                moneys = MutableList(list.size) {
                    list[it].money
                }
            }
        }
    }

    suspend fun insertOriginDownList() {
        dao.insertOriginDownList()
        ProjectApplication.context.dataStore.edit {
            it[INSERT_DEFAULT_DOWN_LIST] = true
        }
    }

    fun saveAll() {
        viewModelScope.launch(Dispatchers.IO) {
            deletedList.value!!.forEach {
                dao.deleteOneUser(it)
            }
            deletedList.value!!.clear()
            launch(Dispatchers.Main) {
                "保存完成".showShortToast()
            }
        }
    }

    fun saveTarget() {
        targetData.toString().showLog()
        dao.insertOneUser(targetData)
    }

    suspend fun getOneUser(name: String): RelativesInfoWhole {
        return withContext(Dispatchers.IO) {
            dao.getOneUser(name)
        }
    }

    suspend fun invalidateMainList() {
        mainListData.value = dao.getAllBaseInfo().first()
    }

    fun saveDownList(name: String) {
        targetData.phones.forEach {
            if (!phones.contains(it.phone))
                dao.insertPhone(
                    Phones(
                        0,
                        normalDataOwnerId,
                        it.phone
                    )
                )
        }
        targetData.emails.forEach {
            if (!emails.contains(it.email))
                dao.insertEmail(
                    Emails(
                        0,
                        normalDataOwnerId,
                        it.email
                    )
                )
        }
        targetData.moneyReceived.forEach {
            if (!things.contains(it.thing))
                dao.insertThing(
                    Things(
                        0,
                        normalDataOwnerId,
                        it.thing
                    )
                )
        }
        targetData.moneyGave.forEach {
            if (!things.contains(it.thing))
                dao.insertThing(
                    Things(
                        0,
                        normalDataOwnerId,
                        it.thing
                    )
                )
        }
        if (!names.contains(name)) dao.insertName(Names(0, normalDataOwnerId, name))
    }

    fun loadTargetDetail(id: Long) {
        targetData = dao.getOneUser(id)!!
    }

    fun deleteTargetUser() {
        dao.deleteOneUser(targetData.baseInfo)
    }
}