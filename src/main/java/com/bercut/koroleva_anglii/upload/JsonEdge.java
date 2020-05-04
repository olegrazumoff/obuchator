package com.bercut.koroleva_anglii.upload;

public class JsonEdge {
    private String source;
    private String target;
    private JsonEdgeMetadata metadata;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public JsonEdgeMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonEdgeMetadata metadata) {
        this.metadata = metadata;
    }
}
