package com.github.mimdal.tools.log.analysis;

import lombok.*;

import java.time.Duration;

/**
 * @author M.dehghan
 * @since 2020-07-30
 */
@Builder
@Getter
@ToString
public class Statistics {
    private Duration duration;
    private int totalServiceCall;
    private int totalCoreServiceCall;
    private String serviceCallFlow;
}
