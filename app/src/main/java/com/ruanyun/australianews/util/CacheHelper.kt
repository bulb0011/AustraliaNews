package com.ruanyun.australianews.util

import android.os.Handler
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.ruanyun.australianews.App
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.*
import jiguang.chat.database.UserEntry
import jiguang.chat.entity.EventNotifyUnread
import jiguang.chat.utils.SharePreferenceManager
import java.util.*

/**
 * Description:缓存工具类
 * author: zhangsan on 17/1/10 上午9:21.
 */
class CacheHelper internal constructor() {
    private var cityInfoList: MutableList<CityInfo>? = null

    lateinit var apiService: ApiService

    lateinit var dbHelper: DbHelper

    val lastLoginUser: UserInfo?
        get() = GsonUtil.parseJson(PrefUtility.get(C.PrefName.PREF_LOGIN_USER_INFO, ""), UserInfo::class.java)

    var lastLoginName: String?
        get() = PrefUtility.get(C.PrefName.PREF_LOGIN_NAME, null)
        set(loginNum) = PrefUtility.put(C.PrefName.PREF_LOGIN_NAME, loginNum)

    var lastLoginType: Boolean
        get() = PrefUtility.getBoolean(C.PrefName.PREF_LOGIN_TYPE, true)
        set(isPhoneLogin) = PrefUtility.put(C.PrefName.PREF_LOGIN_TYPE, isPhoneLogin)

    val unreadCount: Int
        get() = PrefUtility.get(App.getInstance().userOid + C.PrefName.UNREAD_SYSTEM_COUNT, 0)

    var isFirstInApp: Boolean
        get() = PrefUtility.getBoolean(C.PrefName.PREF_IS_FIRSTIN_APP, true)
        set(isFirstInApp) = PrefUtility.put(C.PrefName.PREF_IS_FIRSTIN_APP, isFirstInApp)

    var isTurnOnPush: Boolean
        get() = PrefUtility.getBoolean(C.PrefName.IS_TURN_ON_PUSH, true)
        set(isTurnOnPush) {
            PrefUtility.put(C.PrefName.IS_TURN_ON_PUSH, isTurnOnPush)
            if (isTurnOnPush) {
                JPushHelper.setAlias(App.getInstance().userOid)
            } else {
                JPushHelper.setAlias("")
            }
        }


    fun initHelper(apiService: ApiService, dbHelper: DbHelper) {
        this.apiService = apiService
        this.dbHelper = dbHelper
    }

    fun saveUser(user: UserInfo?) {
        PrefUtility.put(C.PrefName.PREF_LOGIN_USER_INFO, GsonUtil.toJson(user))
    }

    fun addUnreadCount() {
        val count = unreadCount + 1
        PrefUtility.put(App.getInstance().userOid + C.PrefName.UNREAD_SYSTEM_COUNT, count)
    }

    fun clearUnreadCount() {
        PrefUtility.put(App.getInstance().userOid + C.PrefName.UNREAD_SYSTEM_COUNT, 0)
    }

    fun setFirstInStatus(f: Boolean) {
        PrefUtility.put(C.PrefName.PREF_IS_FIRSTIN_APP, f)
    }

    var cityName: String?
        get() = PrefUtility.get(C.PrefName.PREF_CITY_NAME, "悉尼")
        set(cityName) = PrefUtility.put(C.PrefName.PREF_CITY_NAME, cityName)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取初始化缓存数据
     */
    fun getInitCacheList() {
        if (dbHelper.parentCodeInfoSize > 0) {
            Handler().postDelayed({
                getParentCodeList()
            }, 3000)
        } else {
            getParentCodeList()
        }
//        requestAllCityList()
        requestChannelList()
    }


