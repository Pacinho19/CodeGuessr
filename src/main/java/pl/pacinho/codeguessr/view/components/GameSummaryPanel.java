package pl.pacinho.codeguessr.view.components;

import pl.pacinho.codeguessr.utils.SpringUtilities;
import pl.pacinho.codeguessr.view.components.tools.GameTimer;

import javax.swing.*;
import java.math.BigDecimal;

public class GameSummaryPanel extends JPanel {

    private GameTimer gameTimer;

    public GameSummaryPanel() {
        initComponents();
        initView();
        initValues();
    }

    public void initValues() {
        setRoundText(1);
        setPointsText(BigDecimal.ZERO);
        initGameTimer();
    }

    private void initGameTimer() {
        gameTimer = new GameTimer(this);
    }

    private void initComponents() {
        roundL = new JLabel();
        pointsL = new JLabel();
        timeL = new JLabel();
    }

    private void initView() {
        this.setLayout(new SpringLayout());
        this.add(timeL);
        this.add(roundL);
        this.add(pointsL);

        SpringUtilities.makeCompactGrid(this, 3, 1, 5, 5, 5, 5);
    }

    public void startTime() {
        gameTimer.start();
    }
    public void stopTime() {
        gameTimer.stop();
    }


    public void setTimeText(String time) {
        timeL.setText(timeText(time));
    }

    private String timeText(String time) {
        return "Time: " + time;
    }

    public void setRoundText(int i) {
        roundL.setText(roundText(i));
    }

    public void setPointsText(BigDecimal i) {
        pointsL.setText(pointsText(i));
    }

    private String pointsText(BigDecimal i) {
        return "Points : " + i;
    }

    private String roundText(int i) {
        return "Round " + i + "/5";
    }

    private JLabel roundL;
    private JLabel pointsL;
    private JLabel timeL;
}
