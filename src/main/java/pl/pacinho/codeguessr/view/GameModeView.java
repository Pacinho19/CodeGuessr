package pl.pacinho.codeguessr.view;

import pl.pacinho.codeguessr.model.GameModeDto;
import pl.pacinho.codeguessr.utils.SpringUtilities;

import javax.swing.*;
import java.awt.*;

public class GameModeView extends JFrame {

    public GameModeView() {
        initFrame();
        initComponents();
        initView();
        initActions();
    }

    private void initFrame() {
        this.setTitle("Game mode");
        this.setSize(300, 100);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        moveJB = new JCheckBox("Move", true);
        timeLimitJB = new JCheckBox("Time limit", false);
        startJB = new JButton("Start");
    }

    private void initView() {
        Container main = this.getContentPane();
        main.setLayout(new BorderLayout());

        JPanel checkBoxPanel = new JPanel(new SpringLayout());
        checkBoxPanel.add(moveJB);
        checkBoxPanel.add(timeLimitJB);
        SpringUtilities.makeCompactGrid(checkBoxPanel, 1, 2, 5, 5, 5, 5);

        main.add(checkBoxPanel, BorderLayout.CENTER);
        main.add(startJB, BorderLayout.SOUTH);
    }

    private void initActions() {
        startJB.addActionListener(e ->
                new CodeGuessrView( new GameModeDto(moveJB.isSelected(), timeLimitJB.isSelected()))
                .setVisible(true));
    }

    private JCheckBox moveJB;
    private JCheckBox timeLimitJB;
    private JButton startJB;
}
