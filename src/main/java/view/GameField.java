package view;

import model.Figure;
import model.Model;
import model.TypeOfFigure;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static model.FigureColor.*;

public class GameField extends JLayeredPane {
    private final View view;
    protected final Model model;
    private JPanel backingPanel = new JPanel();
    private JPanel[][] panels = new JPanel[8][8];
    private JLabel[][] labels = new JLabel[8][8];
    private static final Font FONT = new Font("Font", Font.ROMAN_BASELINE, 64);

    private JLabel movingLabel;
    private int deltaX;
    private int deltaY;

    public GameField(final View view, final Model model) {
        this.view = view;
        this.model = model;
        backingPanel.setLayout(new GridLayout(8, 8));
        backingPanel.setSize(View.CELL_SIZE * 8, View.CELL_SIZE * 8);
        backingPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE));
        setFocusable(true);

        init();
        add(backingPanel, JLayeredPane.DEFAULT_LAYER);
        movingLabel = new JLabel();
        movingLabel.setFont(FONT);
        movingLabel.setSize(new Dimension(80, 80));
    }

    public void update() {
        boolean reverse = view.isReverseView() && model.getTurn() == BLACK;
        paintCells();
        paintActiveFigure(reverse);
        if (view.isMarkActiveFields())
            paintCellsToMove(reverse);
        paintCheckPlayer(reverse);
        paintFigures(reverse);
        repaint();
    }

    private void init() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                panels[i][j] = new JPanel(new GridBagLayout());
                panels[i][j].setBackground(getCellColor(j, i));
                labels[i][j] = new JLabel();
                labels[i][j].setFont(FONT);
                labels[i][j].setPreferredSize(new Dimension(View.CELL_SIZE, View.CELL_SIZE));
                if (model.getAllFigure()[i][j].color == WHITE)
                    labels[i][j].setForeground(Color.WHITE);
                if (view.getFigureStyle() == FigureStyle.STYLE_1)
                    labels[i][j].setText(ImageHelper.getCharForFigure(model.getAllFigure()[i][j].type,
                            model.getAllFigure()[i][j].color, false));
                else if (model.getAllFigure()[i][j].type != TypeOfFigure.EMPTY) {
                    labels[i][j].setIcon(ImageHelper.getIcon(model.getAllFigure()[i][j], view.getFigureStyle()));
                }
                panels[i][j].add(labels[i][j]);
                backingPanel.add(panels[i][j]);
            }
        }
    }

    private void paintCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                panels[i][j].setBorder(null);
                panels[i][j].setBackground(getCellColor(j, i));
            }
        }
    }

    public Color getCellColor(int x, int y) {
        Color c = (x + y) % 2 == 0 ? view.getColorTheme().getLightColor() : view.getColorTheme().getDarkColor();
        return c;
    }

    private int getReverseCoord(int coord) {
        return 7 - coord;
    }

    private void paintFigures(boolean reverse) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int x = reverse ? getReverseCoord(j) : j;
                int y = reverse ? getReverseCoord(i) : i;
                if (model.getAllFigure()[i][j].type == TypeOfFigure.EMPTY) {
                    labels[y][x].setIcon(null);
                    labels[y][x].setText("");
                } else if (view.getFigureStyle() == FigureStyle.STYLE_1) {
                    labels[y][x].setForeground(model.getAllFigure()[i][j].color == WHITE ? Color.WHITE : Color.BLACK);
                    labels[y][x].setIcon(null);
                    labels[y][x].setText(ImageHelper.getCharForFigure(model.getAllFigure()[i][j].type,
                            model.getAllFigure()[i][j].color, false));
                } else {
                    labels[y][x].setText("");
                    labels[y][x].setIcon(ImageHelper.getIcon(model.getAllFigure()[i][j], view.getFigureStyle()));
                    labels[y][x].revalidate();
                }
            }
        }
    }

    private void paintActiveFigure(boolean reverse) {
        if (model.isFigureChanged()) {
            int x = reverse ? getReverseCoord(model.getActiveFigure().x) : model.getActiveFigure().x;
            int y = reverse ? getReverseCoord(model.getActiveFigure().y) : model.getActiveFigure().y;
            panels[y][x].setBorder(BorderFactory.createEtchedBorder(2, Color.WHITE, null));
        }
    }

    private void paintCellsToMove(boolean reverse) {
        if (model.isFigureChanged()) {
            for (Figure f : model.getCellsToMove()) {
                int x = reverse ? getReverseCoord(f.x) : f.x;
                int y = reverse ? getReverseCoord(f.y) : f.y;
                panels[y][x].setBorder(BorderFactory.createDashedBorder(null, 1, 3, 3, true));
            }
        }
    }

    private void paintCheckPlayer(boolean reverse) {
        if (model.getCheckFigure() != null) {
            int x = reverse ? getReverseCoord(model.getCheckFigure().x) : model.getCheckFigure().x;
            int y = reverse ? getReverseCoord(model.getCheckFigure().y) : model.getCheckFigure().y;
            panels[y][x].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.RED));
        }
    }

    public void doBeforeMoving(int x, int y, int eX, int eY) {
        deltaX = -(eX % 80) + 10;
        deltaY = -(eY % 80);
        movingLabel.setLocation(eX + deltaX, eY + deltaY);
        movingLabel.setForeground(model.getActiveFigure().color == WHITE ? Color.WHITE : Color.BLACK);
        movingLabel.setText(labels[y][x].getText());
        movingLabel.setIcon(labels[y][x].getIcon());
        movingLabel.revalidate();
        labels[y][x].setIcon(null);
        labels[y][x].setText(null);
        add(movingLabel, JLayeredPane.DRAG_LAYER);
        repaint();
    }

    public void doMoving(int x, int y) {
        if (movingLabel != null) {
            movingLabel.setLocation(x + deltaX, y + deltaY);
            repaint();
        }
    }

    public void doPostMoving() {
        remove(movingLabel);
        deltaX = 0;
        deltaY = 0;
        revalidate();
        update();
        repaint();
    }
}
