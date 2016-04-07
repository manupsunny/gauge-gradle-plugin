package com.thoughtworks.gauge.gradle.util;

import com.thoughtworks.gauge.gradle.GaugeExtension;
import com.thoughtworks.gauge.gradle.exception.GaugeExecutionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessBuilderFactory {
    private final Logger log = LoggerFactory.getLogger("gauge");
    private GaugeExtension extension;
    private ProcessBuilder builder;

    private static final String TAGS_FLAG = "--tags";
    private static final String GAUGE = "gauge";
    private static final String PARALLEL_FLAG = "--parallel";
    private static final String DEFAULT_SPECS_DIR = "specs";
    private static final String NODES_FLAG = "-n";
    private static final String ENV_FLAG = "--env";
    private static final String GAUGE_CUSTOM_CLASSPATH_ENV = "gauge_custom_classpath";

    public ProcessBuilderFactory(GaugeExtension extension) {
        this.extension = extension;
    }

    public ProcessBuilder create() {
        builder = new ProcessBuilder();
        builder.command(createGaugeCommand());

        setClasspath(builder);
        return builder;
    }

    private void setClasspath(java.lang.ProcessBuilder builder) {
        String classpath = extension.getClasspath();
        if (classpath == null) {
            classpath = "";
        }
        debug("Setting Custom classpath => %s", classpath);
        builder.environment().put(GAUGE_CUSTOM_CLASSPATH_ENV, classpath);
    }

    public ArrayList<String> createGaugeCommand() {
        ArrayList<String> command = new ArrayList<>();
        command.add(GAUGE);
        addTags(command);
        addParallelFlags(command);
        addEnv(command);
        addAdditionalFlags(command);
        addSpecsDir(command);
        return command;
    }

    private void addEnv(ArrayList<String> command) {
        String env = extension.getEnv();
        if (env != null && !env.isEmpty()) {
            command.add(ENV_FLAG);
            command.add(env);
        }
    }

    private void addAdditionalFlags(ArrayList<String> command) {
        String flags = extension.getAdditionalFlags();
        if (flags != null) {
            command.addAll(Arrays.asList(flags.split(" ")));
        }
    }

    private void addParallelFlags(ArrayList<String> command) {
        Boolean inParallel = extension.isInParallel();
        Integer nodes = extension.getNodes();
        if (inParallel != null && inParallel) {
            command.add(PARALLEL_FLAG);
            if (nodes != null && nodes != 0) {
                command.add(NODES_FLAG);
                command.add(Integer.toString(nodes));
            }
        }
    }

    private void addSpecsDir(ArrayList<String> command) {
        String specsDirectoryPath = extension.getSpecsDir();

        if (specsDirectoryPath != null) {
            validateSpecsDirectory(specsDirectoryPath);
            command.add(specsDirectoryPath);
        } else {
            warn("Property 'specsDir' not set. Using default value => '%s'", DEFAULT_SPECS_DIR);
            command.add(DEFAULT_SPECS_DIR);
        }
    }

    private void validateSpecsDirectory(String specsDirectoryPath) {
        File specsDirectory = new File(specsDirectoryPath);
        if (!specsDirectory.exists()) {
            error("Specs directory specified is not existing!");
            throw new GaugeExecutionFailedException("Specs directory specified is not existing!");
        }
    }

    private void addTags(ArrayList<String> command) {
        String tags = extension.getTags();
        if (tags != null && !tags.isEmpty()) {
            command.add(TAGS_FLAG);
            command.add(tags);
        }
    }

    private void warn(String format, String... args) {
        log.warn(String.format(format, args));
    }

    private void debug(String format, String... args) {
        log.debug(String.format(format, args));
    }

    private void error(String format, String... args) {
        log.error(String.format(format, args));
    }

    private void info(String format, String... args) {
        log.info(String.format(format, args));
    }
}
