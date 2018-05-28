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

package com.thoughtworks.gauge.gradle.util;

import com.thoughtworks.gauge.gradle.GaugeExtension;
import com.thoughtworks.gauge.gradle.exception.GaugeExecutionFailedException;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessBuilderFactory {
    private static final String GAUGE = "gauge";
    private static final String RUN_SUBCOMMAND = "run";
    private static final String ENV_FLAG = "--env";
    private static final String NODE_FLAG = "-n";
    private static final String TAGS_FLAG = "--tags";
    private static final String SPECS_FLAG = "specs";
    private static final String PARALLEL_FLAG = "--parallel";
    private static final String CUSTOM_CLASSPATH = "gauge_custom_classpath";
    private static final String WORKING_DIRECTORY_FLAG = "--dir";

    private final Logger log = LoggerFactory.getLogger(GAUGE);
    private GaugeExtension extension;
    private Project project;

    public ProcessBuilderFactory(GaugeExtension extension, Project project) {
        this.extension = extension;
        this.project = project;
    }

    public ProcessBuilder create() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(createGaugeCommand());

        setClasspath(builder);
        return builder;
    }

    private void setClasspath(ProcessBuilder builder) {
        String classpath = extension.getClasspath();

        log.debug("Setting Custom classpath => %s", classpath);
        builder.environment().put(CUSTOM_CLASSPATH, classpath);
    }

    private ArrayList<String> createGaugeCommand() {
        ArrayList<String> command = new ArrayList<>();
        addGaugeExecutable(command);
        command.add(RUN_SUBCOMMAND);
        addTags(command);
        addParallelFlags(command);
        addEnv(command);
        addWorkingDirectory(command);
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
                command.add(NODE_FLAG);
                command.add(Integer.toString(nodes));
            }
        }
    }

    private void addSpecsDir(ArrayList<String> command) {
        String specsDirectoryPath = extension.getSpecsDir();

        if (specsDirectoryPath != null) {
            validateSpecsDirectory(specsDirectoryPath, command);
        }
    }

    private void validateSpecsDirectory(String specsDirectoryPath, ArrayList<String> command) {
        if (specsDirectoryPath.contains(" ")) {
            for (String file : specsDirectoryPath.split(" ")) {
                validateSingleSpecsDirectory(file, command);
            }
        } else if (specsDirectoryPath.contains(",")) {
            for (String file : specsDirectoryPath.split(",")) {
                validateSingleSpecsDirectory(file, command);
            }
        } else {
            validateSingleSpecsDirectory(specsDirectoryPath, command);
        }
    }

    private void validateSingleSpecsDirectory(String specsDirectoryPath, ArrayList<String> command) {
        File specsDirectory = new File(getCurrentProjectPath() + File.separator + specsDirectoryPath);
        if (!specsDirectory.exists()) {
            log.error("Specs directory specified is not existing!");
            throw new GaugeExecutionFailedException("Specs directory specified is not existing!");
        }
        command.add(specsDirectoryPath);
    }

    private String getCurrentProjectPath() {
        return project.getProjectDir().getAbsolutePath();
    }

    private void addWorkingDirectory(ArrayList<String> command) {
        String workingDirectory = getCurrentProjectPath();
        command.add(WORKING_DIRECTORY_FLAG);
        command.add(workingDirectory);
    }

    private void addTags(ArrayList<String> command) {
        String tags = extension.getTags();
        if (tags != null && !tags.isEmpty()) {
            command.add(TAGS_FLAG);
            command.add(tags);
        }
    }

    private void addGaugeExecutable(ArrayList<String> command) {
        String gauge;
        String gaugeRoot = extension.getGaugeRoot();
        if (StringUtils.isNotEmpty(gaugeRoot)) {
            gauge = Paths.get(gaugeRoot, "bin", GAUGE).toString();
        } else {
            gauge = GAUGE;
        }
        command.add(gauge);
    }
}
