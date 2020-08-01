package com.github.mimdal.tools.log.extract;

import com.github.mimdal.tools.log.constants.Constants;
import com.github.mimdal.tools.log.entity.LogEntity;
import com.github.mimdal.tools.log.dto.CurrentLogParams;
import com.github.mimdal.tools.log.thirdparty.ThirdPartyUtils;

import java.io.File;
import java.util.List;

import static com.github.mimdal.tools.log.constants.Constants.OUT_PUT_FILE_NAME;

/**
 * @author M.dehghan
 * @since 2020-07-30
 */
public class SaveSessionLog {
    private List<LogEntity> sessionLogEntities;
    private CurrentLogParams currentLogParams;

    public SaveSessionLog(List<LogEntity> sessionLogEntities, CurrentLogParams currentLogParams) {
        this.sessionLogEntities = sessionLogEntities;
        this.currentLogParams = currentLogParams;
    }

    public void saveToFile() {
        File newHtmlFile = new File(String.format("%s/output/%s/%s.log", Constants.APP_DIRECTORY,
                currentLogParams.getSession(), OUT_PUT_FILE_NAME));
        StringBuilder sessionExtracted = new StringBuilder();
        for (LogEntity log : sessionLogEntities) {
            sessionExtracted
                    .append(log.getContent());
        }
        ThirdPartyUtils.writeStringToFile(newHtmlFile, sessionExtracted.toString());
    }
}
