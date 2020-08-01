package com.github.mimdal.tools.log.entity;

import com.github.mimdal.tools.log.constants.Constants;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author M.dehghan
 * @since 2020-07-27
 */
@Setter
@Getter
@Builder
@ToString
public class LogEntity {
    private String lineNumber;
    private LocalDateTime time;
    private String firstLine;
    private String level;
    private String channel;
    private String thread;
    private String client;
    private String session;
    private String className;
    private StringJoiner content;
    private String usefulData;
    private Set<String> tags;
    private Set<LogEntity> relatedLogs;

    public void setRelatedLogs(LogEntity relatedLog) {
        if (relatedLogs == null) {
            relatedLogs = new HashSet<>();
        }
        this.relatedLogs.add(relatedLog);
    }

    public String convertTagsToString() {
        StringJoiner stringJoiner = new StringJoiner(Constants.HTML_BREAK, "", "");
        for (String tag : tags) {
            stringJoiner.add(tag);
        }
        return stringJoiner.toString();
    }
}
