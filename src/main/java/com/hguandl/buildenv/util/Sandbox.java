package com.hguandl.buildenv.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sandbox {
    private String path;
    private String code;

    private static List<String> history = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Sandbox.class);

    private static long LIFE_MILLIS;
    private static int CNT_THRESHOLD;

    private final String pathPrefix = "/tmp/";
    private final String buildCmd = "/usr/bin/g++ -Wall -std=c++11 -o %s %s";

    public Sandbox(String code) {
        if (history.size() > CNT_THRESHOLD) {
            cleanup(false);
        }
        path = pathPrefix + UUID.randomUUID().toString();
        history.add(path);
        this.code = code;
    }

    private String codePath() {
        return path + "/a.cpp";
    }

    private String executablePath() {
        return path + "/a.out";
    }

    private void compile() throws IOException {
        boolean success = new File(path).mkdir();
        if (!success) {
            throw new IOException();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(codePath()))) {
            writer.write(code);
        }
    }

    public String result() throws IOException, InterruptedException {
        List<String> output = new ArrayList<>();
        int exitCode = 0;
        try {
            compile();
            Process process;
            process = Runtime.getRuntime().exec(String.format(buildCmd, executablePath(), codePath()));
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            input.lines().forEach(output::add);
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw e;
        }
        StringBuilder result = new StringBuilder();
        for (String s : output) {
            result.append(s).append("\n");
        }
        if (result.length() == 0 && exitCode == 0) {
            result.append("No error(s).\n");
        }
        return result.toString();
    }

    public static void cleanup(boolean force) {
        try {
            for (String s : history) {
                File f = new File(s);
                if (!force && System.currentTimeMillis() - f.lastModified() < LIFE_MILLIS) {
                    continue;
                }
                FileUtils.deleteDirectory(f);
                history.remove(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(String.format("History cleaned up to %d", history.size()));
    }

    public static void setup(long life, int cnt) {
        LIFE_MILLIS = life;
        CNT_THRESHOLD = cnt;

        logger.info(String.format("Life: %d", LIFE_MILLIS));
        logger.info(String.format("Count: %d", CNT_THRESHOLD));
    }
}
