package com.github.mimdal.tools.log.constants;

/**
 * @author M.dehghan
 * @since 2020-07-27
 */
public class Constants {
    private Constants() {
    }

    public static final String APP_DIRECTORY = System.getProperty("user.dir");
    public static final String OUT_PUT_FILE_NAME = "SessionExtracted";

    public static final String LOG_LEVEL_MATCHER = "level";
    public static final String TIME_MATCHER = "time";
    public static final String CHANNEL_MATCHER = "channel";
    public static final String THREAD_MATCHER = "thread";
    public static final String CLIENT_MATCHER = "client";
    public static final String SESSION_MATCHER = "session";
    public static final String CLASS_MATCHER = "class";

    private static final String LINE_START = "^";
    private static final String LINE_END = "$";
    private static final String LOG_LEVEL = "\\[(?<" + LOG_LEVEL_MATCHER + ">\\w+?)\\]";
    private static final String MULTI_SPACE = "\\s+";
    private static final String TIME = "(?<" + TIME_MATCHER + ">\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3})";
    private static final String CHANNEL = "channel:(?<" + CHANNEL_MATCHER + ">\\S+?)";
    private static final String THREAD = "thread:\\[(?<" + THREAD_MATCHER + ">.+?)\\]";
    private static final String CLIENT = "client:\\[(?<" + CLIENT_MATCHER + ">[\\d.]+)]";
    private static final String SESSION = "session_id:\\[(?<" + SESSION_MATCHER + ">.*?)\\]";
    private static final String CLASS_NAME = "(?<" + CLASS_MATCHER + ">.+?)";
    private static final String CLASS_LINE_NUMBER = "@\\d+:";
    public static final String APPLICATION_LOG_REGEX = LINE_START + LOG_LEVEL + MULTI_SPACE + TIME + MULTI_SPACE + CHANNEL +
            MULTI_SPACE + THREAD + MULTI_SPACE + CLIENT + MULTI_SPACE + SESSION + MULTI_SPACE + CLASS_NAME +
            CLASS_LINE_NUMBER + LINE_END;

    public static final String TIME_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'.'SSS";
    public static final String SERVICE_REQUEST_LOG_INDICATOR = "Request: (  Context{";
    public static final String REQUEST = "Request";
    public static final String RESPONSE = "Response";
    public static final String NEW_LINE_CHAR = "\n";
    public static final String HTML_BREAK = "<br/>";
    public static final String HTML_HORIZONTAL_LINE = "---------------------------------------------------------";
    public static final String COMMA_PLUS_SPACE_DELIMITER = ", ";
    public static final String DOT_DELIMITER = ".";
    public static final String DOT_REGEX_DELIMITER = "\\.";
    public static final String BACK_TO_HOME_START_TAG = "<h2><a href='../";
    public static final String BACK_TO_HOME_END_TAG = "'><code>HOME</code></a></h2>";

    public static final String FILTER_DESCRIPTION = "any phrase in table would be searched, e.g. ";
    public static final String INVOKING_TAG = "invoking";
    public static final String REQUEST_TAG = "request";
    public static final String RESPONSE_TAG = "response";
    public static final String Exception_TAG_RED_COLOR = "<span style='color:red'>exception</span>";
    public static final String Exception_TAG = "exception";
    public static final String INCOMING_ISO_MSG_TAG = "incoming isomsg";
    public static final String OUTGOING_ISO_MSG_TAG = "outgoing isomsg";
    public static final String STATISTICS_TAG = "statistics";

    public static final String THREAD_PREFIX = "Thread-";
}
