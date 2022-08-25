package pl.pacinho.codeguessr.view;

import pl.pacinho.codeguessr.model.RoundResultDto;
import pl.pacinho.codeguessr.utils.SpringUtilities;
import pl.pacinho.codeguessr.view.controller.CodeGuessrViewController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RoundResultView extends JDialog {

    private final String[] tableHeaders = new String[]{"", "CORRECT", "ANSWER"};
    private final CodeGuessrViewController codeGuessrViewController;
    private final RoundResultDto roundResultDto;

    public RoundResultView(CodeGuessrViewController codeGuessrViewController, RoundResultDto roundResultDto) {
        this.codeGuessrViewController = codeGuessrViewController;
        this.roundResultDto = roundResultDto;

        initFrame();
        initComponents();
        initView();
        initActions();
    }

    private String getPath(String[] pathArr) {
        return String.join("/", pathArr);
    }

    private void initComponents() {

        correctPathJL = new JLabel("Correct Path");
        correctPathValueJL = new JLabel(getPath(roundResultDto.partsOfCorrectPath()));
        selectedPathJL = new JLabel("Selected Path");
        selectedPathValueJL = new JLabel(getPath(roundResultDto.partsOfSelectedPath()));

        correctLineJL = new JLabel("Correct Line");
        correctLineValueJL = new JLabel("" + roundResultDto.correctLineNumber());
        selectedLineJL = new JLabel("Selected Line");
        selectedLineValueJL = new JLabel("" + roundResultDto.selectedLineNumber());

        confirmJB = new JButton("Confirm");

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMaximum(5_000);
        progressBar.setString("You earned " + roundResultDto.result().intValue() + " points!");
        progressBar.setValue(roundResultDto.result().intValue());
    }

    private void initView() {
        Container main = this.getContentPane();
        main.setLayout(new BorderLayout());
        main.add(progressBar, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new BorderLayout());
        JPanel resultPanel2 = new JPanel(new BorderLayout());

        JPanel correctValuesPanel = new JPanel(new SpringLayout());
        correctValuesPanel.add(correctPathJL);
        correctValuesPanel.add(correctPathValueJL);
        correctValuesPanel.add(correctLineJL);
        correctValuesPanel.add(correctLineValueJL);
        correctValuesPanel.setBorder(new TitledBorder("Correct"));
        SpringUtilities.makeCompactGrid(correctValuesPanel, 2, 2, 5, 5, 5, 5);


        JPanel answerValuesPanel = new JPanel(new SpringLayout());
        answerValuesPanel.add(selectedPathJL);
        answerValuesPanel.add(selectedPathValueJL);
        answerValuesPanel.add(selectedLineJL);
        answerValuesPanel.add(selectedLineValueJL);
        answerValuesPanel.setBorder(new TitledBorder("Correct"));
        SpringUtilities.makeCompactGrid(answerValuesPanel, 2, 2, 5, 5, 5, 5);

        resultPanel.add(correctValuesPanel, BorderLayout.NORTH);
        resultPanel.add(answerValuesPanel, BorderLayout.SOUTH);
        resultPanel2.add(resultPanel, BorderLayout.NORTH);

        main.add(resultPanel2, BorderLayout.CENTER);
        main.add(confirmJB, BorderLayout.SOUTH);
    }

    private void initActions() {
        confirmJB.addActionListener(e -> {
            RoundResultView.this.dispose();
            codeGuessrViewController.finishRound();
        });
    }

    private void initFrame() {
        this.setUndecorated(true);
        this.setModal(true);
        this.setSize(codeGuessrViewController.getCodeGuessrView().getWidth(), 300);
        this.setLocationRelativeTo(codeGuessrViewController.getCodeGuessrView());
    }

    private JLabel correctPathJL;
    private JLabel correctPathValueJL;
    private JLabel selectedPathJL;
    private JLabel selectedPathValueJL;
    private JLabel correctLineJL;
    private JLabel correctLineValueJL;
    private JLabel selectedLineJL;
    private JLabel selectedLineValueJL;
    private JButton confirmJB;
    private JProgressBar progressBar;

}
