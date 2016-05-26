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
import org.gradle.api.Project;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class PropertyManager {
    private static final String ENV = "env";
    private static final String TAGS = "tags";
    private static final String NODES = "nodes";
    private static final String SPECS_DIR = "specsDir";
    private static final String CLASSPATH = "classpath";
    private static final String IN_PARALLEL = "inParallel";
    private static final String ADDITIONAL_FLAGS = "additionalFlags";
    private static final String RUNTIME = "runtime";
    private static final String CLASSES = "/classes";

    private Project project;
    private GaugeExtension extension;

    public PropertyManager(Project project, GaugeExtension extension) {
        this.project = project;
        this.extension = extension;
    }

    public void setProperties() {
        Map<String, ?> properties = project.getProperties();
        setTags(properties);
        setInParallel(properties);
        setNodes(properties);
        setEnv(properties);
        setAdditionalFlags(properties);
        setClasspath(properties);
        setSpecsDir(properties);
    }

    private void setSpecsDir(Map<String, ?> properties) {
        String specsDir = (String) properties.get(SPECS_DIR);
        if (specsDir != null) {
            extension.setSpecsDir(specsDir);
        }
    }

    private void setInParallel(Map<String, ?> properties) {
        String inParallel = (String) properties.get(IN_PARALLEL);
        if (inParallel != null) {
            extension.setInParallel(inParallel.equals("true"));
        }
    }

    private void setNodes(Map<String, ?> properties) {
        String nodes = (String) properties.get(NODES);
        if (nodes != null) {
            extension.setNodes(Integer.parseInt(nodes));
        }
    }

    private void setTags(Map<String, ?> properties) {
        String tags = (String) properties.get(TAGS);
        if (tags != null) {
            extension.setTags(tags);
        }
    }

    private void setEnv(Map<String, ?> properties) {
        String env = (String) properties.get(ENV);
        if (env != null) {
            extension.setEnv(env);
        }
    }

    private void setAdditionalFlags(Map<String, ?> properties) {
        String flags = (String) properties.get(ADDITIONAL_FLAGS);
        if (flags != null) {
            extension.setAdditionalFlags(flags);
        }
    }

    private void setClasspath(Map<String, ?> properties) {
        HashSet<String> classpaths = new HashSet<>();
        addRuntimeClasspaths(project, classpaths);
        addBuildClasspaths(project, classpaths);

        String classpath = "", customClasspath = (String) properties.get(CLASSPATH);
        for(String path : classpaths){
            classpath += path + File.pathSeparator;
        }
        extension.setClasspath(classpath);
//        TODO: Implement provision to pass custom classpath
    }

    private void addRuntimeClasspaths(Project project, HashSet<String> classPaths) {
        project.getConfigurations().stream()
                .filter(configuration -> configuration.getName()
                        .toLowerCase().endsWith(RUNTIME))
                .forEach(configuration -> {
                    String fileList = configuration.getAsFileTree().getAsPath();
                    classPaths.addAll(Arrays.asList(fileList.split(File.pathSeparator)));
                });
    }

    private void addBuildClasspaths(Project project, HashSet<String> classPaths) {
        File classFolders = new File(project.getBuildDir().getAbsolutePath() + CLASSES);
        for (File classFolder : classFolders.listFiles()) {
            classPaths.add(classFolder.getAbsolutePath());
        }
    }
}
