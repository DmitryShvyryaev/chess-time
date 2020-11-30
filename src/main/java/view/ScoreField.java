package view;

import controller.Controller;
import model.FigureColor;
import model.Model;
import model.TypeOfFigure;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ScoreField extends JPanel {
    private Model model;
    private Controller controller;
    private JLabel whitePlayer;
    private JLabel blackPlayer;
    private JLabel whiteTimer;
    private JLabel blackTimer;

    public ScoreField(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;

        setPreferredSize(new Dimension(View.WIDTH, 90));
        setLayout(new BorderLayout());
        addButtons();
        initLabels();
        resetNames();
        resetTimers();
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintEatenFigures(g);
        resetTimers();
        resetNames();
        paintFrame(g);
    }

    private void paintFrame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.WHITE);
        g2.drawRect(1, 1, View.WIDTH - 1, 90 - 1);
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(2, 2, View.WIDTH - 4, 90 - 5);
    }

    private void paintEatenFigures(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("1", Font.ROMAN_BASELINE, 30));
        int indent = 8;
        for (Map.Entry<TypeOfFigure, Integer> pair : model.getEatenFigures().getBlackFigures().entrySet()) {
            if (pair.getValue() != 0) {
                for (int i = 0; i < pair.getValue(); i++) {
                    g.drawString(ImageHelper.getCharForFigure(pair.getKey(), FigureColor.BLACK, true), indent, 75);
                    indent += 13;
                }
                indent += 8;
            }
        }
        indent = View.WIDTH - 40;
        for (Map.Entry<TypeOfFigure, Integer> pair : model.getEatenFigures().getWhiteFigures().entrySet()) {
            if (pair.getValue() != 0) {
                for (int i = 0; i < pair.getValue(); i++) {
                    g.drawString(ImageHelper.getCharForFigure(pair.getKey(), FigureColor.WHITE, true), indent, 75);
                    indent -= 13;
                }
                indent -= 8;
            }
        }
    }

    private void addButtons() {
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout());
        add(container, BorderLayout.SOUTH);

        MenuHelper.addButton(container, "\u2B8C", controller);
        MenuHelper.addButton(container, "\u2690", controller);
        MenuHelper.addButton(container, "\u2696", controller);
    }

    private void initLabels() {
        Font font = new Font("1", Font.ROMAN_BASELINE, 20);
        whitePlayer = new JLabel();
        blackPlayer = new JLabel();
        whitePlayer.setFont(font);
        whitePlayer.setPreferredSize(new Dimension(220, 40));//delete
        blackPlayer.setPreferredSize(new Dimension(220, 40));//delete
        blackPlayer.setHorizontalAlignment(SwingConstants.RIGHT);
        blackPlayer.setFont(font);
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());
        left.add(whitePlayer, BorderLayout.NORTH);
        right.add(blackPlayer, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
        //adding timer
        whiteTimer = new JLabel();
        blackTimer = new JLabel();
        whiteTimer.setFont(font);
        blackTimer.setFont(font);
        whiteTimer.setAlignmentY(TOP_ALIGNMENT);
        blackTimer.setAlignmentY(TOP_ALIGNMENT);
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout());
        top.add(whiteTimer);
        top.add(blackTimer);
        add(top, BorderLayout.CENTER);
    }

    public void resetNames() {
        whitePlayer.setText("\n  " + controller.getWhitePlayer() + " " +
                ImageHelper.getCharForFigure(TypeOfFigure.KING, FigureColor.WHITE, true));
        blackPlayer.setText(ImageHelper.getCharForFigure(TypeOfFigure.KING, FigureColor.BLACK, true) +
                " " + controller.getBlackPlayer() + "  ");
    }

    public void resetTimers() {
        if (controller.isTimeGame()) {
            whiteTimer.setText(controller.getTime(FigureColor.WHITE));
            blackTimer.setText(controller.getTime(FigureColor.BLACK));
        } else {
            whiteTimer.setText("");
            blackTimer.setText("");
        }
    }
}
