package com.github.mimdal.tools.log.analysis;

import com.github.mimdal.tools.log.chain.*;
import com.github.mimdal.tools.log.constants.Constants;
import com.github.mimdal.tools.log.entity.LogEntity;

import java.util.*;

/**
 * @author M.dehghan
 * @since 2020-07-28
 */
public class Analysis implements LogProcess {
    private LogProcess next;
    private Map<String, LogEntity> responseIndicatorRequestMap;

    @Override
    public void setNext(LogProcess next) {
        this.next = next;
    }

    @Override
    public void process(WrapperObject wrapperObject) {
        fieldInitialize();
        LogEntity removeLogFromMap = null;
        for (LogEntity log : wrapperObject.getSessionLogEntities()) {
            String logContent = log.getContent().toString();
            if (logContent.contains(Constants.SERVICE_REQUEST_LOG_INDICATOR)) {
                String firstLine = logContent.split("\n")[1];
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
        next.process(wrapperObject);
    }

    private void fieldInitialize() {
        responseIndicatorRequestMap = new HashMap<>();
    }
}
