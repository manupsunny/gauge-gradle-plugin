package com.thoughtworks.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Map;

public class GaugePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("gauge", GaugeExtension.class);
        pullCommandLineArguments(project);
        project.getTasks().create("gauge", GaugeTask.class);
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
        String specsDir = (String) properties.get("specsDir");
        if (specsDir != null) {
            extension.setSpecsDir(specsDir);
        }
    }

    private void setInParallel(Map<String, ?> properties, GaugeExtension extension) {
        String inParallel = (String) properties.get("inParallel");
        if (inParallel != null) {
            extension.setInParallel(inParallel.equals("true"));
        }
    }

    private void setTags(Map<String, ?> properties, GaugeExtension extension) {
        String tags = (String) properties.get("tags");
        if (tags != null) {
            extension.setTags(tags);
        }
    }

    private void setEnv(Map<String, ?> properties, GaugeExtension extension) {
        String env = (String) properties.get("env");
        if (env != null) {
            extension.setEnv(env);
        }
    }

    private void setNodes(Map<String, ?> properties, GaugeExtension extension) {
        String nodes = (String) properties.get("nodes");
        if (nodes != null) {
            extension.setNodes(Integer.parseInt(nodes));
        }
    }

    private void setAdditionalFlags(Map<String, ?> properties, GaugeExtension extension) {
        String flags = (String) properties.get("additionalFlags");
        if (flags != null) {
            extension.setAdditionalFlags(flags);
        }
    }

    private void setClasspath(Map<String, ?> properties, GaugeExtension extension) {
        String classpath = (String) properties.get("classpath");
        if (classpath != null) {
            extension.setClasspath(classpath);
        }
    }

}