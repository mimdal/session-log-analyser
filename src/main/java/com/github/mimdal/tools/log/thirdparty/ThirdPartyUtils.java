package com.github.mimdal.tools.log.thirdparty;

import com.github.mimdal.tools.log.util.Utils;
import com.google.common.html.HtmlEscapers;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author M.dehghan
 * @since 2020-07-29
 */
public class ThirdPartyUtils {
    public static void writeStringToFile(final File file, final String data) {
        try {
            org.apache.commons.io.FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Utils.systemExit("something happen to save file(" + file.getName() + ").");
        }
    }

    public static String inputStreamToString(final InputStream input) {
        String result = null;
        try {
            result = IOUtils.toString(input, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Utils.systemExit("zero result FOUND! try again by new arguments.");
        }
        return result;
    }

    public static String escapeHtml(String input) {
        return HtmlEscapers.htmlEscaper().escape(input);
    }
}