    /**
     * 获取所有字典表数据
     */
    fun getParentCodeList() {
        apiService.parentCodeList
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<HashMap<String, List<ParentCodeInfo>>>>() {
                    override fun onSuccess(result: ResultBase<HashMap<String, List<ParentCodeInfo>>>) {

                        if (result.data != null) {
                            for (s in result.data.keys) {
                                val infoList = result.data[s] ?: continue
                                for (parentCodeInfo in infoList) {
                                    parentCodeInfo.parentCode = s
                                }
                                dbHelper.insertParentCodes(s, infoList)
                            }
                        }
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        super.onFail(msg)
                        LogX.e("retrofit", "onFail() : msg = [$msg]")
                    }
                })
    }

    /**
     * 获取全部城市
     */
    private fun requestAllCityList() {
        apiService.allCityList
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<MutableList<CityInfo>>>() {
                    override fun onSuccess(result: ResultBase<MutableList<CityInfo>>) {
                        cityInfoList = result.data
                        for (cityInfo in cityInfoList!!) {
                            val classify = cityInfo.cityName.substring(0, 1)
                            if (classify >= "A" && classify <= "z") {
                                cityInfo.classify = classify.toUpperCase()
                            } else {
                                cityInfo.classify = StringUtil.getPinYinFirstLetter(cityInfo.cityName)
                            }
                        }
                        cityInfoList!!.sortWith(Comparator { lhs, rhs -> lhs.classify.compareTo(rhs.classify) })
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        super.onError(erroCode, erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        super.onFail(msg)
                    }
                })
    }


    /**
     * 获取频道列表
     */
    fun requestChannelList() {

        LogX.e("dengpao","userOid"+App.getInstance().userOid)

        apiService.getChannelList(App.getInstance().userOid)
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<ChannelResult>>() {
                    override fun onSuccess(result: ResultBase<ChannelResult>) {
                        val infoList = ArrayList<ChannelInfo>()
                        val isLogin = App.getInstance().isLogin!!
                        if (result.data.channelRelationListChoose != null && result.data.channelRelationListChoose.size > 0) {
                            for (datum in result.data.channelRelationListChoose) {
                                datum.isSubscribe = true
                                datum.isLogin = isLogin
                            }
                            infoList.addAll(result.data.channelRelationListChoose)
                        }
                        if (result.data.channelRelationListNoChoose != null && result.data.channelRelationListNoChoose.size > 0) {
                            for (datum in result.data.channelRelationListNoChoose) {
                                datum.isSubscribe = false
                                datum.isLogin = isLogin
                                datum.isTop = 2//不置顶
                                datum.isFixed = 2//不固定
                                datum.type = 1
                            }
                            infoList.addAll(result.data.channelRelationListNoChoose)
                        }
                        if (result.data.customChannelListNoChoose != null && result.data.customChannelListNoChoose.size > 0) {
                            for (datum in result.data.customChannelListNoChoose) {
                                datum.isSubscribe = false
                                datum.isLogin = isLogin
                                datum.isTop = 2
                                datum.isFixed = 2
                                datum.type = 2
                            }
                            infoList.addAll(result.data.customChannelListNoChoose)
                        }

//                        if (isLogin) {
                            dbHelper.insertChannelList(infoList)
                            EventNotifier.getInstance().updateChannelList()
//                        } else {
//                            val isDataSame = ChannelInfo.isDataSame(dbHelper.getChannelList(false), infoList)
//                            if (!isDataSame) {
//                                dbHelper.insertChannelList(infoList)
//                                EventNotifier.getInstance().updateChannelList()
//                            }
//                        }
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        super.onError(erroCode, erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        super.onFail(msg)
                    }
                })
    }

    fun getAllCityInfoList(): List<CityInfo> {
        return if (cityInfoList?.isNotEmpty() == true) {
            cityInfoList!!
        } else {
            requestAllCityList()
            ArrayList()
        }
    }

    fun getAllCityInfoListAndUnlimited(allName: String): List<CityInfo> {
        return if (cityInfoList?.isNotEmpty() == true) {
            val list = mutableListOf<CityInfo>()
            list.add(0, CityInfo(allName, CITY_UNLIMITED_STR, ""))
            list.addAll(cityInfoList!!)
            list
        } else {
            requestAllCityList()
            ArrayList()
        }
    }

    /**
     * 登录注册成功以后处理
     * @param userInfo
     * @param lastLoginType
     * @param lastLoginName
     */
    fun loginRegisteredSuccess(userInfo: UserInfo, lastLoginType: Boolean, lastLoginName: String) {
        CacheHelper.getInstance().lastLoginType = lastLoginType
        CacheHelper.getInstance().lastLoginName = lastLoginName
        App.getInstance().user = userInfo
        EventNotifier.getInstance().updateUserInfo()
        requestChannelList()
        imLogin()
    }

    var isImLoginSuccess: Boolean = false
    /**
     * 极光聊天登录
     */
    fun imLogin() {
        val user = App.getInstance().user ?: return
        if (isTurnOnPush) {
            JPushHelper.setAlias(App.getInstance().userOid)
        } else {
            JPushHelper.setAlias("")
        }
        JMessageClient.login(user.oid, user.loginPass, object : BasicCallback() {
            override fun gotResult(responseCode: Int, responseMessage: String) {
                LogX.e(
                        "retrofit",
                        "gotResult() called with: responseCode = [$responseCode], responseMessage = [$responseMessage]"
                )
                if (responseCode == 0) {
                    SharePreferenceManager.setCachedPsw(user.loginPass)
                    val myInfo = JMessageClient.getMyInfo()
                    val avatarFile = myInfo.avatarFile
                    //登陆成功,如果用户有头像就把头像存起来,没有就设置null
                    if (avatarFile != null) {
                        SharePreferenceManager.setCachedAvatarPath(avatarFile.absolutePath)
                    } else {
                        SharePreferenceManager.setCachedAvatarPath(null)
                    }
                    val username = myInfo.userName
                    val appKey = myInfo.appKey
                    var userEntry: UserEntry? = UserEntry.getUser(username, appKey)
                    if (null == userEntry) {
                        userEntry = UserEntry(username, appKey)
                        userEntry.save()
                    }
                    EventNotifyUnread.updateUnreadCount()
                    isImLoginSuccess = true
                } else {
                    isImLoginSuccess = false
                    LogX.e("retrofit", "gotResult: 极光IM登录失败")
                }
            }
        })
    }

    fun imLogout() {
        JMessageClient.logout()
        JPushHelper.setAlias("")
        EventNotifyUnread.updateUnreadCount()
        requestChannelList()
    }

    companion object {

        const val CITY_UNLIMITED_STR = "不限"
        private var instance: CacheHelper? = null

        fun getInstance(): CacheHelper {
            if (null == instance) {
                instance = CacheHelper()
            }
            return instance!!
        }
    }

}
