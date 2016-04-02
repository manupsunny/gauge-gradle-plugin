package com.thoughtworks.gauge.gradle;

import com.thoughtworks.gauge.gradle.exception.GaugeExecutionFailedException;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GaugeTaskTest {
    private GaugeTask task;
    private Project project;

    @Before
    public void setUp() {
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
        gauge.setInParallel(true);
        gauge.setNodes(2);
        gauge.setEnv("dev");
        gauge.setTags("tag1");
        gauge.setAdditionalFlags("--verbose");
    }

    @Test
    public void shouldLoadSpecsDirPropertyIfFolderExists() {
        setSpecsDirOptionWithExistingFolder();
        task.gauge();

        List<String> command = task.createGaugeCommand();
        assertTrue(command.contains("specsFolder"));
    }

    private void setSpecsDirOptionWithExistingFolder() {
        File currentDir = new File("./specsFolder");
        currentDir.mkdir();

        GaugeExtension gauge = (GaugeExtension) project.getExtensions().findByName("gauge");
        gauge.setSpecsDir("specsFolder");
    }

    @Test(expected = GaugeExecutionFailedException.class)
    public void shouldThrowExceptionWhenLoadingSpecsDirPropertyWhenFolderDoesNotExists() {
        setSpecsDirOptionWithNonExistingFolder();
        task.gauge();

        List<String> command = task.createGaugeCommand();
        assertTrue(!command.contains("specsFolder"));
    }

    private void setSpecsDirOptionWithNonExistingFolder() {
        GaugeExtension gauge = (GaugeExtension) project.getExtensions().findByName("gauge");
        gauge.setSpecsDir("nonSpecsFolder");
    }
}