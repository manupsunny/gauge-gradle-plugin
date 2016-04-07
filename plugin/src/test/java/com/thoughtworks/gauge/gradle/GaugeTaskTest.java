package com.thoughtworks.gauge.gradle;

import com.thoughtworks.gauge.gradle.exception.GaugeExecutionFailedException;
import com.thoughtworks.gauge.gradle.util.ProcessBuilderFactory;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GaugeTaskTest {
    private GaugeTask task;
    private Project project;
    private ProcessBuilderFactory factory;

    @Before
    public void setUp() {
        GaugePlugin plugin = new GaugePlugin();
        project = ProjectBuilder.builder().build();
        plugin.apply(project);
        task = (GaugeTask) project.getTasks().findByPath("gauge");

        factory = mock(ProcessBuilderFactory.class);
    }

    @Test
    public void shouldLoadProperties() throws InterruptedException {
        ArrayList<String> expectedCommand = new ArrayList<>();
        expectedCommand.add("gauge");
        expectedCommand.add("--parallel");
        expectedCommand.add("-n");
        expectedCommand.add("2");
        expectedCommand.add("--env");
        expectedCommand.add("dev");
        expectedCommand.add("--tags");
        expectedCommand.add("tag1");
        expectedCommand.add("--verbose");
        when(factory.create()).thenReturn(new ProcessBuilder(expectedCommand));

        Process process = mock(Process.class);
        when(process.waitFor()).thenReturn(0);

        GaugeExtension gauge = (GaugeExtension) project.getExtensions().findByName("gauge");
        gauge.setInParallel(true);
        gauge.setNodes(2);
        gauge.setEnv("dev");
        gauge.setTags("tag1");
        gauge.setAdditionalFlags("--verbose");
        task.executeGaugeSpecs(process);

        List<String> command = factory.create().command();
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

    @Test
    public void shouldLoadSpecsDirPropertyIfFolderExists() throws InterruptedException {
        ArrayList<String> expectedCommand = new ArrayList<>();
        expectedCommand.add("gauge");
        expectedCommand.add("specsFolder");
        when(factory.create()).thenReturn(new ProcessBuilder(expectedCommand));

        Process process = mock(Process.class);
        when(process.waitFor()).thenReturn(0);

        GaugeExtension gauge = (GaugeExtension) project.getExtensions().findByName("gauge");
        gauge.setSpecsDir("specsFolder");
        task.executeGaugeSpecs(process);

        List<String> command = factory.create().command();
        assertTrue(command.contains("specsFolder"));
    }

    @Test(expected = GaugeExecutionFailedException.class)
    public void shouldThrowExceptionWhenLoadingSpecsDirPropertyWhenFolderDoesNotExists() {
        ArrayList<String> expectedCommand = new ArrayList<>();
        expectedCommand.add("gauge");
        when(factory.create()).thenThrow(GaugeExecutionFailedException.class);

        GaugeExtension gauge = (GaugeExtension) project.getExtensions().findByName("gauge");
        gauge.setSpecsDir("nonSpecsFolder");
        task.gauge();

        List<String> command = factory.create().command();
        assertTrue(!command.contains("specsFolder"));
    }

}