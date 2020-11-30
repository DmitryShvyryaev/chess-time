package view;

import controller.Controller;
import model.Figure;
import model.FigureColor;
import model.TypeOfFigure;

import javax.swing.*;
import java.awt.*;
import java.net.URLDecoder;
import java.util.HashMap;

public class ImageHelper {
    private static final HashMap<Style, HashMap<FigureColor, HashMap<TypeOfFigure, Image>>> imageContainer = new HashMap<>();
    private static final HashMap<Style, HashMap<FigureColor, HashMap<TypeOfFigure, ImageIcon>>> iconContainer = new HashMap<>();
    private static final String pathToRes = URLDecoder.decode(ClassLoader.getSystemResource("image").getFile().replaceFirst("/", "") + "/");

    static {
        for (Style s : Style.values()) {
            if (s.ordinal() == 0)
                continue;
            HashMap<FigureColor, HashMap<TypeOfFigure, Image>> imageContainer1 = new HashMap<>();
            HashMap<FigureColor, HashMap<TypeOfFigure, ImageIcon>> iconContainer1 = new HashMap<>();
            for (FigureColor figureColor : FigureColor.values()) {
                HashMap<TypeOfFigure, Image> imageContainer2 = new HashMap<>();
                HashMap<TypeOfFigure, ImageIcon> iconContainer2 = new HashMap<>();
                for (TypeOfFigure tof : TypeOfFigure.values()) {
                    if (tof == TypeOfFigure.EMPTY)
                        continue;
                    String filename = pathToRes + figureColor + "_" + tof + "_" + s + ".png";
                    ImageIcon ic = new ImageIcon(filename);
                    iconContainer2.put(tof, ic);
                    Image image = ic.getImage();
                    imageContainer2.put(tof, image);
                }
                imageContainer1.put(figureColor, imageContainer2);
                iconContainer1.put(figureColor, iconContainer2);
            }
            imageContainer.put(s, imageContainer1);
            iconContainer.put(s, iconContainer1);
        }
    }

    public static String getCharForFigure(TypeOfFigure type, FigureColor color, boolean toScore) {
        if (type == TypeOfFigure.EMPTY || color == null)
            return "";
        if (!toScore)
            color = FigureColor.BLACK;
        switch (color) {
            case BLACK:
                switch (type) {
                    case EMPTY:
                        return "";
                    case PAWN:
                        return "\u265F";
                    case BISHOP:
                        return "\u265D";
                    case KNIGHT:
                        return "\u265E";
                    case ROOK:
                        return "\u265C";
                    case KING:
                        return "\u265A";
                    case QWEEN:
                        return "\u265B";
                }
            case WHITE:
                switch (type) {
                    case EMPTY:
                        return "";
                    case PAWN:
                        return "\u2659";
                    case BISHOP:
                        return "\u2657";
                    case KNIGHT:
                        return "\u2658";
                    case ROOK:
                        return "\u2656";
                    case KING:
                        return "\u2654";
                    case QWEEN:
                        return "\u2655";
                }
        }
        return null;
    }

    public static Image getImage(Figure f, Style style) {
        return imageContainer.get(style).get(f.color).get(f.type);
    }

    public static ImageIcon getIcon(Figure f, Style style) {
        if (f.type == TypeOfFigure.EMPTY || f.color == null)
            return null;
        return iconContainer.get(style).get(f.color).get(f.type);
    }

    public enum Style {
        STYLE_1,
        STYLE_2,
        STYLE_3,
        STYLE_4,
        STYLE_5;

        public static String getTranslate(Style s) {
            return "Стиль " + (s.ordinal() + 1);
        }

        public static String[] getTranslateValues() {
            String[] values = new String[Style.values().length];
            for (int i = 0; i < values.length; i++) {
                values[i] = getTranslate(Style.values()[i]);
            }
            return values;
        }

        public static Style getStyleByTranslate(String text) {
            int number = Integer.parseInt(text.substring(text.length() - 1));
            return Style.valueOf("STYLE_" + number);
        }
    }
}
