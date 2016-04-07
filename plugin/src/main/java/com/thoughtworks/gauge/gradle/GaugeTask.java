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
import com.thoughtworks.gauge.gradle.util.ProcessBuilderFactory;
import com.thoughtworks.gauge.gradle.util.PropertyManager;
import com.thoughtworks.gauge.gradle.util.Util;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GaugeTask extends DefaultTask {
    private final Logger log = LoggerFactory.getLogger("gauge");
    private GaugeExtension extension;

    @TaskAction
    public void gauge() {
        Project project = getProject();
        extension = project.getExtensions().findByType(GaugeExtension.class);
        PropertyManager propertyManager = new PropertyManager(project, extension);
        propertyManager.setProperties();

        ProcessBuilder builder = new ProcessBuilderFactory(extension).create();
        info("Executing command => " + builder.command());
        try {
            Process process = builder.start();
            executeGaugeSpecs(process);
        } catch (IOException e) {
            throw new GaugeExecutionFailedException("Gauge or Java runner is not installed! Read http://getgauge.io/documentation/user/current/getting_started/download_and_install.html");
        }
    }

    public void executeGaugeSpecs(Process process) throws GaugeExecutionFailedException {
        try {
            Util.inheritIO(process.getInputStream(), System.out);
            Util.inheritIO(process.getErrorStream(), System.err);
            if (process.waitFor() != 0) {
                throw new GaugeExecutionFailedException("Execution failed for one or more tests!");
            }
        } catch (InterruptedException | NullPointerException e) {
            throw new GaugeExecutionFailedException(e);
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