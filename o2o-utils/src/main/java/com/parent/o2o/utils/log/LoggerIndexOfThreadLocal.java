package com.parent.o2o.utils.log;

import java.util.Random;

/**
 * 标识索引，用于标识同一个请求。
 * <p>
 * 修改备注：无
 * </p>
 *
 * @version 1.0.0
 */
public class LoggerIndexOfThreadLocal{
    private static ThreadLocal<String> reqKeyThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Integer> reqNoThreadLocal = new ThreadLocal<>();
    private static LoggerIndexOfThreadLocal loggerIndex = new LoggerIndexOfThreadLocal();
    
    public static LoggerIndexOfThreadLocal getInstance() {
        return loggerIndex;
    }

    private LoggerIndexOfThreadLocal() {}

    // 设置默认值
    private String set() {
        return set("LG" + String.format("%010d", new Random().nextInt(Integer.MAX_VALUE)));
    }

    // 开始
    public String set(String index){
        if (index == null || index.trim().length() == 0) {
            return set();
        }
        reqKeyThreadLocal.set(index);
        return index;
    }
    
    public String get() {
        return getReqKey() + "_" + getReqNo();
    }
    
    // 转化为日志格式
    public String toLog(){
        return "[" + getReqKey() + "][" + getReqNo() + "]";
    }

    // 获取请求key
    private String getReqKey(){
        String reqInxRef = reqKeyThreadLocal.get();
        if (reqInxRef == null) {
            reqInxRef = set();
        }
        return reqInxRef;
    }

    // 获取请求号
    private Integer getReqNo(){
        Integer reqIndexNoRef = reqNoThreadLocal.get();
        if (reqIndexNoRef == null) {
            reqIndexNoRef = 0;
        }else {
            if(reqIndexNoRef > 10000) {
                reqIndexNoRef = 0;
            } else {
                reqIndexNoRef++;
            }
        }
        reqNoThreadLocal.set(reqIndexNoRef);
        return reqIndexNoRef;
    }

    /**
     * 清除记录。
     * <p>功能说明：一般不需要调用。在线程结束时，会自动清除引用。<br>
     * 修改备注：无
     * </p>
     */
    public void remove(){
        reqKeyThreadLocal.remove();
        reqNoThreadLocal.remove();
    }
}
