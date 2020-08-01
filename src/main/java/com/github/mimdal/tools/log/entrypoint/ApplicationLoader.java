package com.github.mimdal.tools.log.entrypoint;

import com.github.mimdal.tools.log.analysis.Analysis;
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
    public Object call() throws Exception {
        if (usageHelpRequested) {
            new CommandLine(new ApplicationLoader()).usage(System.out);
        }
        CurrentLogParams currentLogParams = new CurrentLogParams(sampleLog.trim());
        ReadLogFile readLogFile = new ReadLogFile(currentLogParams);

        readLogFile.processLogFile(logFile);

        Analysis analysis = new Analysis();
        analysis.setSessionLogEntities(readLogFile.getSessionLogEntities());
        analysis.process();

        SaveSessionLog saveSessionLog = new SaveSessionLog(readLogFile.getSessionLogEntities(), currentLogParams);
        saveSessionLog.saveToFile();

        HTML htmlRendering = new HTML(currentLogParams, readLogFile.getSessionLogEntities());
        htmlRendering.generate();

        return null;
    }
}
