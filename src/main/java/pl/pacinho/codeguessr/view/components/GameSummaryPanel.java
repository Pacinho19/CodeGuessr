package pl.pacinho.codeguessr.view.components;

import pl.pacinho.codeguessr.utils.SpringUtilities;

import javax.swing.*;
import java.math.BigDecimal;

public class GameSummaryPanel extends JPanel {


    public GameSummaryPanel() {
        initComponents();
        initView();
        initValues();
    }

    public void initValues() {
        setRoundText(1);
        setPointsText(BigDecimal.ZERO);
    }

    private void initComponents() {
        roundL = new JLabel();
        pointsL = new JLabel();
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

    private void initView() {
        this.setLayout(new SpringLayout());
        this.add(roundL);
        this.add(pointsL);

        SpringUtilities.makeCompactGrid(this, 2, 1, 5, 5, 5, 5);
    }

    private JLabel roundL;
    private JLabel pointsL;
}
