package controller.listeners;

import view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingListener implements ActionListener {
    private View view;

    public SettingListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Подсветка полей":
                view.setMarkActiveFields(!view.isMarkActiveFields());
                view.updateView();
                break;
            case "Поворот доски":
                view.setReverseView(!view.isReverseView());
                view.updateView();
                break;
        }
    }
}
