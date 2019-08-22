package common;


import org.apache.log4j.*;
import org.junit.jupiter.api.TestInfo;

import java.io.File;


public class LogInstance {

    private static ThreadLocal<Logger> log = new ThreadLocal<>();

    private LogInstance() {}

    public static synchronized Logger getLogger() {
        if (log.get() == null) {
            log.set(LogManager.getLogger("Logger"));
        }
        return log.get();
    }

    public static synchronized Logger setContext(TestInfo testInfo) {
        Logger logger = LogManager.getLogger(testInfo.getDisplayName());
        logger.addAppender(fileAppenderConfig(testInfo));
        logger.addAppender(consoleAppenderConfig());
        log.set(logger);
        return log.get();
    }

    private static FileAppender fileAppenderConfig(TestInfo testInfo) {

        String path = getLogsFilePath(testInfo);

        new File(path);

        FileAppender appender = new FileAppender();
        appender.setFile(path);
        appender.setLayout(new EnhancedPatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n"));
        appender.setThreshold(Level.TRACE);
        appender.setAppend(false);
        appender.activateOptions();
        return appender;
    }

    private static ConsoleAppender consoleAppenderConfig(){
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(new EnhancedPatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n"));
        consoleAppender.setThreshold(Level.TRACE);
        consoleAppender.activateOptions();

        return consoleAppender;
    }

    public static String getLogsFilePath(TestInfo testInfo) {
       return String.format("%s\\build\\reports\\logsByTestMethod\\%s\\%s.log",
                System.getProperty("user.dir"), testInfo.getTestMethod().get().getName(), testInfo.getDisplayName());
    }

    public static void resetLog() {
        log.set(null);
    }
}
