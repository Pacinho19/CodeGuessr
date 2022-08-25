package pl.pacinho.codeguessr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class ProjectDto {

    private String name;
    @Setter
    private Set<NodeDto> nodes;
    @Getter
    private Map<String, String> filesContent;

    public ProjectDto(String name) {
        this.name = name;
        nodes = new HashSet<>();
        filesContent = new HashMap<>();
    }

    public void addNode(NodeDto nodeDto) {
        nodes.add(nodeDto);
    }

    public void addFileContent(String name, String content) {
        filesContent.put(name, content);
    }
}
