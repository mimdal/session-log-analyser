package com.github.mimdal.tools.log.chain;

import com.github.mimdal.tools.log.dto.CurrentLogParams;
import com.github.mimdal.tools.log.entity.LogEntity;
import lombok.*;

import java.util.*;

/**
 * @author M.dehghan
 * @since 2020-08-02
 */
@Setter
@Getter
@Builder
@ToString
public class WrapperObject {
    private CurrentLogParams currentLogParams;
    private List<LogEntity> sessionLogEntities;
    private Set<String> sessionThreads;
}
