package com.limboooo.contactrecorder.repository

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.fragivity.NavOptions
import com.limboooo.contactrecorder.repository.room.ProjectDatabase
import com.limboooo.contactrecorder.repository.room.entity.normal.NormalDataSet
import com.limboooo.contactrecorder.repository.room.entity.normal.NormalKey
import com.limboooo.contactrecorder.repository.room.entity.whole.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")
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

const val normalDataOwnerId = 1000000


/**
 * 每次更新mutableStateFlow.value会导致走流程，相当于emit
 * StateFlow用于观察，但是如果mutableStateFlow的数据和上次一样，就不会走collect，相当于多了个校验过程
 */
class ProjectViewModel : ViewModel() {

    var targetData: RelativesInfoWhole = resetTargetData()

    private fun resetTargetData(): RelativesInfoWhole {
        return RelativesInfoWhole(
            RelativesBaseInfo(0, "", 0, 0),
            mutableListOf(Phones(0, 0, "")),
            mutableListOf(Emails(0, 0, "")),
            mutableListOf(Exchanges(0, 0, today, "", "")),
            mutableListOf(Exchanges(0, 0, today, "", ""))
        )
    }

    var today: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    var isHaveContent = false
    var deleteMode: Boolean = false

    //数据库的操作列表
    private val dao by lazy { ProjectDatabase.getDatabase().projectDao() }

    //全部数据
    private val _dataList: MutableStateFlow<List<RelativesInfoWhole>> =
        MutableStateFlow(listOf())
    val dataList = _dataList.asStateFlow()

    //下拉栏数据
    private val _normalData = MutableStateFlow(
        NormalDataSet(
            NormalKey(1, ""),
            listOf(), listOf(), listOf(), listOf(), listOf()
        )
    )
    val normalData = _normalData.asStateFlow()

    //主页面的list
    private val _mainListData: MutableStateFlow<List<RelativesBaseInfo>> =
        MutableStateFlow(listOf())
    val mainListData = _mainListData.asStateFlow()
    val mainListDataBackup by lazy { mutableListOf<RelativesBaseInfo>() }

    //被删除的名单
    val deletedList: MutableList<RelativesBaseInfo> by lazy { mutableListOf() }
    private val _names = MutableStateFlow(listOf<String>())
    private val _moneys = MutableStateFlow(listOf<String>())
    private val _things = MutableStateFlow(listOf<String>())
    private val _emails = MutableStateFlow(listOf<String>())
    private val _phones = MutableStateFlow(listOf<String>())
    val names = _names.asStateFlow()
    val moneys = _moneys.asStateFlow()
    val things = _things.asStateFlow()
    val emails = _emails.asStateFlow()
    val phones = _phones.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {

            }
            launch {
                dao.getMainList().collect {
                    _mainListData.value = it
                    mainListDataBackup.run {
                        clear()
                        addAll(it)
                    }
                }
            }
            launch {

            }
            launch {
                dao.getNormalNames().collect {
                    _names.value = it
                }
            }
            launch {
                dao.getNormalEmails().collect {
                    _emails.value = it
                }
            }
            launch {
                dao.getNormalMoneys().collect {
                    _moneys.value = it
                }
            }
            launch {
                dao.getNormalPhones().collect {
                    _phones.value = it
                }
            }
            launch {
                dao.getNormalThings().collect {
                    _things.value = it
                }
            }
        }
    }

    fun save() {
        if (deletedList.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            deletedList.forEach { base ->

            }
        }
    }


}