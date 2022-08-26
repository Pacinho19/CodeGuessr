package pl.pacinho.codeguessr.model;

import java.util.List;

public record CodeDto(String projectName, String filePath, int lineIndex, List<String> fullCode) {

    public String getPath(){
        return projectName + "/" + filePath;
    }
}
