package com.ruanyun.australianews.util;


import com.ruanyun.australianews.App;
import com.ruanyun.australianews.model.*;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:数据库管理类
 * author: zhangsan on 16/11/21 下午5:39.
 */

public class DbHelper {

    private static DbHelper instance;
    DaoMaster daoMaster;
    DaoSession daoSession;

    private ParentCodeInfoDao parentCodeInfoDao;
    private ChannelInfoDao channelInfoDao;
    private HistorySearchInfoDao searchHistoryDao;

    public static DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }


    public void initHelper(DaoMaster daoMaster, DaoSession daoSession) {
        this.daoMaster = daoMaster;
        this.daoSession = daoSession;

        parentCodeInfoDao = daoSession.getParentCodeInfoDao();
        channelInfoDao = daoSession.getChannelInfoDao();
        searchHistoryDao = daoSession.getHistorySearchInfoDao();
    }


////////////////////////////////////// 字典表数据 ///////////////////////////////////////////////////////////
public void insertParentCodes(String parentCode, List<ParentCodeInfo> infoList) {
    parentCodeInfoDao.deleteInTx(getParentCodeList(parentCode));
    daoSession.startAsyncSession().insertOrReplaceInTx(ParentCodeInfo.class, infoList);
}

    public Long getParentCodeInfoSize() {
        return parentCodeInfoDao.count();
    }

    public List<ParentCodeInfo> getAll() {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .build();
        return query.list();
    }

    public List<ParentCodeInfo> getParentCodeList(String parentCode) {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .build();
        return query.list();
    }

    public List<ParentCodeInfo> getParentCodeListAndAll(String parentCode, String name, String listShowName) {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .build();
        List<ParentCodeInfo> list = new ArrayList<>();
        list.add(new ParentCodeInfo(name, listShowName, ""));
        list.addAll(query.list());
        return list;
    }

    public String getItemName(String code, String parentCode) {
        if (code == null) return "";
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .where(ParentCodeInfoDao.Properties.Code.eq(code))
                .build();
        ParentCodeInfo info = query.unique();
        if (info == null) return code;
        return info.name;
    }

    public String getItemNames(String codes, String parentCode) {
        if (codes == null) return "";
        String names = "";
        String[] nameList = codes.split(",");
        for (String s : nameList) {
            if(CommonUtil.isNotEmpty(names)){
                names += "、";
            }
            names += getItemName(s, parentCode);
        }
        return names;
    }

    public String getItemNameByParentCode(String parentCode) {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .build();
        ParentCodeInfo info = query.unique();
        if (info == null) return parentCode;
        return info.name;
    }

    public String getItemNameByParentCode(String parentCode, String code) {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .where(ParentCodeInfoDao.Properties.Code.eq(code))
                .build();
        ParentCodeInfo info = query.unique();
        if (info == null) return "";
        return info.name;
    }

    public ParentCodeInfo getItemByParentCode(String parentCode, String code) {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .where(ParentCodeInfoDao.Properties.Code.eq(code))
                .build();
        return query.unique();
    }

    public String getItemCodeByParentCode(String parentCode) {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .build();
        ParentCodeInfo info = query.unique();
        if (info == null) return parentCode;
        return info.name;
    }

    public String getItemCodeByParentCode(String parentCode, String name) {
        Query<ParentCodeInfo> query = parentCodeInfoDao.queryBuilder()
                .where(ParentCodeInfoDao.Properties.ParentCode.eq(parentCode))
                .where(ParentCodeInfoDao.Properties.Name.eq(name))
                .build();
        ParentCodeInfo info = query.unique();
        if (info == null) return name;
        return info.name;
    }


