package pl.pacinho.codeguessr.view.components.tools;

import pl.pacinho.codeguessr.view.controller.CodeGuessrViewController;

import java.util.Timer;
import java.util.TimerTask;

public class RoundTimer implements Runnable {

    private final CodeGuessrViewController codeGuessrViewController;
    private int timeLeft = 60;
    private final long DELAY = 1_000L;
    private Timer timer;

    public RoundTimer(CodeGuessrViewController codeGuessrViewController) {
        this.codeGuessrViewController = codeGuessrViewController;
        initTimer();
    }

    private void initTimer() {
        timer = new Timer("RoundTimer");
        codeGuessrViewController.getCodeGuessrView().getRoundTimer().setMaximum(timeLeft);
        timeLeft();
    }

    private void timeLeft() {
        codeGuessrViewController.getCodeGuessrView().getRoundTimer().setValue(timeLeft);
        codeGuessrViewController.getCodeGuessrView()
                .getRoundTimer()
                .setString(timeLeft + " seconds");
    }

    @Override
    public void run() {
        timer.schedule(roundTimeTask(), DELAY, DELAY);
    }

    private TimerTask roundTimeTask() {
        return new TimerTask() {
            public void run() {
                timeLeft--;
                timeLeft();
                if (timeLeft == 0) end();
            }
        };
    }

    private void end() {
        stop();
        codeGuessrViewController.guess();
    }

    public void start() {
        run();
    }

    public void stop() {
        timer.cancel();
    }
}
