/**********************************************************      
 
 * 项目名称：o2o-utils   
 * 类名称：Constant   
 * 类描述：   
 * 创建人：王璐瑶 
 * 创建时间：2018年5月27日 下午3:16:22   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.utils.constant;

public class Constant {

	private Constant() {
	}

	public static final int TIME_OUT = 8100;
	// comm接口服务超时时间
	public static final int COMM_TIME_OUT = 8100;
	// pay接口服务超时时间
	public static final int PAY_TIME_OUT = 8100;

	// olinepay接口服务超时时间
	public static final int ONLINE_PAY_TIME_OUT = 20000;
	// 积分排名rpc接口服务超时时间
	public static final int SCORE_RANK_TIME_OUT = 15000;
	// 美食广场超时时间
	public static final int FOOD_COURT_TIME_OUT = 30000;

	/** ERROR_CODE入参 */
	public static final String ERROR_CODE = "errorCode";
	/** RETURN_CODE入参 */
	public static final String RETURN_CODE = "returnCode";
	/** ERROR_TEXT入参 */
	public static final String ERROR_TEXT = "errorText";
	/** ERROR_MSG入参 */
	public static final String ERROR_MSG = "errorMsg";
	/** 返回成功 */
	public static final int RETURN_CODE_SUCCESS = 1;
	/** 返回失败 */
	public static final int RETURN_CODE_FAIL = -1;
	/** ERROR 0 */
	public static final String ERROR_0 = "0";
	/** ERROR 200 */
	public static final String ERROR_200 = "200";
	/** ERROR 400 */
	public static final String ERROR_400 = "400";
	/** ERROR 401 */
	public static final String ERROR_401 = "401";
	/** ERROR 402(通用业务错误) */
	public static final String ERROR_402 = "402";
	/** ERROR 403(特殊业务错误,错误号一次追加) */
	public static final String ERROR_403 = "403";
	/** ERROR 405(特殊业务错误,错误号一次追加) */
	public static final String ERROR_405 = "405";
	/** ERROR 406(特殊业务错误,错误号一次追加) */
	public static final String ERROR_406 = "406";
	/** ERROR 407(特殊业务错误,错误号一次追加) */
	public static final String ERROR_407 = "407";
	/** ERROR 408(特殊业务错误,错误号一次追加) */
	public static final String ERROR_408 = "408";
	/** ERROR 409(特殊业务错误,错误号一次追加) */
	public static final String ERROR_409 = "409";
	/** ERROR 410(特殊业务错误,错误号一次追加) */
	public static final String ERROR_410 = "410";
	/** ERROR 500 */
	public static final String ERROR_500 = "500";
	/** ERROR 200返回消息 */
	public static final String ERROR_MSG_200 = "成功";
	/** ERROR 400返回消息 */
	public static final String ERROR_MSG_400 = "参数不全";
	/** ERROR 401返回消息 */
	public static final String ERROR_MSG_401 = "认证失败";
	/** ERROR 500返回消息 */
	public static final String ERROR_MSG_500 = "系统错误";
	/** CRM6标识 */
	public static final String CRM6 = "6";
	/** CRM7标识 */
	public static final String CRM7 = "7";
	/** CRM雪沥标识 */
	public static final String XUELI = "XUELI";
	
	/** CRM6 版本返回消息 */
	public static final String CRM6_VERSION = "[{\"code\":,\"0\",\"label\":\"O2O版\"},{\"code\":,\"1\",\"label\":\"CRM基础版\"},{\"code\":,\"2\",\"label\":\"CRM增强版\"},{\"code\":,\"3\",\"label\":\"食堂版\"}]";

	/** 定义24个时段代表一天的24个小时 */
	public static final int[] TIME_INTERVAL = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
			20, 21, 22, 23 };
	
	/** 定义12个月 */
	public static final int[] MONTH_INTERVAL = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	
	/** 定义12个月名称 */
	public static final String[] MONTH_NAME = { "1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};

	/** 星期 */
	public static final int[] WEEK_DAYS = { 1, 2, 3, 4, 5, 6, 7 };
	/** 时间格式化字符串 yyyyMMdd*/
	public static final String DATEFORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DATEFORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	/** 时间格式化字符串 yyyyMMddHHmmss*/
	public static final String DATEFORMAT_JOIN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	/** 时间格式化字符串 yyyyMMddHHmmssSSS*/
	public static final String DATEFORMAT_JOIN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	/** 时间格式化字符串 yyyy-MM-dd HH:mm:ss*/
	public static final String DATEFORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	/** 时间格式化字符串 HH:mm:ss*/
	public static final String DATEFORMAT_HHMMSS = "HH:mm:ss";
}
