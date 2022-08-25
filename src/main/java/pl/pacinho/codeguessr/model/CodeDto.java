package pl.pacinho.codeguessr.model;

public record CodeDto(String projectName, String filePath,  String text, int line, int countOfLines) {

    public String getPath(){
        return projectName + "/" + filePath;
    }
}
