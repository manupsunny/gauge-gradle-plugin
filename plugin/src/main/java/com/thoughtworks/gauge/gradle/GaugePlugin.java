package com.thoughtworks.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Map;

public class GaugePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("gauge", GaugeExtension.class);
        project.getTasks().create("gauge", GaugeTask.class);
    }
}