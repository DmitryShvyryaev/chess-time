package view;

import controller.Controller;
import model.Model;

import javax.swing.*;

import java.awt.*;

public class View extends JFrame {
    public static final int CELL_SIZE = 80;
    public static final int WIDTH = 8 * CELL_SIZE;
    private GameField gameField;
    private ScoreField scoreField;
    private ColorTheme colorTheme = ColorTheme.BROWN;
    private final Model model;
    private final Controller controller;
    private boolean markActiveFields = true;
    private boolean reverseView = false;
    private ImageHelper.Style figureStyle = ImageHelper.Style.STYLE_1;

    public View(final Model model, Controller controller)  {
        this.controller = controller;
        this.model = model;
        if (controller.getRes().containsKey("ColorTheme"))
            colorTheme = ColorTheme.valueOf(controller.getRes().getProperty("ColorTheme"));
        if (controller.getRes().containsKey("markActiveFields"))
            markActiveFields = Boolean.parseBoolean(controller.getRes().getProperty("markActiveFields"));
        if (controller.getRes().containsKey("reverseView"))
            reverseView = Boolean.parseBoolean(controller.getRes().getProperty("reverseView"));
        if (controller.getRes().containsKey("figureStyle"))
            figureStyle = ImageHelper.Style.valueOf(controller.getRes().getProperty("figureStyle"));
        initView();
        initMenuBar();
        initGameField();
        initScoreField();
        setVisible(true);
    }

    private void initView() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());
        addWindowListener(controller);
        setSize(WIDTH + 7, WIDTH + 50 + 90);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Chess");
    }

    public void updateView() {
        gameField.update();
        scoreField.repaint();
    }

    public void updateScore() {
        scoreField.repaint();
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public boolean isMarkActiveFields() {
        return markActiveFields;
    }

    public void setMarkActiveFields(boolean markActiveFields) {
        this.markActiveFields = markActiveFields;
    }

    public void setReverseView(boolean reverseView) {
        this.reverseView = reverseView;
    }

    public boolean isReverseView() {
        return reverseView;
    }

    public void setFigureStyle(ImageHelper.Style figureStyle) {
        this.figureStyle = figureStyle;
    }

    public ImageHelper.Style getFigureStyle() {
        return figureStyle;
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public int askQuestion(String message) {
        return JOptionPane.showConfirmDialog(null, message);
    }

    public String askString(String message) {
        return JOptionPane.showInputDialog(message);
    }

    public String askFigure(String message) {
        return (String) JOptionPane.showInputDialog(null, message, "Выбор фигуры", JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"Ферзь", "Ладья", "Слон", "Конь"}, "1");
    }

    public void doBeforeMoving(int x, int y, int dX, int dY) {
        gameField.doBeforeMoving(x, y, dX, dY);
    }

    public void doMoving(int x, int y) {
        gameField.doMoving(x, y);
    }

    public void doPostMoving() {
        gameField.doPostMoving();
    }

    private void initMenuBar() {
        JMenuBar bar = new JMenuBar();
        MenuHelper.initFileMenu(bar, controller);
        MenuHelper.initColorMenu(bar, this);
        MenuHelper.initTimeMenu(bar, controller);
        MenuHelper.initNameMenu(bar, controller);
        MenuHelper.initImageMenu(bar, this);
        MenuHelper.initSettingMenu(bar, this);

        this.add(bar, BorderLayout.NORTH);
    }

    private void initGameField() {
        gameField = new GameField(this, model);
        gameField.setPreferredSize(new Dimension(WIDTH, WIDTH));
        gameField.addMouseListener(controller);
        gameField.addMouseMotionListener(controller);

        this.add(gameField, BorderLayout.CENTER);
    }

    private void initScoreField() {
        scoreField = new ScoreField(model, controller);
        this.add(scoreField, BorderLayout.SOUTH);
    }
}
