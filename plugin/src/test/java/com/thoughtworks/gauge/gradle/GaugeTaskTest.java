package com.thoughtworks.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class GaugeTaskTest {
    private GaugeTask task;
    private Project project;

    @Before
    public void setup() {
        GaugePlugin plugin = new GaugePlugin();
        project = ProjectBuilder.builder().build();
        plugin.apply(project);
        task = (GaugeTask) project.getTasks().findByPath("gauge");
    }

    @Test
    public void shouldLoadProperties() {
        setGaugeOptions();
        task.gauge();

        List<String> command = task.createGaugeCommand();
        assertTrue(command.contains("gauge"));
        assertTrue(command.contains("specsFolder"));
        assertTrue(command.contains("--parallel"));
        assertTrue(command.contains("-n"));
        assertTrue(command.contains("2"));
        assertTrue(command.contains("--env"));
        assertTrue(command.contains("dev"));
        assertTrue(command.contains("--tags"));
        assertTrue(command.contains("tag1"));
        assertTrue(command.contains("--verbose"));
    }

    private void setGaugeOptions() {
        GaugeExtension gauge = (GaugeExtension) project.getExtensions().findByName("gauge");
        gauge.setSpecsDir("specsFolder");
        gauge.setInParallel(true);
        gauge.setNodes(2);
        gauge.setEnv("dev");
        gauge.setTags("tag1");
        gauge.setAdditionalFlags("--verbose");
    }
}