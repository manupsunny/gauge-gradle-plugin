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

package com.thoughtworks.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class GaugePlugin implements Plugin<Project> {

    private static final String GAUGE = "gauge";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(GAUGE, GaugeExtension.class);
        GaugeTask gaugeTask = project.getTasks().create(GAUGE, GaugeTask.class);
        Task compileTestJava = project.getTasks().findByName("testClasses");

        if (compileTestJava != null) {
            gaugeTask.dependsOn(compileTestJava);
        }
    }
}