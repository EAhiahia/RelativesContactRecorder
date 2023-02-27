package com.limboooo.contactrecorder.repository.room

import androidx.lifecycle.*
import com.limboooo.contactrecorder.repository.room.entity.NormalDataSet
import com.limboooo.contactrecorder.repository.room.entity.RelativesInfoWhole
import kotlinx.coroutines.launch
import kotlin.math.sign

class ProjectViewModel(private val state: SavedStateHandle) : ViewModel() {

    init {
        viewModelScope.launch {
            dao.getAllContact().collect {
                allData = it
            }
            dao.getNormalDataSet().collect {
                normalSet = it
            }
        }
    }

    lateinit var targetContact: RelativesInfoWhole

    //数据库的操作列表
    private val dao by lazy { ProjectDatabase.getDatabase().projectDao() }

    //给list双向绑定使用
    suspend fun getAllContact() = dao.getAllContact()
    suspend fun getNormalSet() = dao.getNormalDataSet()

    //给日常操作用
    lateinit var allData: List<RelativesInfoWhole>
    lateinit var normalSet: NormalDataSet


    //返回值小于等于0就是没有成功
    suspend fun save(isNew: Boolean, new: RelativesInfoWhole) =
        if (isNew) dao.insert(new).size else dao.update(new)

    fun updateNormalSet(new: NormalDataSet) = dao.updateNormalSet(new)


}