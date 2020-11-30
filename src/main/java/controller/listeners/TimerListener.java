package controller.listeners;

import controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerListener implements ActionListener {
    private Controller controller;

    public TimerListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "нет":
                controller.setTimeCount(0);
                break;
            case "3 мин":
                controller.setTimeCount(3);
                break;
            case "5 мин":
                controller.setTimeCount(5);
                break;
            case "10 мин":
                controller.setTimeCount(10);
                break;
            case "15 мин":
                controller.setTimeCount(15);
                break;
            case "20 мин":
                controller.setTimeCount(20);
                break;
            case "30 мин":
                controller.setTimeCount(30);
                break;
        }
    }
}
