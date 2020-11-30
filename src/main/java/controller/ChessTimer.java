package controller;

import view.View;
import model.FigureColor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChessTimer implements Runnable {
    private int time = 0;
    private LocalTime whiteTime = LocalTime.of(0 ,0 , 0);
    private LocalTime blackTime = LocalTime.of(0 ,0 , 0);
    private FigureColor active = FigureColor.WHITE;
    private Controller controller;
    private View view;
    private boolean isStopped = false;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
    private Thread currentThread;

    public ChessTimer(Controller controller) {
        this.controller = controller;
        isStopped = false;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setTime(int minute) {
        time = minute;
        whiteTime.plusMinutes(time);
        blackTime.plusMinutes(time);
        active = FigureColor.WHITE;
        isStopped = false;
    }

    public void reset() {
        whiteTime = LocalTime.of(0, time, 0);
        blackTime = LocalTime.of(0, time, 0);
        active = FigureColor.WHITE;
        isStopped = false;
    }

    public void startTimer() {
        currentThread = new Thread(this);
        currentThread.setDaemon(true);
        currentThread.start();
    }

    public String getTime(FigureColor color) {
        if (color == FigureColor.WHITE)
            return whiteTime.format(dtf);
        else
            return blackTime.format(dtf);
    }

    public void changeActiveTimer() {
        active = active == FigureColor.BLACK ? FigureColor.WHITE : FigureColor.BLACK;
    }

    public void stop() {
        isStopped = true;
        if (currentThread != null) {
            try {
                currentThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (!isStopped) {
            if (active == FigureColor.WHITE)
                whiteTime = whiteTime.minusSeconds(1);
            else
                blackTime = blackTime.minusSeconds(1);

            LocalTime activeTimer = active == FigureColor.WHITE ? whiteTime : blackTime;
            view.updateScore();
            if (activeTimer.getSecond() == 0 && activeTimer.getMinute() == 0) {
                controller.stopGameIfTime(activeTimer == whiteTime ? FigureColor.BLACK : FigureColor.WHITE);
                stop();
                reset();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
