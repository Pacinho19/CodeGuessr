package pl.pacinho.codeguessr.utils;

import pl.pacinho.codeguessr.model.CodeDto;
import pl.pacinho.codeguessr.model.ProjectDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class CodeFinderUtils {

    public static CodeDto getRandomCode(List<ProjectDto> projects) {
        ProjectDto projectDto = projects.get(RandomUtils.getInt(0, projects.size() - 1));
        Collection<String> contents = projectDto.getFilesContent().keySet();
        String filePath = contents.stream()
                .toList()
                .get(RandomUtils.getInt(0, contents.size() - 1));

        List<String> allLines = Arrays.stream(projectDto.getFilesContent()
                        .get(filePath)
                        .split("\n"))
                .toList();

        List<String> lines = allLines.stream()
                .filter(s -> s.trim().length() > 15)
                .toList();

        String text = lines.get(RandomUtils.getInt(0, lines.size() - 1));

        return new CodeDto(projectDto.getName(), filePath, allLines.indexOf(text), allLines);
    }
}
