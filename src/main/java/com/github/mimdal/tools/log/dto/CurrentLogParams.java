package com.github.mimdal.tools.log.dto;

import com.github.mimdal.tools.log.util.Utils;
import lombok.*;

import java.util.regex.*;

import static com.github.mimdal.tools.log.constants.Constants.*;

/**
 * @author M.dehghan
 * @since 2020-07-28
 */
@Getter
@ToString
public class CurrentLogParams {
    private String channel;
    private String thread;
    private String client;
    private String session;

    public CurrentLogParams(String sampleLogLine) {
        Pattern pattern = Pattern.compile(APPLICATION_LOG_REGEX);
        Matcher matcher = pattern.matcher(sampleLogLine);
        if (matcher.matches()) {
            this.channel = matcher.group(CHANNEL_MATCHER).trim();
            this.thread = matcher.group(THREAD_MATCHER).trim();
            this.client = matcher.group(CLIENT_MATCHER).trim();
            this.session = matcher.group(SESSION_MATCHER).trim();
        } else {
            Utils.systemExit("sample Log Line not ok or may be need to change Regex pattern!!");
        }
    }
}
