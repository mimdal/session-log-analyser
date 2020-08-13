package com.github.mimdal.tools.log.analysis;

import com.github.mimdal.tools.log.chain.*;
import com.github.mimdal.tools.log.constants.Constants;
import com.github.mimdal.tools.log.entity.LogEntity;

import java.util.*;

import static com.github.mimdal.tools.log.constants.Constants.*;

/**
 * @author M.dehghan
 * @since 2020-07-28
 */
public class Analysis implements LogProcess {

    private LogProcess next;
    private Map<String, LogEntity> responseIndicatorRequestMap;
    private Map<String, String> sessionThreads;

    @Override
    public void setNext(LogProcess next) {
        this.next = next;
    }

    @Override
    public void process(WrapperObject wrapperObject) {
        fieldInitialize();
        LogEntity removeLogFromMap = null;
        int threadsOrder = 1;
        for (LogEntity log : wrapperObject.getSessionLogEntities()) {
            if (!sessionThreads.containsKey(log.getThread())) {
                sessionThreads.put(log.getThread(), THREAD_PREFIX + threadsOrder++);
            }
            String logContent = log.getContent().toString();
            if (logContent.contains(Constants.SERVICE_REQUEST_LOG_INDICATOR)) {
                String firstLine = logContent.split("\n")[1];
                String serviceName = firstLine.trim().split(Constants.REQUEST)[0];
                String usefulData = sessionThreads.get(log.getThread()) + COMMA_PLUS_SPACE_DELIMITER + serviceName;
                log.setUsefulData(usefulData);
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
        wrapperObject.setSessionThreads(sessionThreads);
        next.process(wrapperObject);
    }

    private void fieldInitialize() {
        responseIndicatorRequestMap = new HashMap<>();
        sessionThreads = new HashMap<>();
    }
}
