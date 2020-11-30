package controller.listeners;

import controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileListener implements ActionListener {
    private Controller controller;

    public FileListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Новая игра":
                controller.createNewGame();
                break;
            case "Сохранить":
                controller.saveGame();
                break;
            case "Открыть":
                controller.loadGame();
                break;
        }
    }
}
