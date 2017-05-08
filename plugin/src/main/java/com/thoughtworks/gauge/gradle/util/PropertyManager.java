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
import org.gradle.api.artifacts.Configuration;

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
    private static final String IN_PARALLEL = "inParallel";
    private static final String ADDITIONAL_FLAGS = "additionalFlags";
    private static final String GAUGE_ROOT = "gaugeRoot";
    private static final String RUNTIME = "runtime";
    private static final String CLASSES = "/classes";

    private Project project;
    private GaugeExtension extension;
    private Map<String, ?> properties;

    public PropertyManager(Project project, GaugeExtension extension) {
        this.project = project;
        this.extension = extension;
        this.properties = project.getProperties();
    }

    public void setProperties() {
        setTags();
        setInParallel();
        setNodes();
        setEnv();
        setAdditionalFlags();
        setClasspath();
        setSpecsDir();
        setGaugeRoot();
    }

    private void setSpecsDir() {
        String specsDir = (String) properties.get(SPECS_DIR);
        if (specsDir != null) {
            extension.setSpecsDir(specsDir);
        }
    }

    private void setInParallel() {
        String inParallel = (String) properties.get(IN_PARALLEL);
        if (inParallel != null) {
            extension.setInParallel("true".equals(inParallel));
        }
    }

    private void setNodes() {
        String nodes = (String) properties.get(NODES);
        if (nodes != null) {
            extension.setNodes(Integer.parseInt(nodes));
        }
    }

    private void setTags() {
        String tags = (String) properties.get(TAGS);
        if (tags != null) {
            extension.setTags(tags);
        }
    }

    private void setEnv() {
        String env = (String) properties.get(ENV);
        if (env != null) {
            extension.setEnv(env);
        }
    }

    private void setAdditionalFlags() {
        String flags = (String) properties.get(ADDITIONAL_FLAGS);
        if (flags != null) {
            extension.setAdditionalFlags(flags);
        }
    }

    private void setClasspath() {
        HashSet<String> classpaths = new HashSet<>();
        String classpath = "";

        addRuntimeClasspaths(classpaths);
        addBuildClasspaths(classpaths);

        for (String path : classpaths) {
            classpath += path + File.pathSeparator;
        }
        extension.setClasspath(classpath);
    }

    private void setGaugeRoot() {
        String gaugeRoot = (String) properties.get(GAUGE_ROOT);
        if (gaugeRoot != null) {
            extension.setGaugeRoot(gaugeRoot);
        }
    }

    private void addRuntimeClasspaths(HashSet<String> classPaths) {
        for (Configuration configuration : project.getConfigurations()) {
            if(configuration.getName().toLowerCase().endsWith(RUNTIME)){
                String fileList = configuration.getAsFileTree().getAsPath();
                classPaths.addAll(Arrays.asList(fileList.split(File.pathSeparator)));
            }
        }
    }

    private void addBuildClasspaths(HashSet<String> classPaths) {
        File classFolders = new File(project.getBuildDir().getAbsolutePath() + CLASSES);
        for (File file : classFolders.listFiles()){
            classPaths.add(file.getAbsolutePath());
        }
    }
}
