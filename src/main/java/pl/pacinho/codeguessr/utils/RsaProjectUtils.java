package pl.pacinho.codeguessr.utils;

import pl.pacinho.codeguessr.model.ProjectDto;
import pl.pacinho.codeguessr.model.enums.RsaRepository;
import pl.pacinho.codeguessr.tools.svn.RepositoryTreeTool;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RsaProjectUtils {

    public static List<ProjectDto> getProjects() {
        return Arrays.stream(RsaRepository.values())
                .map(rr -> new RepositoryTreeTool(rr).getSchema())
                .filter(Objects::nonNull)
                .toList();
    }
}
