/*
 *  Copyright 2015 Manu Sunny
 *
 *  This file is part of Gauge-gradle-plugin.
 *
 *  Gauge-gradle-plugin is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gauge-gradle-plugin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Gauge-gradle-plugin.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.thoughtworks.gauge.gradle;

import com.thoughtworks.gauge.gradle.exception.GaugeExecutionFailedException;
import com.thoughtworks.gauge.gradle.util.Util;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GaugeTask extends DefaultTask {
    private final Logger log = LoggerFactory.getLogger("gauge");
    private GaugeExtension extension;

    public static final String TAGS_FLAG = "--tags";
    public static final String GAUGE = "gauge";
    public static final String PARALLEL_FLAG = "--parallel";
    public static final String DEFAULT_SPECS_DIR = "specs";
    private static final String NODES_FLAG = "-n";
    public static final String GAUGE_CUSTOM_CLASSPATH_ENV = "gauge_custom_classpath";
    private static final String ENV_FLAG = "--env";

    @TaskAction
    public void gauge() {
        try {
            extension = getProject().getExtensions().findByType(GaugeExtension.class);
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
            Util.inheritIO(process.getInputStream(), System.out);
            Util.inheritIO(process.getErrorStream(), System.err);
            if (process.waitFor() != 0) {
                throw new GaugeExecutionFailedException();
            }
        } catch (InterruptedException | IOException | NullPointerException e) {
            throw new GaugeExecutionFailedException(e);
        }
    }

    public ProcessBuilder createProcessBuilder() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(createGaugeCommand());
        String customClasspath = createCustomClasspath();
        debug("Setting Custom classpath => %s", customClasspath);
        builder.environment().put(GAUGE_CUSTOM_CLASSPATH_ENV, customClasspath);
        return builder;
    }

    private String createCustomClasspath() {
        String classpath = extension.getClasspath();
        if (classpath == null || classpath.isEmpty()) {
            return "";
        }
        return classpath;
    }

    public ArrayList<String> createGaugeCommand() {
        ArrayList<String> command = new ArrayList<>();
        command.add(GAUGE);
        addSpecsDir(command);
        addParallelFlags(command);
        addEnv(command);
        addTags(command);
        addAdditionalFlags(command);
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
        String specsDir = extension.getSpecsDir();
        if (specsDir != null) {
            command.add(specsDir);
        } else {
            warn("Property 'specsDir' not set. Using default value => '%s'", DEFAULT_SPECS_DIR);
            command.add(DEFAULT_SPECS_DIR);
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
}