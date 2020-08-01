package com.github.mimdal.tools.log.render;

import lombok.*;

/**
 * @author M.dehghan
 * @since 2020-07-29
 */
@Builder
@ToString
public class DynaTableRow {
    private static final String TABLE_DATA_TAG_CLOSE = "</td>";
    private int order;
    private String logLevel;
    private String lineNumber;
    private String time;
    private String elapsed;
    private String className;
    private String usefulData;
    private String tags;
    private String content;

    private String getOrder() {
        return "<td class='order-cell'>" + order + TABLE_DATA_TAG_CLOSE;
    }

    public String getLogLevel() {
        return "<td class='log-level-cell'>" + logLevel + TABLE_DATA_TAG_CLOSE;
    }

    private String getLineNumber() {
        return "<td class='lineNumber-cell'>" + lineNumber + TABLE_DATA_TAG_CLOSE;
    }

    private String getTime() {
        return "<td class='time-cell'>" + time + TABLE_DATA_TAG_CLOSE;
    }

    private String getElapsed() {
        return "<td class='elapsed-time-cell'>" + elapsed + TABLE_DATA_TAG_CLOSE;
    }

    private String getClassName() {
        return "<td class='className-cell'>" + className + TABLE_DATA_TAG_CLOSE;
    }

    private String getUsefulData() {
        return "<td class='useful-data-cell'>" + usefulData + TABLE_DATA_TAG_CLOSE;
    }

    private String getTags() {
        return "<td class='tags-cell'>" + tags + TABLE_DATA_TAG_CLOSE;
    }

    private String getContent() {
        return "<td class='content-cell'>" + content + TABLE_DATA_TAG_CLOSE;
    }

    public String getTableRow() {
        return "<tr class='row-" + order + "'>"
                + getOrder()
                + getLogLevel()
                + getLineNumber()
                + getTime()
                + getElapsed()
                + getClassName()
                + getUsefulData()
                + getTags()
                + getContent()
                + "</tr>";
    }
}
