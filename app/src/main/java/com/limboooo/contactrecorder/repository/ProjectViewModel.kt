package com.limboooo.contactrecorder.repository

import android.util.Log
import androidx.fragment.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.fragivity.NavOptions
import com.limboooo.contactrecorder.repository.room.ProjectDatabase
import com.limboooo.contactrecorder.repository.room.entity.normal.Names
import com.limboooo.contactrecorder.repository.room.entity.normal.Things
import com.limboooo.contactrecorder.repository.room.entity.whole.*
import com.nlf.calendar.Solar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

const val normalDataOwnerId = 1000000


/**
 * 每次更新mutableStateFlow.value会导致走流程，相当于emit
 * StateFlow用于观察，但是如果mutableStateFlow的数据和上次一样，就不会走collect，相当于多了个校验过程
 */
class ProjectViewModel : ViewModel() {

    //用于保存新的用户或者更新
    lateinit var targetData: RelativesInfoWhole

    //用于RecyclerView保存
    var inputPhone = mutableListOf(Phones(0, 0, ""))
    var inputEmail = mutableListOf(Emails(0, 0, ""))
    var inputThingReceived = mutableListOf(MoneyReceived(0, 0, "", "", ""))
    var inputThingGave = mutableListOf(MoneyGave(0, 0, "", "", ""))
    var inputName = ""

    var today: Solar = Solar.fromDate(Date())

    //用于RecyclerView判断是否有输入
    var isHaveContent = false

    //判断是否是删除模式，控制UI显示
    var deleteMode: Boolean = false

    //数据库的操作列表
    private val dao by lazy { ProjectDatabase.getDatabase().projectDao() }

    //主页面的list
    private val _mainListData: MutableStateFlow<List<RelativesBaseInfo>> =
        MutableStateFlow(listOf())
    val mainListData = _mainListData.asStateFlow()

    //用于辅助删除撤销操作
    val mainListDataBackup by lazy { mutableListOf<RelativesBaseInfo>() }

    //被删除的名单
    val deletedList: MutableList<RelativesBaseInfo> by lazy { mutableListOf() }

    //用于下拉栏
//    private val _names = MutableStateFlow(listOf<Names>())
//    private val _moneys = MutableStateFlow(listOf<Moneys>())
//    private val _things = MutableStateFlow(listOf<Things>())
//    private val _emails = MutableStateFlow(listOf<Emails>())
//    private val _phones = MutableStateFlow(listOf<Phones>())

    //    val names = _names.asStateFlow()
//    val moneys = _moneys.asStateFlow()
//    val things = _things.asStateFlow()
//    val emails = _emails.asStateFlow()
//    val phones = _phones.asStateFlow()
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
                    _mainListData.value = it
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

    private fun resetTargetData(): RelativesInfoWhole {
        return RelativesInfoWhole(
            RelativesBaseInfo(0, "", 0, 0),
            mutableListOf(Phones(0, 0, "")),
            mutableListOf(Emails(0, 0, "")),
            mutableListOf(MoneyReceived(0, 0, "$today   ${today.lunar}", "", "")),
            mutableListOf(MoneyGave(0, 0, "$today   ${today.lunar}", "", ""))
        )
    }

    fun saveAll() {
        if (deletedList.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            //todo 从全部列表中删除
            deletedList.forEach {
                dao.deleteOneUser(it)
            }
        }
    }

    fun saveTarget() {
        //todo 调用dao的update和add
        dao.insertOneUser(targetData)
    }

    suspend fun getOneUser(name: String): RelativesInfoWhole {
        return withContext(Dispatchers.IO) {
            dao.getOneUser(name)
        }
    }

    fun invalidateMainList() {
        viewModelScope.launch {
            _mainListData.value = dao.getAllBaseInfo().first()
        }
    }

    fun saveDownList(name: String) {
        inputPhone.forEachIndexed { index, phone ->
            if (index != inputPhone.size - 1 && !phones.contains(phone.phone))
                dao.insertPhone(
                    Phones(
                        0,
                        normalDataOwnerId,
                        phone.phone
                    )
                )
        }
        inputEmail.forEachIndexed { index, email ->
            if (index != inputEmail.size - 1 && !emails.contains(email.email))
                dao.insertEmail(
                    Emails(
                        0,
                        normalDataOwnerId,
                        email.email
                    )
                )
        }
        inputThingGave.forEachIndexed { index, thing ->
            if (index != inputThingGave.size - 1 && !things.contains(thing.thing))
                dao.insertThing(
                    Things(
                        0,
                        normalDataOwnerId,
                        thing.thing
                    )
                )
        }
        inputThingReceived.forEachIndexed { index, thing ->
            if (index != inputThingReceived.size - 1 && !things.contains(thing.thing))
                dao.insertThing(
                    Things(
                        0,
                        normalDataOwnerId,
                        thing.thing
                    )
                )
        }
        if (!names.contains(name)) dao.insertName(Names(0, normalDataOwnerId, name))
    }

    fun loadTargetDetail(position: Int) {
        targetData = dao.getOneUser(mainListData.value[position].id)
    }
}