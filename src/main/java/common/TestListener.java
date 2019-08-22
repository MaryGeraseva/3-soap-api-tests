package common;

import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class TestListener implements TestWatcher{

    private Logger log;

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log = LogInstance.getLogger();
        log.info(String.format("%s disabled", context.getDisplayName()));
        logsAttachment(context);
        LogInstance.resetLog();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log = LogInstance.getLogger();
        log.info(String.format("%s finished successfully", context.getDisplayName()));
        logsAttachment(context);
        LogInstance.resetLog();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log = LogInstance.getLogger();
        getErrorLog(cause);
        log.info(String.format("%s aborted", context.getDisplayName()));
        logsAttachment(context);
        LogInstance.resetLog();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log = LogInstance.getLogger();
        getErrorLog(cause);
        log.info(String.format("%s failed", context.getDisplayName()));
        logsAttachment(context);
        LogInstance.resetLog();
    }

    private void getErrorLog(Throwable cause) {
        log.error(cause.getMessage());
        String stackTrace = "\n\n";
        for (StackTraceElement element : cause.getStackTrace()) {
            stackTrace += element.toString() + "\n";
        }
        log.error(stackTrace);
    }

    @Attachment
    private static byte[] attachment(ExtensionContext context) throws IOException {
        return Files.readAllBytes(getLogsFilePath(context));
    }

    private static Path getLogsFilePath(ExtensionContext context) {
        return Paths.get(String.format("%s\\build\\reports\\logsByTestMethod\\%s\\%s.log",
                System.getProperty("user.dir"), context.getTestMethod().get().getName(), context.getDisplayName()));
    }

    private void logsAttachment(ExtensionContext context) {
        try {
            attachment(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
