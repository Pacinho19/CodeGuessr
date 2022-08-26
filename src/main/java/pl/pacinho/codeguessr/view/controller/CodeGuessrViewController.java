package pl.pacinho.codeguessr.view.controller;

import lombok.RequiredArgsConstructor;
import pl.pacinho.codeguessr.model.*;
import pl.pacinho.codeguessr.utils.CodeFinderUtils;
import pl.pacinho.codeguessr.utils.RsaProjectUtils;
import pl.pacinho.codeguessr.view.CodeGuessrView;
import pl.pacinho.codeguessr.view.RoundResultView;
import pl.pacinho.codeguessr.view.components.tools.RoundTimer;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CodeGuessrViewController {

    private CodeGuessrView codeGuessrView;
    private final GameModeDto gameModeDto;
    private List<ProjectDto> projects;
    private Map<String, ProjectDto> projectsMap;
    private CodeDto randomCode;
    private GameSummaryDto gameSummary;
    private int currentPos;

    public CodeGuessrViewController(CodeGuessrView codeGuessrView, GameModeDto gameModeDto) {
        this.codeGuessrView = codeGuessrView;
        this.gameModeDto = gameModeDto;
        initProjects();
        initGameSummary();
        gameMode();
    }

    private void gameMode() {
        codeGuessrView.enableMoveButtons(gameModeDto.canMove());
    }

    private void initProjects() {
        projects = RsaProjectUtils.getProjects();
        projectsMap = projects.stream()
                .collect(Collectors.toMap(ProjectDto::getName, Function.identity()));
    }

    private void initGameSummary() {
        gameSummary = new GameSummaryDto();
        codeGuessrView.getGameSummaryPanel().startTime();
    }

    public void initRandomCode() {
        randomCode = CodeFinderUtils.getRandomCode(projects);
        initCodeToFind();

        if (gameModeDto.timeLimit())
            new RoundTimer(this).start();
    }

    private void initCodeToFind() {
        currentPos = randomCode.lineIndex() + 1;
        codeToFind();
    }

    public void up() {
        if (currentPos - 1 == 1) return;
        currentPos--;
        codeToFind();
    }

    public void down() {
        if (currentPos + 1 == randomCode.fullCode().size()) return;
        currentPos++;
        codeToFind();
    }

    public void startPosition() {
        initCodeToFind();
    }

    private void codeToFind() {
        String text = getCodeLine(currentPos - 1) + "\n"
                      + getCodeLine(currentPos) + "\n"
                      + getCodeLine(currentPos + 1);
        codeGuessrView.getLineToFindTA().setText(text);
        setLineToFindTextColor(text);
    }

    private String getCodeLine(int i) {
        if (i < 0) return "";
        if (i > randomCode.fullCode().size() - 1) return "";
        String text = randomCode.fullCode().get(i - 1);
        if (i == randomCode.lineIndex() + 1) return text;
        if (!text.startsWith("package ")) return text;
        return "package [...]";
    }

    private void setLineToFindTextColor(String text) {
        String line = randomCode.fullCode().get(randomCode.lineIndex());
        try {
            Highlighter highlighter = codeGuessrView.getLineToFindTA().getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
            int p0 = text.toLowerCase().indexOf(line.toLowerCase());
            int p1 = p0 + line.length();
            if (p0 > -1) {
                highlighter.addHighlight(p0, p1, painter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getCodeOfClass() {
        List<String> partsOfPath = getSelectedPath();
        if (partsOfPath.isEmpty()) return "";

        String fileName = partsOfPath.get(partsOfPath.size() - 1);
        if (!fileName.endsWith(".java")) return "";

        return projectsMap.get(partsOfPath.get(0))
                .getFilesContent()
                .get(getFilePath(partsOfPath, true));
    }

    private List<String> getSelectedPath() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                codeGuessrView.getRepositoryTree().getLastSelectedPathComponent();

        if (node == null) return Collections.emptyList();
        if (codeGuessrView.getRepositoryTree().getSelectionPath() == null) return Collections.emptyList();

        return Stream.of(codeGuessrView.getRepositoryTree().getSelectionPath()
                        .toString()
                        .replace("[", "")
                        .replace("]", "")
                        .split(","))
                .skip(1) //Main Tree Node
                .map(String::trim)
                .toList();
    }

    private String getFilePath(List<String> partsOfPath, boolean skipProjectName) {
        return partsOfPath.stream()
                .skip(skipProjectName ? 1 : 0) //Project Name
                .collect(Collectors.joining("/"));

    }

    public void guess() {
        Integer answerI = getAnswerI();
        String selectedPath = getFilePath(getSelectedPath(), false);

//        if (answerI == -1) {
//            JOptionPane.showMessageDialog(codeGuessrView, "Invalid lineIndex number. Only integers.");
//            return;
//        }
//
//        if (!selectedPath.contains(".")) {
//            JOptionPane.showMessageDialog(codeGuessrView, "Invalid path. Please select .java file, not directory.");
//            return;
//        }

        checkAnswer(selectedPath, answerI);
    }

    private void checkAnswer(String selectedPath, Integer answerI) {
        String[] partsOfCorrectPath = randomCode.getPath().split("/");
        String[] partsOfSelectedPath = selectedPath.split("/");

        int correct = 0;
        for (int i = 0; i < partsOfCorrectPath.length; i++) {
            if (i > partsOfSelectedPath.length - 1) break;

            if (partsOfSelectedPath[i].equals(partsOfCorrectPath[i]))
                correct++;
            else
                break;
        }

        BigDecimal projectPoints = BigDecimal.valueOf(4_000 * ((double) correct / (double) partsOfCorrectPath.length)).setScale(0, RoundingMode.CEILING);
        double correctPercent = ((double) Math.abs(randomCode.lineIndex() + 1 - answerI) / (double) randomCode.fullCode().size()) * 100L;

        BigDecimal linePoints = !projectPoints.equals(BigDecimal.valueOf(4_000)) ? BigDecimal.ZERO : BigDecimal.valueOf(1_000L * ((100 - correctPercent) / 100));
        BigDecimal result = projectPoints.add(linePoints).setScale(0, RoundingMode.HALF_UP);
        gameSummary.addPoints(result);

        result(result, partsOfCorrectPath, partsOfSelectedPath, randomCode.lineIndex() + 1, answerI);
    }

    private void result(BigDecimal result, String[] partsOfCorrectPath, String[] partsOfSelectedPath, Integer correctLineNumber, Integer selectedLineNumber) {
        new RoundResultView(this, new RoundResultDto(result, partsOfCorrectPath, partsOfSelectedPath, correctLineNumber, selectedLineNumber))
                .setVisible(true);
    }

    public void finishRound() {
        if (gameSummary.getRound() == 5) {
            finishGame();
            return;
        }
        nextRound();
    }

    private void nextRound() {
        codeGuessrView.clearComponents();
        gameSummary.nextRound();
        initRandomCode();
        setPointsAndRound();
    }

    private void finishGame() {
        gameSummary.finishGame();
        setPointsAndRound();
        codeGuessrView.getGameSummaryPanel().stopTime();

        if (endGameOptionPane() == JOptionPane.YES_OPTION) newGame();
        else System.exit(0);

    }

    private void setPointsAndRound() {
        codeGuessrView.getGameSummaryPanel().setPointsText(gameSummary.getPoints());
        codeGuessrView.getGameSummaryPanel().setRoundText(gameSummary.getRound());
    }

    private int endGameOptionPane() {
        return JOptionPane.showConfirmDialog(null, "You earned summary " + gameSummary.getPoints() + ".\nGame Time : " + gameSummary.getGameDuration() + " seconds.\nReady to play again ?", "Game End!",
                JOptionPane.YES_NO_OPTION);
    }

    private void newGame() {
        codeGuessrView.clearComponents();
        codeGuessrView.getGameSummaryPanel().initValues();
        initRandomCode();
        initGameSummary();
    }

    private Integer getAnswerI() {
        try {
            return Integer.parseInt(codeGuessrView.getAnswer());
        } catch (Exception e) {
            return -1;
        }
    }

    public CodeGuessrView getCodeGuessrView() {
        return codeGuessrView;
    }

    public List<ProjectDto> getProjects() {
        return projects;
    }

}
