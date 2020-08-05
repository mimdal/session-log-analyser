package com.github.mimdal.tools.log.chain;

/**
 * @author M.dehghan
 * @since 2020-08-02
 */
public interface LogProcess {
    void setNext(LogProcess next);
    void process(WrapperObject wrapperObject);
}
