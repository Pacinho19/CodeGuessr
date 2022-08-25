package pl.pacinho.codeguessr.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class NodeDto {

    private String name;
    private boolean file;
    private Set<NodeDto> children;

    public NodeDto(String name, boolean isFile) {
        this.name = name;
        this.file = isFile;
        children = new HashSet<>();
    }

    public void addChild(NodeDto child) {
        children.add(child);
    }
}
