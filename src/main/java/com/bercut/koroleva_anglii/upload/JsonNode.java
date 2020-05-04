package com.bercut.koroleva_anglii.upload;

public class JsonNode {
    private String label;
    private JsonNodeMetadata metadata;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JsonNodeMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNodeMetadata metadata) {
        this.metadata = metadata;
    }
}
