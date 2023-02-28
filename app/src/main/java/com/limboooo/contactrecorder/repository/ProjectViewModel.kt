package com.limboooo.contactrecorder.repository

import android.content.Context
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
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesBaseInfo
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesInfoWhole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")
const val disableLog = true

fun String.showLog(tag: String = "myapp") {
    if (!disableLog) Log.d(tag, this)
}

fun NavOptions.initAnimator() {
    enterAnim = R.animator.fragment_open_enter
    exitAnim = R.animator.fragment_open_exit
    popEnterAnim = R.animator.fragment_close_enter
    popExitAnim = R.animator.fragment_close_exit
}


/**
 * 每次更新mutableStateFlow.value会导致走流程，相当于emit
 * StateFlow用于观察，但是如果mutableStateFlow的数据和上次一样，就不会走collect，相当于多了个校验过程
 */
class ProjectViewModel : ViewModel() {


    var deleteMode: Boolean = false

    //数据库的操作列表
    private val dao by lazy { ProjectDatabase.getDatabase().projectDao() }

    //全部数据
    private val _dataList: MutableStateFlow<List<RelativesInfoWhole>> =
        MutableStateFlow(mutableListOf())
    val dataList: StateFlow<List<RelativesInfoWhole>> = _dataList
    val dataListBackup: MutableList<RelativesInfoWhole> by lazy { mutableListOf() }

    //下拉栏数据
    private val _normalData = MutableStateFlow<List<NormalDataSet>>(mutableListOf())
    val normalData: StateFlow<List<NormalDataSet>> = _normalData
    val normalDataBackup: MutableList<NormalDataSet> by lazy { mutableListOf() }

    //主页面的list
    private val _mainListData: MutableStateFlow<List<RelativesBaseInfo>> =
        MutableStateFlow(mutableListOf())
    val mainListData: StateFlow<List<RelativesBaseInfo>> = _mainListData
    val mainListDataBackup: MutableList<RelativesBaseInfo> by lazy { mutableListOf() }

    //被删除的名单
    val deletedList: MutableList<RelativesBaseInfo> by lazy {
        mutableListOf()
    }

    init {
        viewModelScope.launch {
            launch {
                dao.getAll().collect {
                    _dataList.value = it
                    dataListBackup.run {
                        clear()
                        addAll(it)
                    }
                }
            }
            launch {
                dao.getNormal().collect {
                    _normalData.value = it
                    normalDataBackup.run {
                        clear()
                        addAll(it)
                    }
                }
            }
        }
    }

    fun save() {
        if (deletedList.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            deletedList.forEach {
                dao.deleteFromAll(it)
            }
        }
    }


}