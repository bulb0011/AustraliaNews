package com.ruanyun.australianews.util;

/**
 * Description:常量类
 * author: zhangsan on 16/11/21 下午6:22.
 */

public final class C {

    public static final class PrefName {
        /** 保存登录用户名 **/
        public static final String PREF_LOGIN_NAME = "pref_login_name";
        /** 保存城市 **/
        public static final String PREF_CITY_NAME = "pref_city_name";
        /** 保存登录类型 **/
        public static final String PREF_LOGIN_TYPE = "pref_login_type";
        /** 保存用户信息 **/
        public static final String PREF_LOGIN_USER_INFO = "pref_login_user_info";
        /** cookie信息 **/
        public static final String COOKIE_PHPSESSID = "COOKIE_PHPSESSID";
        /** SERVICE_TEL **/
        public static final String SERVICE_TEL = "service_tel";
        /** 未读消息通知数量 **/
        public static final String UNREAD_SYSTEM_COUNT = "unread_system_count";
        /** 未读消息通知数量 **/
        public static final String DEFAULT_CITY_NAME = "default_city_name";
        public static final String PREF_IS_FIRSTIN_APP = "pref_is_firstin_app";
        public static final String IS_TURN_ON_PUSH = "is_turn_on_push";

    }

    public class ParentCode {
        /** 新闻来源 */
        public static final String NEWSINFO_WEBSITE = "NEWSINFO_WEBSITE";
        /** 黄页-交易性质 */
        public static final String Y_TRANSACTION_NATURE = "Y_TRANSACTION_NATURE";

        /** 房屋出租-房屋来源、房屋求租-交易性质、交易市场-身份、宠物交易-身份 */
        public static final String HOUSING_SOURCE = "HOUSING_SOURCE";

        /** 房屋出租-出租方式、房屋求租-求租方式 */
        public static final String RENTAL_METHOD = "RENTAL_METHOD";
        /** 房屋出租-房屋配置、房屋求租-期望设施 */
        public static final String HOUSE_CONFIGURATION = "HOUSE_CONFIGURATION";
        /** 房屋出租-房屋户型、房屋求租-期望户型 */
        public static final String HOUSE_HUXING = "HOUSE_HUXING";
        /** 房屋出租-距离 */
        public static final String DISTANCE = "DISTANCE";
        /** 房屋出租-租金筛选 */
        public static final String SCREEN_RENT = "SCREEN_RENT";
        /** 房屋出租-附近设施 */
        public static final String NEARBY_FACILITIES = "NEARBY_FACILITIES";
        /** 房屋出租-是否允许宠物 */
        public static final String PETS_ALLOWED = "PETS_ALLOWED";

        /** 招聘信息-性别要求 */
        public static final String SEX_REQUIREMENTS = "SEX_REQUIREMENTS";
        /** 招聘信息-工作性质 */
        public static final String JOB_FUNCTIONS = "JOB_FUNCTIONS";
        /** 招聘信息-行业要求 */
        public static final String INDUSTRY_REQUIREMENTS = "INDUSTRY_REQUIREMENTS";
        /** 招聘信息-经验要求 */
        public static final String EXPERIENCE_REQUIREMENTS = "EXPERIENCE_REQUIREMENTS";
        /** 招聘信息-学历要求 */
        public static final String EDUCATIONAL_REQUIREMENTS = "EDUCATIONAL_REQUIREMENTS";
        /** 招聘信息-签证要求 */
        public static final String VISA = "VISA";

        /** 宠物交易-宠物类型 */
        public static final String ALL_PET_TYPE = "ALL_PET_TYPE";
        /** 宠物交易-宠物品种-宠物猫 */
        public static final String PET_CAT_TYPE = "PET_CAT_TYPE";
        /** 宠物交易-宠物品种-宠物狗 */
        public static final String PET_DOG_TYPE = "PET_DOG_TYPE";
        /** 宠物交易-宠物品种-花鸟鱼虫 */
        public static final String PET_FLOWER_TYPE = "PET_FLOWER_TYPE";
        /** 宠物交易-宠物品种-宠物用品 */
        public static final String PET_CWYP_TYPE = "PET_CWYP_TYPE";
        /** 宠物交易-宠物性别 */
        public static final String PET_SEX = "PET_SEX";
        /** 宠物交易-血统证明 */
        public static final String LINEAGE = "LINEAGE";
        /** 宠物交易-疫苗情况 */
        public static final String VACCINUM = "VACCINUM";
        /** 宠物交易-是否驱虫 */
        public static final String ISEXPELLING_PARASITE = "ISEXPELLING_PARASITE";
        /** 宠物交易-是否绝育 */
        public static final String IS_STERILIZE = "IS_STERILIZE";
        /** 宠物交易-宠物猫、狗、鱼虫发布类型 */
        public static final String PUBLISH_TYPE = "PUBLISH_TYPE";
        /** 宠物交易-宠物用品发布类型 */
        public static final String PUBLISH_TYPE_CWYP = "PUBLISH_TYPE_CWYP";
        /** 宠物交易-提供服务 */
        public static final String PROVIDE_SERVICE = "PROVIDE_SERVICE";

