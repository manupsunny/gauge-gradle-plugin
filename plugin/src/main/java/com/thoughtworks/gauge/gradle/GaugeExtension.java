package com.thoughtworks.gauge.gradle;

public class GaugeExtension {

    private String specsDir;
    private Boolean inParallel = false;
    private Integer nodes;
    private String env;
    private String tags;
    private String classpath;
    private String additionalFlags;

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
}
