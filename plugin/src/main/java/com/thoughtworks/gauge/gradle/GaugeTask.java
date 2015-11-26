package com.thoughtworks.gauge.gradle;

import com.thoughtworks.gauge.gradle.exception.GaugeExecutionFailedException;
import com.thoughtworks.gauge.gradle.util.Util;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.util.ArrayList;

public class GaugeTask extends DefaultTask {

    @TaskAction
    public void gauge() {
        try {
            executeGaugeSpecs();
        } catch (GaugeExecutionFailedException e) {
            e.printStackTrace();
        }
    }

    private void executeGaugeSpecs() throws GaugeExecutionFailedException {
        try {
            ProcessBuilder builder = createProcessBuilder();
            debug("Executing => " + builder.command());
            Process process = builder.start();
            Util.InheritIO(process.getInputStream(), System.out);
            Util.InheritIO(process.getErrorStream(), System.err);
            if (process.waitFor() != 0) {
                throw new GaugeExecutionFailedException();
            }
        } catch (InterruptedException | IOException | NullPointerException e) {
            throw new GaugeExecutionFailedException(e);
        }
    }

    private ProcessBuilder createProcessBuilder() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(createGaugeCommand());
        return builder;
    }

    public ArrayList<String> createGaugeCommand() {
        ArrayList<String> command = new ArrayList<String>();
        command.add("gauge");
        command.add("specs");
        return command;
    }

    private void debug(String format, String... args) {
//        getLog().debug("[gauge] "+ String.format(format, args));
    }
}