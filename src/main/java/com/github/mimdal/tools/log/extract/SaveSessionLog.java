package com.github.mimdal.tools.log.extract;

import com.github.mimdal.tools.log.analysis.Analysis;
import com.github.mimdal.tools.log.chain.*;
import com.github.mimdal.tools.log.constants.Constants;
import com.github.mimdal.tools.log.entity.LogEntity;
import com.github.mimdal.tools.log.dto.CurrentLogParams;
import com.github.mimdal.tools.log.render.HTML;
import com.github.mimdal.tools.log.thirdparty.ThirdPartyUtils;

import java.io.File;
import java.util.List;

import static com.github.mimdal.tools.log.constants.Constants.OUT_PUT_FILE_NAME;

/**
 * @author M.dehghan
 * @since 2020-07-30
 */
public class SaveSessionLog implements LogProcess {
    private LogProcess next;

    @Override
    public void setNext(LogProcess next) {
        this.next = next;
    }

    @Override
    public void process(WrapperObject wrapperObject) {
        saveToFile(wrapperObject);
        next.process(wrapperObject);
    }

    public void saveToFile(WrapperObject wrapperObject) {
        File newHtmlFile = new File(String.format("%s/output/%s/%s.log", Constants.APP_DIRECTORY,
                wrapperObject.getCurrentLogParams().getSession(), OUT_PUT_FILE_NAME));
        StringBuilder sessionExtracted = new StringBuilder();
        for (LogEntity log : wrapperObject.getSessionLogEntities()) {
            sessionExtracted
                    .append(log.getContent());
        }
        ThirdPartyUtils.writeStringToFile(newHtmlFile, sessionExtracted.toString());
    }
}