////////////////////////////////////// 频道管理 ///////////////////////////////////////////////////////////
    public void insertChannelList(List<ChannelInfo> infoList) {
        channelInfoDao.deleteInTx(getChannelList(App.getInstance().isLogin()));
        daoSession.startAsyncSession().insertOrReplaceInTx(ChannelInfo.class, infoList);
    }

    public void addChannelInfo(ChannelInfo info) {
        daoSession.startAsyncSession().insertOrReplaceInTx(ChannelInfo.class, info);
    }

    public List<ChannelInfo> getChannelList(boolean isLogin) {
        Query<ChannelInfo> query = channelInfoDao.queryBuilder()
                .where(ChannelInfoDao.Properties.IsLogin.eq(isLogin))
                .orderAsc(ChannelInfoDao.Properties.Sort)
                .build();
        return query.list();
    }

    public ChannelInfo getCustomizeChannelInfo(String title) {
        Query<ChannelInfo> query = channelInfoDao.queryBuilder()
                .where(ChannelInfoDao.Properties.IsLogin.eq(true))
                .where(ChannelInfoDao.Properties.Type.eq(2))
                .where(ChannelInfoDao.Properties.Title.eq(title))
                .build();
        return query.unique();
    }

    public List<ChannelInfo> getSubscribedList(boolean isLogin) {
        Query<ChannelInfo> query = channelInfoDao.queryBuilder()
                .where(ChannelInfoDao.Properties.IsLogin.eq(isLogin), ChannelInfoDao.Properties.IsSubscribe.eq(true)).orderAsc(ChannelInfoDao.Properties.Sort)
                .build();
        List<ChannelInfo> list = query.list();

        if (list.size()>0) {
    //        list.add(2,new ChannelInfo(false, 0, true, "行情", "00000000000000000000", 1, 2, 1));
    //        list.add(1,new ChannelInfo(false, 0, true, "7*24", "00000000000000000000", 1, 2, 1));
//              list.add(new ChannelInfo(false, 0, true, "其他", "00000000000000000000", 1, 2, 1));
        }
        return list;
    }

    /**
     * 获取没有订阅的系统频道
     * @param isLogin
     * @return
     */
    public List<ChannelInfo> getUnSubscribedList(boolean isLogin) {
        Query<ChannelInfo> query = channelInfoDao.queryBuilder()
                .where(ChannelInfoDao.Properties.IsLogin.eq(isLogin),
                        ChannelInfoDao.Properties.IsSubscribe.eq(false),
                        ChannelInfoDao.Properties.Type.eq(1))
                .orderAsc(ChannelInfoDao.Properties.Sort)
                .build();
        return query.list();
    }

    /**
     * 获取没有订阅的自定义个人频道
     * @param isLogin
     * @return
     */
    public List<ChannelInfo> getUnSubscribedCustomizeList(boolean isLogin) {
        Query<ChannelInfo> query = channelInfoDao.queryBuilder()
                .where(ChannelInfoDao.Properties.IsLogin.eq(isLogin),
                        ChannelInfoDao.Properties.IsSubscribe.eq(false),
                        ChannelInfoDao.Properties.Type.eq(2))
                .orderAsc(ChannelInfoDao.Properties.Sort)
                .build();
        return query.list();
    }


///////////////////////////////////////// 搜索历史 ///////////////////////////////////////////////////////////
    public void insertHistorySearchInfo(String cotent) {
        HistorySearchInfo searchHistory = new HistorySearchInfo();
        searchHistory.time = System.currentTimeMillis();
        searchHistory.keyWord = cotent;
        searchHistoryDao.insertOrReplace(searchHistory);
    }

    public List<HistorySearchInfo> getSearchHistory() {
        Query<HistorySearchInfo> query = searchHistoryDao.queryBuilder()
                .orderDesc(HistorySearchInfoDao.Properties.Time)
                .build();
        List<HistorySearchInfo> result = query.list();
        if (result.size() > 10) {
            return result.subList(0, 9);
        }
        return query.list();
    }

    public void clearSearchHistory() {
        searchHistoryDao.deleteAll();
    }


}
