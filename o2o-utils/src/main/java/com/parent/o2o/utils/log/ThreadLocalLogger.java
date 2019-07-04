package com.parent.o2o.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 日志包装。
 * <p>功能说明：日志包装。<br>
 * 修改备注：无
 * </p>
 *
 * @version 1.0.0
 */
public class ThreadLocalLogger implements Logger{
    private static Integer maxLine = 80;
    private static List<String> filterRules = new ArrayList<>();
    private static LoggerIndexOfThreadLocal loggerIndex = LoggerIndexOfThreadLocal.getInstance();

    private Logger logger;

    static {
        Properties properties = new Properties();
        try {
            properties.load(ThreadLocalLogger.class.getResourceAsStream("/threadLocalLogger.properties"));
            String filterRulesStr = properties.getProperty("filterRules");
            String maxLineStr = properties.getProperty("maxLine");
            if (filterRulesStr != null && (filterRulesStr = filterRulesStr.trim()).length() > 0) {
                String[] filterRulesNew = filterRulesStr.split(";");
                for (String rule : filterRulesNew) {
                    if ((rule = rule.trim()).length() > 0) {
                        filterRules.add(rule);
                    }
                }
            }
            if (maxLineStr != null && (maxLineStr=maxLineStr.trim()).length() > 0 && maxLineStr.matches("^\\d+$")) {
                Integer maxLineNew = Integer.parseInt(maxLineStr);
                if (maxLineNew > 10) {
                    maxLine = maxLineNew;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger(Class<?> clz){
        Logger slfLogger = LoggerFactory.getLogger(clz);
        ThreadLocalLogger tlLogger = new ThreadLocalLogger();
        tlLogger.logger = slfLogger;
        return tlLogger;
    }

    public static Logger getLogger(String name){
        Logger slfLogger = LoggerFactory.getLogger(name);
        ThreadLocalLogger tlLogger = new ThreadLocalLogger();
        tlLogger.logger = slfLogger;
        return tlLogger;
    }

    private ThreadLocalLogger(){}

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        if(logger.isTraceEnabled()) {
            logger.trace(formatMsg(msg));
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if(logger.isTraceEnabled()) {
            logger.trace(formatMsg(format), arg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if(logger.isTraceEnabled()) {
            logger.trace(formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if(logger.isTraceEnabled()) {
            logger.trace(formatMsg(format), arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if(logger.isTraceEnabled()) {
            logger.trace(formatMsg(msg));
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if (len == 0) {
                        logger.trace(formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    } else {
                        logger.trace(formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.trace(loggerIndex.toLog() + "  at " + element.toString());
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        if(logger.isTraceEnabled(marker)) {
            logger.trace(marker, formatMsg(msg));
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if(logger.isTraceEnabled(marker)) {
            logger.trace(marker, formatMsg(format), arg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if(logger.isTraceEnabled(marker)) {
            logger.trace(marker, formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if(logger.isTraceEnabled(marker)) {
            logger.trace(marker, formatMsg(format), argArray);
        }
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if(logger.isTraceEnabled(marker)) {
            logger.trace(marker, formatMsg(msg));
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if (len == 0) {
                        logger.trace(marker, formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    } else {
                        logger.trace(marker, formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.trace(marker, formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        if(logger.isDebugEnabled()) {
            logger.debug(formatMsg(msg));
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if(logger.isDebugEnabled()) {
            logger.debug(formatMsg(format), arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if(logger.isDebugEnabled()) {
            logger.debug(formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if(logger.isDebugEnabled()) {
            logger.debug(formatMsg(format), arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if(logger.isDebugEnabled()) {
            logger.debug(formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.debug(formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.debug(formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.debug(formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        if(logger.isDebugEnabled(marker)) {
            logger.debug(marker, formatMsg(msg));
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if(logger.isDebugEnabled(marker)) {
            logger.debug(marker, formatMsg(format), arg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if(logger.isDebugEnabled(marker)) {
            logger.debug(marker, formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        if(logger.isDebugEnabled(marker)) {
            logger.debug(marker, formatMsg(format), arguments);
        }
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        if(logger.isDebugEnabled(marker)) {
            logger.debug(marker, formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.debug(marker,formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.debug(marker,formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.debug(marker,formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        if(logger.isInfoEnabled()) {
            logger.info(formatMsg(msg));
        }
    }

    @Override
    public void info(String format, Object arg) {
        if(logger.isInfoEnabled()) {
            logger.info(formatMsg(format), arg);
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if(logger.isInfoEnabled()) {
            logger.info(formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if(logger.isInfoEnabled()) {
            logger.info(formatMsg(format), arguments);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if(logger.isInfoEnabled()) {
            logger.info(formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.info(formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.info(formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.info(formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        if(logger.isInfoEnabled(marker)) {
            logger.info(marker, formatMsg(msg));
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        if(logger.isInfoEnabled(marker)) {
            logger.info(marker, formatMsg(format), arg);
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if(logger.isInfoEnabled(marker)) {
            logger.info(marker, formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        if(logger.isInfoEnabled(marker)) {
            logger.info(marker, formatMsg(format), arguments);
        }
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        if(logger.isInfoEnabled(marker)) {
            logger.info(marker, formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.info(marker,formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.info(marker,formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.info(marker,formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        if(logger.isWarnEnabled()) {
            logger.warn(formatMsg(msg));
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if(logger.isWarnEnabled()) {
            logger.warn(formatMsg(format), arg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if(logger.isWarnEnabled()) {
            logger.warn(formatMsg(format), arguments);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if(logger.isWarnEnabled()) {
            logger.warn(formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if(logger.isWarnEnabled()) {
            logger.warn(formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.warn(formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.warn(formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.warn(formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        if(logger.isWarnEnabled(marker)) {
            logger.warn(marker, formatMsg(msg));
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        if(logger.isWarnEnabled(marker)) {
            logger.warn(marker, formatMsg(format), arg);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if(logger.isWarnEnabled(marker)) {
            logger.warn(marker, formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        if(logger.isWarnEnabled(marker)) {
            logger.warn(marker, formatMsg(format), arguments);
        }
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        if(logger.isWarnEnabled(marker)) {
            logger.warn(marker, formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.warn(marker,formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.warn(marker,formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.warn(marker,formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        if(logger.isErrorEnabled()) {
            logger.error(formatMsg(msg));
        }
    }

    @Override
    public void error(String format, Object arg) {
        if(logger.isErrorEnabled()) {
            logger.error(formatMsg(format), arg);
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if(logger.isErrorEnabled()) {
            logger.error(formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if(logger.isErrorEnabled()) {
            logger.error(formatMsg(format), arguments);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if(logger.isErrorEnabled()) {
            logger.error(formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.error(formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.error(formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.error(formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        if(logger.isErrorEnabled(marker)) {
            logger.error(marker, formatMsg(msg));
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        if(logger.isErrorEnabled(marker)) {
            logger.error(marker, formatMsg(format), arg);
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if(logger.isErrorEnabled(marker)) {
            logger.error(marker, formatMsg(format), arg1, arg2);
        }
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        if(logger.isErrorEnabled(marker)) {
            logger.error(marker, formatMsg(format), arguments);
        }
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if(logger.isErrorEnabled(marker)) {
            logger.error(marker, formatMsg(msg));
            // 输出错误堆栈
            if (t != null) {
                int len = 0;
                Throwable cause = t;
                // 输出错误其他的错误堆栈
                overLines:
                while (cause != null && len < maxLine) {
                    if(len == 0) {
                        logger.error(marker, formatMsg(t.getClass().getName() + ":" + t.getMessage()));
                    }else {
                        logger.error(marker,formatMsg("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage()));
                    }
                    if (++len >= maxLine) {
                        break;
                    }
                    StackTraceElement[] causeStackTrace = cause.getStackTrace();
                    if (causeStackTrace != null && causeStackTrace.length > 0) {
                        for (StackTraceElement element : causeStackTrace) {
                            if (checkRule(element.getClassName()) == null) {
                                logger.error(marker, formatMsg("  at " + element.toString()));
                                if (++len >= maxLine) {
                                    break overLines;
                                }
                            }
                        }
                    }
                    cause = cause.getCause();
                }
            }
        }
    }

    private static String checkRule(String className) {
        for (String rule : filterRules) {
            if (className.startsWith(rule)) {
                return rule;
            }
        }
        return null;
    }

    private String formatMsg(String msg) {
        if (msg == null) {
            return loggerIndex.toLog() + "null";
        } else if (msg.contains("\n")) {
            return loggerIndex.toLog() + msg.replace("\n"," ");
        } else {
            return loggerIndex.toLog() + msg;
        }
    }
}
