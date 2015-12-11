package com.thoughtworks.gauge.gradle.util;

import com.thoughtworks.gauge.gradle.GaugeExtension;
import org.gradle.api.Project;

import java.util.Map;

public class PropertyManager {
    public static final String SPECS_DIR = "specsDir";
    public static final String IN_PARALLEL = "inParallel";
    public static final String TAGS = "tags";
    public static final String ENV = "env";
    public static final String NODES = "nodes";
    public static final String ADDITIONAL_FLAGS = "additionalFlags";
    public static final String CLASSPATH = "classpath";

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
            extension.setInParallel("true".equals(inParallel));
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
        String classpath = (String) properties.get(CLASSPATH);
        if (classpath != null) {
            extension.setClasspath(classpath);
        }
    }
}
