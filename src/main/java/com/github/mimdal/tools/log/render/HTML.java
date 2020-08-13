package com.github.mimdal.tools.log.render;

import com.github.mimdal.tools.log.chain.*;
import com.github.mimdal.tools.log.dto.CurrentLogParams;
import com.github.mimdal.tools.log.entity.LogEntity;
import com.github.mimdal.tools.log.thirdparty.ThirdPartyUtils;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.github.mimdal.tools.log.constants.Constants.*;

/**
 * @author M.dehghan
 * @since 2020-07-29
 */
public class HTML implements LogProcess {
    private String fileName;
    private List<LogEntity> logList;
    private CurrentLogParams currentLogParams;
    private Map<String, String> sessionThreads;

    {
        fileName = OUT_PUT_FILE_NAME + ".html";
    }

    @Override
    public void setNext(LogProcess next) {
        //there is no next chain
    }

    @Override
    public void process(WrapperObject wrapperObject) {
        logList = wrapperObject.getSessionLogEntities();
        sessionThreads = wrapperObject.getSessionThreads();
        currentLogParams = wrapperObject.getCurrentLogParams();
        generate();
    }

    public void generate() {
        InputStream inputStream = readFileFromResourceDir("dynamic-table.html");
        String session = currentLogParams.getSession();
        String filters = getFilterTags();
        String table = generateTableRows();
        String statistics = "";

        String htmlString = ThirdPartyUtils.inputStreamToString(inputStream)
                .replace("$sessionThreads", getThreadsFormattedName())
                .replace("$session", session)
                .replace("$filters", filters)
                .replace("$statistics", statistics)
                .replace("$table", table);
        File newHtmlFile = new File(String.format("%s/output/%s/%s", APP_DIRECTORY,
                currentLogParams.getSession() != null ? currentLogParams.getSession() : "without-session",
                fileName));
        ThirdPartyUtils.writeStringToFile(newHtmlFile, htmlString);
    }

    private String getThreadsFormattedName() {
        StringJoiner returnValue = new StringJoiner(COMMA_PLUS_SPACE_DELIMITER);
        sessionThreads.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> returnValue.add(entry.getKey() + OPEN_PARENTHESIS + entry.getValue() + CLOSE_PARENTHESIS));
        return returnValue.toString();
    }

    private InputStream readFileFromResourceDir(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(fileName);
    }

    private String generateTableRows() {
        StringBuilder outPut = new StringBuilder();
        int incrementItemNumber = 1;
        LocalTime firstLogTime = logList.get(0).getTime().toLocalTime();
        for (LogEntity log : logList) {
            long elapsedTimeInMilliseconds = Duration.between(firstLogTime, log.getTime().toLocalTime()).toMillis();
            String elapsed = String.format("%dmin, %dsec, %dms",
                    TimeUnit.MILLISECONDS.toMinutes(elapsedTimeInMilliseconds),
                    TimeUnit.MILLISECONDS.toSeconds(elapsedTimeInMilliseconds),
                    TimeUnit.MILLISECONDS.toMillis(elapsedTimeInMilliseconds % 1000)
            );
            elapsed = elapsed.replace("0min,", "")
                    .replace("0sec,", "");

            DynaTableRow tableRow = DynaTableRow.builder()
                    .order(incrementItemNumber)
                    .logLevel(log.getLevel())
                    .lineNumber(log.getLineNumber())
                    .time(log.getTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss SSS")))
                    .elapsed(elapsed)
                    .className(log.getClassName())
                    .usefulData(log.getUsefulData() != null ? log.getUsefulData() : "")
                    .tags(log.convertTagsToString())
                    .content("<a href='./files/" + incrementItemNumber + ".html'><code>link</code></a>")
                    .build();

            String individualFileName = incrementItemNumber + ".html";
            File newHtmlFile = new File(String.format("%s/output/%s/files/%s", APP_DIRECTORY, currentLogParams.getSession(),
                    individualFileName));
            ThirdPartyUtils.writeStringToFile(newHtmlFile, log.getContent().toString());

            InputStream inputStream = readFileFromResourceDir("log-console.html");
            String backToHome = BACK_TO_HOME_START_TAG + fileName + BACK_TO_HOME_END_TAG;
            StringJoiner content = new StringJoiner("\n", "", "")
                    .add("Line Number: " + log.getLineNumber())
                    .merge(log.getContent());
            if (log.getRelatedLogs() != null && !log.getRelatedLogs().isEmpty()) {
                log.getRelatedLogs().forEach(logEntity ->
                        content.add(HTML_HORIZONTAL_LINE)
                                .add("Line Number: " + logEntity.getLineNumber())
                                .merge(logEntity.getContent()));
            }
            String htmlString = ThirdPartyUtils.inputStreamToString(inputStream)
                    .replace("$backToHome", backToHome)
                    .replace("$content", ThirdPartyUtils.escapeHtml(content.toString()));

            ThirdPartyUtils.writeStringToFile(newHtmlFile, htmlString);
            incrementItemNumber++;
            outPut.append(tableRow.getTableRow());
        }
        return outPut.toString();
    }

    private String getFilterTags() {
        return FILTER_DESCRIPTION +
                INVOKING_TAG + COMMA_PLUS_SPACE_DELIMITER +
                REQUEST_TAG + COMMA_PLUS_SPACE_DELIMITER +
                RESPONSE_TAG + COMMA_PLUS_SPACE_DELIMITER +
                Exception_TAG + COMMA_PLUS_SPACE_DELIMITER +
                INCOMING_ISO_MSG_TAG + COMMA_PLUS_SPACE_DELIMITER +
                OUTGOING_ISO_MSG_TAG;
    }
}
