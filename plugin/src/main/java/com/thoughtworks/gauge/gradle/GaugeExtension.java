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

public class GaugeExtension {

    private String specsDir;
    private Boolean inParallel = false;
    private Integer nodes;
    private String env;
    private String tags;
    private String classpath;
    private String additionalFlags;
    private String gaugeRoot;

    public String getSpecsDir() {
        return specsDir;
    }

    public void setSpecsDir(String specsDir) {
        this.specsDir = specsDir;
    }

    public Boolean isInParallel() {
        return inParallel;
    }

    public void setInParallel(Boolean inParallel) {
        this.inParallel = inParallel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Integer getNodes() {
        return nodes;
    }

    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getAdditionalFlags() {
        return additionalFlags;
    }

    public void setAdditionalFlags(String additionalFlags) {
        this.additionalFlags = additionalFlags;
    }

    public String getGaugeRoot() {
        return gaugeRoot;
    }

    public void setGaugeRoot(String gaugeRoot) {
        this.gaugeRoot = gaugeRoot;
    }
}
