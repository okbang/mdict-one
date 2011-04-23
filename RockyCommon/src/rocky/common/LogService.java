/**
 * LogService.java
 * Copyright Rocky.
 */
package rocky.common;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * LogService cung cấp các dịch vụ logging.
 * @author Thach
 * @since 1.4
 */
public class LogService {
    // Singleton instance of Logger.
    private static Logger logger = Logger.getLogger(LogService.class);
    
    /**
     * Logging.
     * @param priority log level.
     * @param obj đối tượng cần logging.
     */
    public static void log(Priority priority, Object obj) {
        logger.log(priority, obj); 
    }
    
    /**
     * Logging obj voi priority la Priority.DEBUG
     * @param obj đối tượng cần log.
     */
    public static void logDebug(Object obj) {
        logger.debug(obj);
    }
    
    /**
     * Logging đối tượng obj và Throwable th với Priority.ERROR
     * @param obj đối tượng sẽ được gọi toString để xuất log.
     * @param th đối tượng mô tả lỗi.
     */
    public static void logError(Object obj, Throwable th) {
        logger.error(obj, th);
    }
    
    public static void logError(Throwable th) {
        logger.error(th);
    }

    /**
     * Logging đối tượng obj và Throwable th với Priority.WARN
     * @param obj đối tượng sẽ được gọi toString để xuất log.
     * @param th đối tượng mô tả lỗi.
     */
    public static void logWarn(Object obj, Throwable th) {
        logger.warn(obj, th);
    }
    
    /**
     * Logging đối tượng obj với Priority.WARN.
     * @param obj đối tượng sẽ được gọi toString để xuất log
     */
    public static void logWarn(Object obj) {
        logger.warn(obj);
    }

    /**
     * Logging obj voi priority la Priority.INFO
     * @param obj đối tượng cần log.
     */
    public static void logInfo(Object obj) {
        logger.info(obj);
    }
}
