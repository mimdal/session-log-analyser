package com.github.mimdal.tools.log.analysis;

import com.github.mimdal.tools.log.constants.Constants;
import com.github.mimdal.tools.log.entity.LogEntity;

import java.util.*;

/**
 * @author M.dehghan
 * @since 2020-07-28
 */
public class Analysis {
    private List<LogEntity> sessionLogEntities;
    private Map<String, LogEntity> responseIndicatorRequestMap;
    private Statistics statistics;

    public void setSessionLogEntities(List<LogEntity> sessionLogEntities) {
        this.sessionLogEntities = sessionLogEntities;
    }

    public void process() {
        fieldInitialize();
        LogEntity removeLogFromMap = null;
        for (LogEntity log : sessionLogEntities) {
            String logContent = log.getContent().toString();
            if (logContent.contains(Constants.SERVICE_REQUEST_LOG_INDICATOR)) {
                String firstLine = logContent.split("\n")[0];
                String serviceName = firstLine.trim().split(Constants.REQUEST)[0];
                log.setUsefulData(serviceName);
                String serviceResponseLogIndicator = "\t" + serviceName + Constants.RESPONSE;
                responseIndicatorRequestMap.put(serviceResponseLogIndicator, log);
            } else {
                for (Map.Entry<String, LogEntity> entry : responseIndicatorRequestMap.entrySet()) {
                    if (logContent.contains(entry.getKey())) {
                        log.setUsefulData(entry.getValue().getUsefulData());
                        log.setRelatedLogs(entry.getValue());
                        entry.getValue().setRelatedLogs(log);
                        removeLogFromMap = entry.getValue();
                        break;
                    }
                }
                responseIndicatorRequestMap.remove(removeLogFromMap);
            }
        }
    }

    private void fieldInitialize() {
        responseIndicatorRequestMap = new HashMap<>();
    }
}
