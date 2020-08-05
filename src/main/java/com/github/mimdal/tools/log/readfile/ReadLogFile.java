package com.github.mimdal.tools.log.readfile;

import com.github.mimdal.tools.log.analysis.Analysis;
import com.github.mimdal.tools.log.chain.*;
import com.github.mimdal.tools.log.constants.Constants;
import com.github.mimdal.tools.log.dto.CurrentLogParams;
import com.github.mimdal.tools.log.entity.LogEntity;
import com.github.mimdal.tools.log.util.Utils;
import org.apache.commons.io.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.*;
import java.util.*;
import java.util.regex.*;

/**
 * @author M.dehghan
 * @since 2020-07-27
 */
public class ReadLogFile implements LogProcess {
    private LogProcess next;
    private String filePath;
    private CurrentLogParams sampleSessionLog;
    private Pattern pattern;
    private List<LogEntity> sessionLogEntities;
    private boolean addLineToContent;
    private boolean maybeHasMoreLog;
    private LocalDateTime firstTimeSessionHappen;
    private LogEntity logEntity;
    private int sessionMaxLiveInMinutes;

    public ReadLogFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void setNext(LogProcess next) {
        this.next = next;
    }

    @Override
    public void process(WrapperObject wrapperObject) {
        sampleSessionLog = wrapperObject.getCurrentLogParams();
        fieldInitialize();
        int lineNumber = 1;
        try (LineIterator it = FileUtils.lineIterator(new File(filePath), "UTF-8")) {
            while (maybeHasMoreLog && it.hasNext()) {
                String line = it.nextLine();
                aLineProcess(line, lineNumber);
                lineNumber++;
            }
            System.out.println("max lineNumber read= " + lineNumber);
        } catch (IOException e) {
            Utils.systemExit("file not exist, filePath = " + filePath);
        }
        System.out.println("sessionLogEntities = " + sessionLogEntities.size());
        if (sessionLogEntities.isEmpty()) {
            Utils.systemExit("zero result FOUND! try again by new arguments.");
        }
        wrapperObject.setSessionLogEntities(sessionLogEntities);
        next.process(wrapperObject);
    }

    private void fieldInitialize() {
        pattern = Pattern.compile(Constants.APPLICATION_LOG_REGEX);
        sessionLogEntities = new ArrayList<>();
        sessionMaxLiveInMinutes = 30; //todo hardcode section
        maybeHasMoreLog = true;
        addLineToContent = false;
    }



    public void aLineProcess(String aLogLine, int lineNumber) {
        Matcher matcher = pattern.matcher(aLogLine);
        if (matcher.matches()) {
            if (isRelatedToCurrentLogParams(matcher)) {
                performancePassion(matcher);
                addLineToContent = true;
                logEntity = logEntityCreation(aLogLine, lineNumber, matcher);
                sessionLogEntities.add(logEntity);
            } else {
                addLineToContent = false;
            }
        } else if (addLineToContent) {
            logEntity.getContent().add(aLogLine);
            logEntity.setTags(tagGenerate(aLogLine, logEntity.getTags()));
            loopBreakIfLogout(aLogLine);
        }
    }

    private LogEntity logEntityCreation(String aLogLine, int lineNumber, Matcher matcher) {
        return LogEntity.builder()
                .lineNumber(Utils.formatNumber(lineNumber))
                .time(Utils.stringToDate(matcher.group(Constants.TIME_MATCHER).trim()))
                .level(highlightLogLevel(matcher.group(Constants.LOG_LEVEL_MATCHER).trim()))
                .channel(matcher.group(Constants.CHANNEL_MATCHER).trim())
                .thread(matcher.group(Constants.THREAD_MATCHER).trim())
                .client(matcher.group(Constants.CLIENT_MATCHER).trim())
                .session(matcher.group(Constants.SESSION_MATCHER).trim())
                .className(extractCLassName(matcher.group(Constants.CLASS_MATCHER).trim()))
                .content(new StringJoiner(Constants.NEW_LINE_CHAR, "", "").add(aLogLine))
                .build();
    }

    private String extractCLassName(String packageName) {
        String className;
        String[] names = packageName.split(Constants.DOT_REGEX_DELIMITER);
        int arrayLength = names.length;
        if (arrayLength > 1) {
            className = names[arrayLength-2].split("\\$")[0] + Constants.DOT_DELIMITER + names[arrayLength-1];
        } else {
            className = packageName;
        }
        return className;
    }

    private String highlightLogLevel(String logLevel) {
        String overrideLogLevel;
        switch (logLevel) {
            case "FATAL":
            case "ERROR":
            case "WARN":
                overrideLogLevel = "<span style='color:red'>" + logLevel + "</span>";
                break;
            default:
                overrideLogLevel = logLevel;
                break;
        }
        return overrideLogLevel;
    }

    private void performancePassion(Matcher matcher) {
        LocalDateTime logTime = Utils.stringToDate(matcher.group(Constants.TIME_MATCHER).trim());
        if (firstTimeSessionHappen == null) {
            firstTimeSessionHappen = logTime;
        } else {
            long minutes = ChronoUnit.MINUTES.between(firstTimeSessionHappen, logTime);
            if (minutes > sessionMaxLiveInMinutes) {
                maybeHasMoreLog = false;
            }
        }
    }

    private boolean isRelatedToCurrentLogParams(Matcher matcher) {
        //todo hardcode section: dependent to specific logs
        return ((sampleSessionLog.getSession().equals(matcher.group(Constants.SESSION_MATCHER).trim())) ||
                (matcher.group(Constants.SESSION_MATCHER).isEmpty() &&
                        sampleSessionLog.getChannel().equals(matcher.group(Constants.CHANNEL_MATCHER).trim()) &&
                        sampleSessionLog.getClient().equals(matcher.group(Constants.CLIENT_MATCHER).trim())));
    }

    private void loopBreakIfLogout(String aLogLine) {
        //todo hardcode section: dependent to specific logs
        if (aLogLine.contains("logout Response: () ")) {
            maybeHasMoreLog = false;
        }
    }

    private Set<String> tagGenerate(String aLogLine, Set<String> tags) {
        //todo hardcode section: dependent to specific logs
        if (tags == null) {
            tags = new HashSet<>();
        }
        if (aLogLine.contains("Invoking ")) {
            tags.add(Constants.INVOKING_TAG);
        }
        if (aLogLine.contains("Request")) {
            tags.add(Constants.REQUEST_TAG);
        }
        if (aLogLine.contains("Response")) {
            tags.add(Constants.RESPONSE_TAG);
        }
        if (aLogLine.contains("Exception")) {
            tags.add(Constants.Exception_TAG_RED_COLOR);
        }
        if (aLogLine.contains("isomsg direction=\"incoming")) {
            tags.add(Constants.INCOMING_ISO_MSG_TAG);
        }
        if (aLogLine.contains("isomsg direction=\"outgoing")) {
            tags.add(Constants.OUTGOING_ISO_MSG_TAG);
        }
        if (aLogLine.contains("Statistics")) {
            tags.add(Constants.STATISTICS_TAG);
        }
        return tags;
    }
}
