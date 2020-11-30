package controller.listeners;

import view.View;
import view.ImageHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageListener implements ActionListener {
    private View view;

    public ImageListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        view.setFigureStyle(ImageHelper.Style.getStyleByTranslate(command));
        view.updateView();
    }
}
