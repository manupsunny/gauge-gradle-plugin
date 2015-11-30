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

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Map;

public class GaugePlugin implements Plugin<Project> {

    public static final String GAUGE = "gauge";
    public static final String SPECS_DIR = "specsDir";
    public static final String IN_PARALLEL = "inParallel";
    public static final String TAGS = "tags";
    public static final String ENV = "env";
    public static final String NODES = "nodes";
    public static final String ADDITIONAL_FLAGS = "additionalFlags";
    public static final String CLASSPATH = "classpath";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(GAUGE, GaugeExtension.class);
        pullCommandLineArguments(project);
        project.getTasks().create(GAUGE, GaugeTask.class);
    }

    private void pullCommandLineArguments(Project project) {
        GaugeExtension extension = project.getExtensions().findByType(GaugeExtension.class);
        Map<String, ?> properties = project.getProperties();
        setSpecsDir(properties, extension);
        setInParallel(properties, extension);
        setTags(properties, extension);
        setEnv(properties, extension);
        setNodes(properties, extension);
        setAdditionalFlags(properties, extension);
        setClasspath(properties, extension);
    }

    private void setSpecsDir(Map<String, ?> properties, GaugeExtension extension) {
        String specsDir = (String) properties.get(SPECS_DIR);
        if (specsDir != null) {
            extension.setSpecsDir(specsDir);
        }
    }

    private void setInParallel(Map<String, ?> properties, GaugeExtension extension) {
        String inParallel = (String) properties.get(IN_PARALLEL);
        if (inParallel != null) {
            extension.setInParallel("true".equals(inParallel));
        }
    }

    private void setTags(Map<String, ?> properties, GaugeExtension extension) {
        String tags = (String) properties.get(TAGS);
        if (tags != null) {
            extension.setTags(tags);
        }
    }

    private void setEnv(Map<String, ?> properties, GaugeExtension extension) {
        String env = (String) properties.get(ENV);
        if (env != null) {
            extension.setEnv(env);
        }
    }

    private void setNodes(Map<String, ?> properties, GaugeExtension extension) {
        String nodes = (String) properties.get(NODES);
        if (nodes != null) {
            extension.setNodes(Integer.parseInt(nodes));
        }
    }

    private void setAdditionalFlags(Map<String, ?> properties, GaugeExtension extension) {
        String flags = (String) properties.get(ADDITIONAL_FLAGS);
        if (flags != null) {
            extension.setAdditionalFlags(flags);
        }
    }

    private void setClasspath(Map<String, ?> properties, GaugeExtension extension) {
        String classpath = (String) properties.get(CLASSPATH);
        if (classpath != null) {
            extension.setClasspath(classpath);
        }
    }

}