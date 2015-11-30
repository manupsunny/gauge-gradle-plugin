package com.thoughtworks.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.SortedMap;

import static org.junit.Assert.*;

public class GaugePluginTest {
    private Project project;

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    public void pluginShouldBeAddedOnApply() {
        project.getPluginManager().apply("gauge");
        assertTrue(project.getPlugins().getPlugin("gauge") instanceof GaugePlugin);
        assertFalse(project.getPlugins().getPlugin("gauge") instanceof JavaPlugin);
    }

    @Test
    public void taskShouldBeAddedOnApply() {
        project.getPluginManager().apply("gauge");
        TaskContainer tasks = project.getTasks();
        assertEquals(1, tasks.size());

        SortedMap<String, Task> tasksMap = tasks.getAsMap();
        Task gauge = tasksMap.get("gauge");
        assertTrue(gauge instanceof GaugeTask);
    }
}