package pl.pacinho.codeguessr.view;

import pl.pacinho.codeguessr.model.GameModeDto;
import pl.pacinho.codeguessr.model.NodeDto;
import pl.pacinho.codeguessr.utils.SpringUtilities;
import pl.pacinho.codeguessr.view.controller.CodeGuessrViewController;
import pl.pacinho.codeguessr.utils.NumberFormatterUtils;
import pl.pacinho.codeguessr.view.components.GameSummaryPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CodeGuessrView extends JFrame {

    private CodeGuessrViewController codeGuessrViewController;

    public CodeGuessrView(GameModeDto gameModeDto) {
        this.setTitle("Repository View");
        this.setSize(1100, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        initComponents();
        initView();
        initActions();
        codeGuessrViewController = new CodeGuessrViewController(this, gameModeDto);
        codeGuessrViewController.initRandomCode();
        createProjectsTree();
    }


    private void initComponents() {
        parentNode = new DefaultMutableTreeNode("RSA Projects");
        repositoryTree = new JTree(parentNode);

        lineToFindTA = new JTextArea(2, 50);
        lineToFindTA.setEditable(false);

        upJB = new JButton("UP");
        downJB = new JButton("DOWN");
        startPosJB = new JButton("START");

        codeTA = new JTextArea(2, 50);
        codeTA.setEditable(false);

        answerL = new JLabel("Line number :");
        answerTF = new JFormattedTextField(NumberFormatterUtils.intFormatter);
        answerTF.setText("1");
        answerTF.setColumns(4);
        guessJB = new JButton("Guess");

        gameSummaryPanel = new GameSummaryPanel();
        gameSummaryPanel.setMinimumSize(new Dimension(100, this.getHeight()));
        gameSummaryPanel.setPreferredSize(new Dimension(100, this.getHeight()));

        roundTimer = new JProgressBar();
        roundTimer.setStringPainted(true);
        roundTimer.setString("Unlimited time");
    }

    private void initView() {
        Container main = this.getContentPane();
        main.setLayout(new BorderLayout());

        main.add(roundTimer, BorderLayout.NORTH);

        JScrollPane jScrollPane = new JScrollPane(repositoryTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setMinimumSize(new Dimension(300, this.getHeight()));
        jScrollPane.setPreferredSize(new Dimension(300, 9999));
        main.add(jScrollPane, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        codeTA.setBorder(new TitledBorder("Code of class"));

        JPanel guessPanel = new JPanel(new BorderLayout());
        guessPanel.add(new JScrollPane(lineToFindTA), BorderLayout.NORTH);

        JPanel linePanel = new JPanel(new BorderLayout());
        linePanel.add(new JScrollPane(lineToFindTA), BorderLayout.CENTER);
        guessPanel.add(linePanel, BorderLayout.NORTH);

        JPanel navigationPanel = new JPanel(new SpringLayout());
        navigationPanel.add(upJB);
        navigationPanel.add(downJB);
        navigationPanel.add(startPosJB);
        SpringUtilities.makeCompactGrid(navigationPanel, 3, 1, 5, 5, 5, 5);
        linePanel.add(navigationPanel, BorderLayout.EAST);

        JPanel answerPanel = new JPanel(new BorderLayout());

        JPanel answerLinePanel = new JPanel(new SpringLayout());
        answerLinePanel.add(answerL);
        answerLinePanel.add(answerTF);
        SpringUtilities.makeCompactGrid(answerLinePanel, 1, 2, 5, 5, 5, 5);

        answerPanel.add(answerLinePanel, BorderLayout.WEST);
        answerPanel.add(guessJB, BorderLayout.CENTER);
        guessPanel.add(answerPanel, BorderLayout.SOUTH);

        centerPanel.add(guessPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(codeTA), BorderLayout.CENTER);
        lineToFindTA.setBorder(new TitledBorder("Code to find"));
        main.add(centerPanel, BorderLayout.CENTER);
        main.add(gameSummaryPanel, BorderLayout.EAST);
    }

    private void initActions() {
        repositoryTree.addTreeSelectionListener(e -> codeTA.setText(codeGuessrViewController.getCodeOfClass()));

        guessJB.addActionListener(e -> codeGuessrViewController.guess());

        answerTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int code = e.getKeyChar();
                if (!(code >= 48 && code <= 57)) {
                    e.consume();
                    return;
                }
            }
        });

        upJB.addActionListener(e -> codeGuessrViewController.up());
        downJB.addActionListener(e -> codeGuessrViewController.down());
        startPosJB.addActionListener(e -> codeGuessrViewController.startPosition());
    }

    private void createProjectsTree() {
        codeGuessrViewController.getProjects().forEach(r -> {
            DefaultMutableTreeNode node = createNode(r.getName());
            r.getNodes().forEach(c -> appendChild(node, c));
            parentNode.add(node);
        });
    }

    private void appendChild(DefaultMutableTreeNode node, NodeDto c) {
        DefaultMutableTreeNode child = createNode(c.getName());
        node.add(child);
        c.getChildren().forEach(c1 -> appendChild(child, c1));
    }

    private DefaultMutableTreeNode createNode(String name) {
        return new DefaultMutableTreeNode(name);
    }

    public JTree getRepositoryTree() {
        return repositoryTree;
    }

    public String getAnswer() {
        return answerTF.getText();
    }

    public JTextArea getLineToFindTA() {
        return lineToFindTA;
    }

    public JProgressBar getRoundTimer() {
        return roundTimer;
    }

    public void enableMoveButtons(boolean enable) {
        upJB.setEnabled(enable);
        downJB.setEnabled(enable);
        startPosJB.setEnabled(enable);
    }

    public GameSummaryPanel getGameSummaryPanel() {
        return gameSummaryPanel;
    }

    public void clearComponents() {
        codeTA.setText("");
        answerTF.setText("1");
        lineToFindTA.setText("");
    }

    private JTree repositoryTree;
    private DefaultMutableTreeNode parentNode;
    private JTextArea lineToFindTA;
    private JButton upJB;
    private JButton downJB;
    private JButton startPosJB;
    private JTextArea codeTA;
    private JLabel answerL;
    private JFormattedTextField answerTF;
    private JButton guessJB;
    private GameSummaryPanel gameSummaryPanel;
    private JProgressBar roundTimer;

}
