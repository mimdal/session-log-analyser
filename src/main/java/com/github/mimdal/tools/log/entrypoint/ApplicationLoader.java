package com.github.mimdal.tools.log.entrypoint;

import com.github.mimdal.tools.log.analysis.Analysis;
import com.github.mimdal.tools.log.chain.*;
import com.github.mimdal.tools.log.extract.SaveSessionLog;
import com.github.mimdal.tools.log.render.HTML;
import com.github.mimdal.tools.log.dto.CurrentLogParams;
import com.github.mimdal.tools.log.readfile.ReadLogFile;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author M.dehghan
 * @since 2020-07-28
 */
@CommandLine.Command(name = "Session Log Analyzer", sortOptions = false,
        description = {"SessionLogAnalyzer commands parsing and usage help.",}, optionListHeading = "@|bold %nOptions|@:%n")
public class ApplicationLoader implements Callable {

    @CommandLine.Option(names = {"-l", "--log"}, required = true, description = "a full log line to get a parameters " +
            "related to a session.\n" +
            "e.g: [INFO]  2020-06-29T22:30:37.573 session_id:[ce4bbc6f-9426 ...")
    private String sampleLog;

    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "absolute path of log file.")
    private String logFile;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display help message")
    private boolean usageHelpRequested;

    public static void main(String[] args) {
        int exitCode;
        exitCode = new CommandLine(new ApplicationLoader()).execute(args);
        assert exitCode == 0;
    }

    @Override
    public Object call() {
        if (usageHelpRequested) {
            new CommandLine(new ApplicationLoader()).usage(System.out);
        }
        chainCall();
        return null;
    }

    private void chainCall() {
        LogProcess chain_1 = new ReadLogFile(logFile);
        LogProcess chain_2 = new Analysis();
        LogProcess chain_3 = new SaveSessionLog();
        LogProcess chain_4 = new HTML();
        chain_1.setNext(chain_2);
        chain_2.setNext(chain_3);
        chain_3.setNext(chain_4);
        chain_1.process(
                WrapperObject.builder()
                        .currentLogParams(new CurrentLogParams(sampleLog.trim()))
                        .build()
        );
    }
}
