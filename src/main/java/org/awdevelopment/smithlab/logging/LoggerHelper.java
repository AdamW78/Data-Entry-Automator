package org.awdevelopment.smithlab.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;


public class LoggerHelper {

    private final Logger LOGGER;

    public LoggerHelper(Logger logger) { LOGGER = logger; }

    public void atFatal(String[] messages) { for (String message : messages) atFatal(message); }
    public void atFatal(String message, Exception e) { LOGGER.atFatal().log(message, e); }
    public void atFatal(String message) { LOGGER.atFatal().log(message); }
    public void atInfo(String[] messages) { for (String message : messages) atInfo(message); }
    public void atInfo(String message, Exception e) { LOGGER.atInfo().log(message, e); }
    public void atInfo(String message) { LOGGER.atInfo().log(message); }
    public void atDebug(String[] messages) { for (String message : messages) atDebug(message); }
    public void atDebug(String message, Exception e) { LOGGER.atDebug().log(message, e); }
    public void atDebug(String message) { LOGGER.atDebug().log(message); }
    public void atError(String[] messages) { for (String message : messages) atError(message); }
    public void atError(String message, Exception e) { LOGGER.atError().log(message, e); }
    public void atError(String message) { LOGGER.atError().log(message); }
    public void atWarn(String[] messages) { for (String message : messages) atWarn(message); }
    public void atWarn(String message, Exception e) { LOGGER.atWarn().log(message, e); }
    public void atWarn(String message) { LOGGER.atWarn().log(message); }
    public void atTrace(String[] messages) { for (String message : messages) atTrace(message); }
    public void atTrace(String message, Exception e) { LOGGER.atTrace().log(message, e); }
    public void atTrace(String message) { LOGGER.atTrace().log(message); }
    public void atLevel(String message, Level level) { LOGGER.atLevel(level).log(message); }
    public void atLevel(String message, Level level, Exception e) { LOGGER.atLevel(level).log(message, e); }
    public void atLevel(String[] messages, Level level) { for (String message : messages) atLevel(message, level); }
}
