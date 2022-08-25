package pl.pacinho.codeguessr.model;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class GameSummaryDto {

    private BigDecimal points;
    private int round;
    private long startTime;
    private long gameDuration;

    public GameSummaryDto() {
        points = BigDecimal.ZERO;
        round = 1;
        startTime = System.currentTimeMillis();
    }

    public void nextRound() {
        round++;
    }

    public void addPoints(BigDecimal points) {
        this.points = this.points.add(points);
    }

    public void finishGame() {
        gameDuration = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);
    }

    public BigDecimal getPoints() {
        return points;
    }

    public int getRound() {
        return round;
    }

    public long getGameDuration() {
        return gameDuration;
    }
}
