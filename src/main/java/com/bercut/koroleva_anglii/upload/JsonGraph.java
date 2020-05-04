package com.bercut.koroleva_anglii.upload;

import java.util.List;
import java.util.Map;

public class JsonGraph {
    private String id;
    private String type;
    private String label;
    private JsonGroupMetadata metadata;
    private Map<String, JsonNode> nodes;
    private List<JsonEdge> edges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JsonGroupMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonGroupMetadata metadata) {
        this.metadata = metadata;
    }

    public Map<String, JsonNode> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, JsonNode> nodes) {
        this.nodes = nodes;
    }

    public List<JsonEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<JsonEdge> edges) {
        this.edges = edges;
    }
}