        /** 汽车买卖-品牌 */
        public static final String CAR_BRAND = "CAR_BRAND";
        /** 汽车买卖-车辆交易方式 */
        public static final String CAR_JIAOYI_METHOD = "CAR_JIAOYI_METHOD";
        /** 汽车买卖-车辆来源 */
        public static final String VEHICLE_SOURCE = "HOUSING_SOURCE";
        /** 汽车买卖-变速箱 */
        public static final String CAR_TRANSMISSION = "CAR_TRANSMISSION";

        /** 交易市场-交易类型 */
        public static final String MEANS_TRANSACTION = "MEANS_TRANSACTION";
        /** 交易市场-产品分类 */
        public static final String PRODUCT_CATEGORIES = "PRODUCT_CATEGORIES";
        /** 交易市场-新旧程度 */
        public static final String NEWOID_STANDARD = "NEWOID_STANDARD";
        /** 交易市场-保修期内 */
        public static final String WARRANTY = "WARRANTY";
        /** 交易市场-交易方式 */
        public static final String TRANSACTION_NATURE = "TRANSACTION_NATURE";
        /** 交易市场-交易区域 */
        public static final String TRANSACTION_AREA = "TRANSACTION_AREA";
        /** 交易市场-标签 */
        public static final String TMARKETDESCRIPTION = "TMARKETDESCRIPTION";

        /** 生意转让-身份 */
        public static final String IDENTITY = "HOUSING_SOURCE";
        /** 生意转让-行业 */
        public static final String INDUSTRY = "INDUSTRY";
        /** 生意转让-类型 */
        public static final String SERVICE_TYPE = "SERVICE_TYPE";
        /** 生意转让-筛选时间 */
        public static final String FILTER_UPDATE_TIME = "FILTER_UPDATE_TIME";

        /** 教科书-学校名称 */
        public static final String BOOK_SCHOOL_NAME = "BOOK_SCHOOL_NAME";
        /** 教科书-书籍属于 */
        public static final String BOOK_SCHOOL = "BOOK_SCHOOL";
        /** 教科书-书本类型 */
        public static final String BOOK_TYPE = "BOOK_TYPE";
        /** 教科书-形式 */
        public static final String BOOK_SHAPE = "BOOK_SHAPE";
        /** 教科书-新旧程度 */
        public static final String BOOK_NEW_OLD = "BOOK_NEW_OLD";
        /** 教科书-有无笔记 */
        public static final String BOOK_IS_NOTE = "BOOK_IS_NOTE";
        /** 教科书-交易地点 */
        public static final String BOOK_TRAN_AREA = "BOOK_TRAN_AREA";

        /** 餐饮美食-菜系 */
        public static final String COOK_STYLE = "COOK_STYLE";
        /** 餐饮美食-营业周期 */
        public static final String WORK_PERIOD = "WORK_PERIOD";
        /** 餐饮美食-配送时间 */
        public static final String DELIVERY_TIME = "DELIVERY_TIME";

        /** 活动类型 */
        public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";

        /** 房市类型 */
        public static final String HOUSING_MARKET = "HOUSING_MARKET";

        /** 地产户型 */
        public static final String ESTATEINFO_HUXING = "ESTATEINFO_HUXING";

        /** 物业状态 */
        public static final String  PROPETY_STATUS = "PROPETY_STATUS";

    }

