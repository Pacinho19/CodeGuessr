package pl.pacinho.codeguessr.view.components.tools;

import pl.pacinho.codeguessr.utils.TimeUtils;
import pl.pacinho.codeguessr.view.components.GameSummaryPanel;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer implements Runnable {

    private final GameSummaryPanel gameSummaryPanel;

    private final long DELAY = 1_000L;
    private Timer timer;
    private long seconds = 0L;

    public GameTimer(GameSummaryPanel gameSummaryPanel) {
        this.gameSummaryPanel = gameSummaryPanel;
        initTimer();
    }

    private void initTimer() {
        timer = new Timer("GameTime");
        gameSummaryPanel.setTimeText(TimeUtils.getTime(seconds));
    }

    @Override
    public void run() {
        timer.schedule(gameTimeTask(), DELAY, DELAY);
    }

    private TimerTask gameTimeTask() {
        return new TimerTask() {
            public void run() {
                seconds++;
                gameSummaryPanel.setTimeText(TimeUtils.getTime(seconds));
            }
        };
    }

    public void start() {
        run();
    }

    public void stop() {
        timer.cancel();
    }
}
