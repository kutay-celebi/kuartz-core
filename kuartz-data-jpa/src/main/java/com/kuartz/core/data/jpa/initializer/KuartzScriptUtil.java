package com.kuartz.core.data.jpa.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.*;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KuartzScriptUtil extends ScriptUtils {

    public static void executeSqlScript(Connection connection, EncodedResource resource, boolean continueOnError,
                                        boolean ignoreFailedDrops, String commentPrefix, @Nullable String separator,
                                        String blockCommentStartDelimiter, String blockCommentEndDelimiter) throws ScriptException {

        try {
            log.info(resource + " SQL dosyasi calistiriliyor.");
            long startTime = System.currentTimeMillis();

            String script;
            try {
                script = readScript(resource, commentPrefix, separator, blockCommentEndDelimiter);
            } catch (IOException ex) {
                throw new CannotReadScriptException(resource, ex);
            }

            if (separator == null) {
                separator = DEFAULT_STATEMENT_SEPARATOR;
            }
            if (!EOF_STATEMENT_SEPARATOR.equals(separator) && !containsSqlScriptDelimiters(script, separator)) {
                separator = FALLBACK_STATEMENT_SEPARATOR;
            }

            List<String> statements = new ArrayList<>();
            splitSqlScript(resource, script, separator, commentPrefix, blockCommentStartDelimiter,
                           blockCommentEndDelimiter, statements);

            int stmtNumber = 0;
            long rowsAffected = 0;
            Statement stmt = connection.createStatement();
            try {
                for (String statement : statements) {
                    stmtNumber++;
                    try {
                        stmt.execute(statement);
                        log.info(statement + " calistirildi.");

                        // todo bunu inceleyelim.
                        rowsAffected += stmt.getUpdateCount();

                        SQLWarning warningToLog = stmt.getWarnings();
                        while (warningToLog != null) {
                            log.warn(warningToLog.getSQLState() + " " + warningToLog.getErrorCode() + " " + warningToLog.getMessage() +
                                     " hatasi esgecildi");
                            warningToLog = warningToLog.getNextWarning();
                        }
                    } catch (SQLException ex) {
                        boolean dropStatement = StringUtils.startsWithIgnoreCase(statement.trim(), "drop");
                        if (continueOnError || (dropStatement && ignoreFailedDrops)) {
                            log.error("Satir calistirilamadi : " + statement);
                        } else {
                            throw new ScriptStatementFailedException(statement, stmtNumber, resource, ex);
                        }
                    }
                }
            } finally {
                try {
                    stmt.close();
                } catch (Throwable ex) {
                    log.trace("Could not close JDBC Statement", ex);
                }
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info(resource.getResource().getFilename() + " --> " + elapsedTime + " ms." + " 'de calistiridi. " + rowsAffected +
                     " satir guncellendi.");
        } catch (Exception ex) {
            if (ex instanceof ScriptException) {
                throw (ScriptException) ex;
            }
            throw new UncategorizedScriptException(
                    "Failed to execute database script from resource [" + resource.getResource().getFilename() + "]", ex);
        }
    }


    private static String readScript(EncodedResource resource, @Nullable String commentPrefix,
                                     @Nullable String separator, @Nullable String blockCommentEndDelimiter) throws IOException {

        LineNumberReader lnr = new LineNumberReader(resource.getReader());
        try {
            return readScript(lnr, commentPrefix, separator, blockCommentEndDelimiter);
        } finally {
            lnr.close();
        }
    }

}