    public static final class IntentKey {
        public static final String CHANNEL_INFO = "channel_info";
        public static final String CLICK_POSITION = "click_position";
        public static final String WEB_VIEW_URL = "web_view_url";
        public static final String WEB_VIEW_TITLE = "web_view_title";
        public static final String CITY_NAME = "city_name";
        public static final String PHONE_AREA_INFO = "phone_area_info";
        public static final String SEARCH_TYPE = "search_type";
        public static final String INFORMATION_TYPE = "information_type";
        public static final String ICON_INFO = "icon_info";
        public static final String IS_REFRESH_VIEW_ENABLE = "is_refresh_view_enable";
        public static final String NEWS_INFO = "news_info";
        public static final String NEWS_INFO_OID = "news_info_oid";
        public static final String SHARE_INFO_JSON = "news_info_json";
        public static final String NEWS_IMAGE_COVER = "news_image_cover";
        public static final String NEWS_VIDEO_PATH = "news_video_path";
        public static final String TYPE = "type";
        public static final String LIFE_COMMON_OID = "life_common_oid";
        public static final String ADDRESS_INFO = "address_info";
        public static final String YELLOW_PAGE_TYPE_OID = "yellow_page_type_oid";
        public static final String PET_TYPE = "pet_type";
        public static final String MODE = "mode";
        public static final String SHOP_OID = "shop_oid";
        public static final String USER_OID = "user_oid";

    }


    public static final class EventKey {

        /** 清除图片缓存成功 */
        public static final String CLEAR_IMAGE_CACHE = "clear_image_cache";
        /** 切换城市成功 */
        public static final String SWITCH_CITY_SUCCESS = "cutover_city_success";
        /** 用户信息变化或登录/注销 */
        public static final String UPDATE_USER_INFO = "update_user_info";
        /** 刷新购物车列表 */
        public static final String UPDATE_SHOP_CART_LIST = "update_shop_cart_list";
        /** 刷新新闻列表 */
        public static final String UPDATE_NEWS_LIST = "update_news_list";
        /** 刷新新闻或生活搜索列表 */
        public static final String UPDATE_NEWS_LIFE_SEARCH_LIST = "update_news_life_search_list";
        /** 刷新未读消息数 */
        public static final String UPDATE_UNREAD_COUNT = "update_unread_count";
        /** 刷新频道列表 */
        public static final String UPDATE_CHANNEL_LIST = "update_channel_list";
        /** 刷新生活服务列表 */
        public static final String UPDATE_LIFE_RELEASE_LIST = "update_life_release_list";
        /** 刷新黄页列表 */
        public static final String UPDATE_YELLOW_PAGE_LIST = "update_yellow_page_list";
        public static final String UPDATE_SHOP_GOODS_PAGE = "update_shop_goods_page";
        public static final String SCROLL_TO_TOP = "scroll_to_top";
        public static final String ADD_COMMENT_SUCCESS = "add_comment_success";
    }

    public static final class IconType {

        public static final String 房屋出租 = "房屋出租";
        public static final String 招聘信息 = "招聘信息";
        public static final String 机票查询 = "机票查询";
        public static final String 汽车买卖 = "汽车买卖";
        public static final String 宠物交易 = "宠物交易";
        public static final String 交易市场 = "交易市场";
        public static final String 房屋求租 = "房屋求租";
        public static final String 生意转让 = "生意转让";
        public static final String 教科书 = "教科书";
        public static final String 餐饮美食 = "餐饮美食";

        public static final String 汇款中国 = "汇款中国";
        public static final String 房贷计算 = "房贷计算";
        public static final String 违规查询 = "违规查询";
        public static final String 实时交通 = "实时交通";
        public static final String 油价跟踪 = "油价跟踪";
        public static final String 实时汇率 = "实时汇率";
        public static final String 及时天气 = "及时天气";
        public static final String 犯罪调查 = "犯罪调查";
        public static final String 异域美食 = "异域美食";
        public static final String 个人算税 = "个人算税";
        public static final String 交通时表 = "交通时表";
        public static final String 公众假日 = "公众假日";

        public static final String 基金 = "基金";
        public static final String 股票 = "股票";
        public static final String 外汇 = "外汇";
        public static final String 民用地产 = "民用地产";
        public static final String 商业地产 = "商业地产";
        public static final String 估值 = "估值";
        public static final String 农场 = "农场";
    }

    public static final String M_NUMBER = "0010011107";
    public static final String SECRET_KEY = "3f29f2b7f9a84c3cba2d16491627238c";

    public static final String TANK_ELDER_SISTER = "TKJ0000000000001";
}
