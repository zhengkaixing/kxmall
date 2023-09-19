package com.kxmall.common.constant;

/**
 * 商城统一常量
 */
public interface ShopConstants {

	/**
	 * 订单自动取消时间（分钟）
	 */
	long ORDER_OUTTIME_UNPAY = 30;
	/**
	 * 订单自动收货时间（天）
	 */
	long ORDER_OUTTIME_UNCONFIRM = 7;
	/**
	 * redis订单未付款key
	 */
	String REDIS_ORDER_OUTTIME_UNPAY = "order:unpay:";
	/**
	 * redis订单收货key
	 */
	String REDIS_ORDER_OUTTIME_UNCONFIRM = "order:unconfirm:";

	/**
	 * redis拼团key
	 */
	String REDIS_PINK_CANCEL_KEY = "pink:cancel:";

	/**
	 * 微信支付service
	 */
	String KXMALLS_WEIXIN_PAY_SERVICE = "kxmall_weixin_pay_service";

	/**
	 * 微信支付小程序service
	 */
	String KXMALLS_WEIXIN_MINI_PAY_SERVICE = "kxmall_weixin_mini_pay_service";

	/**
	 * 微信支付app service
	 */
	String KXMALLS_WEIXIN_APP_PAY_SERVICE = "kxmall_weixin_app_pay_service";

	/**
	 * 微信公众号service
	 */
	String KXMALLS_WEIXIN_MP_SERVICE = "kxmall_weixin_mp_service";
	/**
	 * 微信小程序service
	 */
	String KXMALLS_WEIXIN_MA_SERVICE = "kxmall_weixin_ma_service";

	/**
	 * 商城默认密码
	 */
	String KXMALLS_DEFAULT_PWD = "123456";

	/**
	 * 商城默认注册图片
	 */
	String KXMALLS_DEFAULT_AVATAR = "https://image.dayouqiantu.cn/5e79f6cfd33b6.png";

	/**
	 * 腾讯地图地址解析
	 */
	String QQ_MAP_URL = "https://apis.map.qq.com/ws/geocoder/v1/";

	/**
	 * redis首页键
	 */
	String WMHOP_REDIS_INDEX_KEY = "kxmall:index_data";

	/**
	 * 配置列表缓存
	 */
	String KXMALLS_REDIS_CONFIG_DATAS = "kxmall:config_datas";

	/**
	 * 充值方案
	 */
	String KXMALLS_RECHARGE_PRICE_WAYS = "kxmall_recharge_price_ways";
	/**
	 * 首页banner
	 */
	String KXMALLS_HOME_BANNER = "kxmall_home_banner";
	/**
	 * 首页菜单
	 */
	String KXMALLS_HOME_MENUS = "kxmall_home_menus";
	/**
	 * 首页滚动新闻
	 */
	String KXMALLS_HOME_ROLL_NEWS = "kxmall_home_roll_news";
	/**
	 * 热门搜索
	 */
	String KXMALLS_HOT_SEARCH = "kxmall_hot_search";
	/**
	 * 个人中心菜单
	 */
	String KXMALLS_MY_MENUES = "kxmall_my_menus";
	/**
	 * 秒杀时间段
	 */
	String KXMALLS_SECKILL_TIME = "kxmall_seckill_time";
	/**
	 * 签到天数
	 */
	String KXMALLS_SIGN_DAY_NUM = "kxmall_sign_day_num";

	/**
	 * 打印机配置
	 */
	String KXMALLS_ORDER_PRINT_COUNT = "order_print_count";
	/**
	 * 飞蛾用户信息
	 */
	String KXMALLS_FEI_E_USER = "fei_e_user";
	/**
	 * 飞蛾用户密钥
	 */
	String KXMALLS_FEI_E_UKEY= "fei_e_ukey";

	/**
	 * 打印机配置
	 */
	String KXMALLS_ORDER_PRINT_COUNT_DETAIL = "order_print_count_detail";

	/**
	 * 短信验证码长度
	 */
	int KXMALLS_SMS_SIZE = 6;

	/**
	 * 短信缓存时间
	 */
	long KXMALLS_SMS_REDIS_TIME = 600L;

	//零标识
	String KXMALLS_ZERO =  "0";

	//业务标识标识
	String KXMALLS_ONE =  "1";

	//目前完成任务数量是3
	int TASK_FINISH_COUNT = 3;

	int KXMALLS_ONE_NUM = 1;

	String KXMALLS_ORDER_CACHE_KEY = "kxmall:order";

	long KXMALLS_ORDER_CACHE_TIME = 600L;

	String WECHAT_MENUS =  "wechat_menus";

	String KXMALLS_EXPRESS_SERVICE = "kxmall_express_service";

	String WMSHOP_REDIS_SYS_CITY_KEY = "kxmall:city_list";

	String KXMALLS_REDIS_CITY_KEY = "kxmall:city";

	String KXMALLS_APP_LOGIN_USER = "app-online-token:";

	String KXMALLS_WECHAT_PUSH_REMARK = "kxmall为您服务！";

	String DEFAULT_UNI_H5_URL = "https://h5.kxmall.vip";

	String KXMALLS_MINI_SESSION_KET = "kxmall:session_key:";

	/**公众号二维码*/
	String WECHAT_FOLLOW_IMG="wechat_follow_img";
	/**后台api地址*/
	String ADMIN_API_URL="admin_api_url";
}
