package controller.listeners;

import view.View;
import view.ColorTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorListener implements ActionListener {
    private View view;

    public ColorListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Коричневый":
                view.setColorTheme(ColorTheme.BROWN);
                view.updateView();
                break;
            case "Красный":
                view.setColorTheme(ColorTheme.RED);
                view.updateView();
                break;
            case "Синий":
                view.setColorTheme(ColorTheme.BLUE);
                view.updateView();
                break;
            case "Зеленый":
                view.setColorTheme(ColorTheme.GREEN);
                view.updateView();
                break;
            case "Золотой":
                view.setColorTheme(ColorTheme.GOLD);
                view.updateView();
                break;
        }
    }
}
