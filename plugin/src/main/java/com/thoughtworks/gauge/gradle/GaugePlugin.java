package com.thoughtworks.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GaugePlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
//        target.getPluginManager().apply(JavaPlugin.class);
//        GaugeTask test =
        target.getTasks().create("gauge", GaugeTask.class);
//        test.setGroup("gauge");

    }

}