package pl.pacinho.codeguessr.tools.svn;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import pl.pacinho.codeguessr.config.RepoProperties;
import pl.pacinho.codeguessr.model.NodeDto;
import pl.pacinho.codeguessr.model.ProjectDto;
import pl.pacinho.codeguessr.model.enums.RsaRepository;

public class RepositoryTreeTool {

    private final RsaRepository rsaRepository;
    private HashMap<String, NodeDto> nodes;
    private ProjectDto projectDto;
    private SVNRepository repository;

    public RepositoryTreeTool(RsaRepository rsaRepository) {
        this.rsaRepository = rsaRepository;
        nodes = new HashMap<>();
    }

    public ProjectDto getSchema() {

        String url = RepoProperties.MAIN_URL + rsaRepository.getUrl();
        setupLibrary();

        repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            System.err.println("error while creating an SVNRepository for location '"
                               + url + "': " + svne.getMessage());
            System.exit(1);
        }
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(RepoProperties.USERNAME, RepoProperties.PASSWORD);
        repository.setAuthenticationManager(authManager);

        projectDto = new ProjectDto(getProjectName(url));
        try {
            listEntries("");
        } catch (SVNException svne) {
            System.err.println("error while listing entries: " + svne.getMessage());
        }
        return projectDto;
    }

    private String getProjectName(String url) {
        return url.replace(RepoProperties.MAIN_URL, "").split("/")[0];
    }

    private void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    private void listEntries(String path)
            throws SVNException {

        Collection entries = repository.getDir(path, -1, null,
                (Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();

            if (path.startsWith("src") || entry.getName().equals("src")) {
//                System.out.println("/" + (path.equals("") ? "" : path + "/") + entry.getName());
                NodeDto nodeDto = nodes.computeIfAbsent( (path.equals("") ? "" : path + "/") + entry.getName(), s -> new NodeDto(entry.getName(), entry.getKind() != SVNNodeKind.DIR));
                if (path.isEmpty())
                    projectDto.addNode(nodeDto);
                else
                    addChild(path, nodeDto);

                if (entry.getKind() == SVNNodeKind.DIR)
                    listEntries((path.equals("")) ? entry.getName() : path + "/" + entry.getName());
                else
                    addFileContent(path + "/" + entry.getName());

            }
        }
    }

    private void addFileContent(String path) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            repository.getFile(path, -1, new SVNProperties(), baos);
            projectDto.addFileContent(path, baos.toString(StandardCharsets.UTF_8));
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    private void addChild(String path, NodeDto nodeDto) {
        getParent(path)
                .addChild(nodeDto);
    }

    private NodeDto getParent(String path) {
        return nodes.get(path);
    }
}