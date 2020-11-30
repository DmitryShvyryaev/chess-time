package view;

import controller.Controller;
import controller.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuHelper {
    public static void initFileMenu(JMenuBar parent,  Controller controller) {
        JMenu fileMenu = new JMenu("Файл");
        parent.add(fileMenu);
        FileListener fileListener = new FileListener(controller);
        addMenuItem(fileMenu, "Новая игра", fileListener);
        addMenuItem(fileMenu, "Открыть", fileListener);
        addMenuItem(fileMenu, "Сохранить", fileListener);
    }

    public static void initColorMenu(JMenuBar parent, View view) {
        JMenu colorMenu = new JMenu("Цвет");
        ColorListener colorListener = new ColorListener(view);
        ButtonGroup bg = addJCheckBoxGroup(colorMenu, colorListener ,
                ColorTheme.getTranslate(view.getColorTheme()),
                "Коричневый", "Красный", "Синий", "Зеленый", "Золотой");
        parent.add(colorMenu);
    }

    public static void initTimeMenu(JMenuBar parent, Controller controller) {
        JMenu timeMenu = new JMenu("Таймер");
        TimerListener timerListener = new TimerListener(controller);
        addJCheckBoxGroup(timeMenu, timerListener, "нет",
                "нет", "3 мин", "5 мин", "10 мин", "15 мин", "20 мин", "30 мин");
        parent.add(timeMenu);
    }

    public static void initNameMenu(JMenuBar parent, Controller controller) {
        JMenu nameMenu = new JMenu("Имена");
        PlayerNameListener nameListener = new PlayerNameListener(controller);
        addMenuItem(nameMenu, "Игрок 1", nameListener);
        addMenuItem(nameMenu, "Игрок 2", nameListener);
        addMenuItem(nameMenu, "Поменять местами", nameListener);
        parent.add(nameMenu);
    }

    public static void initSettingMenu(JMenuBar parent, View view) {
        JMenu settingMenu = new JMenu("Настройки");
        SettingListener settingListener = new SettingListener(view);
        JCheckBoxMenuItem item1 = addCheckBox(settingMenu, "Подсветка полей", settingListener);
        item1.setState(view.isMarkActiveFields());
        JCheckBoxMenuItem item2 = addCheckBox(settingMenu, "Поворот доски", settingListener);
        item2.setState(view.isReverseView());
        parent.add(settingMenu);
    }

    public static void initImageMenu(JMenuBar parent, View view) {
        JMenu imageMenu = new JMenu("Иконки");
        ImageListener listener = new ImageListener(view);
        addJCheckBoxGroup(imageMenu, listener, ImageHelper.Style.getTranslate(view.getFigureStyle()),
                ImageHelper.Style.getTranslateValues());
        parent.add(imageMenu);
    }

    public static JMenuItem addMenuItem(JMenu parent, String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        parent.add(menuItem);
        return menuItem;
    }

    public static JCheckBoxMenuItem addCheckBox(JMenu parent, String text, ActionListener actionListener) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(text);
        item.addActionListener(actionListener);
        parent.add(item);
        return item;
    }

    public static ButtonGroup addJCheckBoxGroup(JMenu parent, ActionListener listener,
                                                String activated, String... strings) {
        ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < strings.length; i++) {
            JCheckBoxMenuItem item = addCheckBox(parent, strings[i], listener);
            if (strings[i].equals(activated))
                item.setState(true);
            bg.add(item);
        }
        return bg;
    }

    public static void addButton(JPanel parent, String name, ActionListener listener) {
        JButton button = new JButton(name);
        button.setFont(new Font("1", Font.ROMAN_BASELINE, 25));
        button.addActionListener(listener);
        button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        parent.add(button);
    }
}
