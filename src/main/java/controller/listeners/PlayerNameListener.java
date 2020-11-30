package controller.listeners;

import controller.Controller;
import model.FigureColor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNameListener implements ActionListener {
    private Controller controller;

    public PlayerNameListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Игрок 1":
                controller.setPlayerName(FigureColor.WHITE);
                break;
            case "Игрок 2":
                controller.setPlayerName(FigureColor.BLACK);
                break;
            case "Поменять местами":
                controller.swapNames();
                break;
        }
    }
}
