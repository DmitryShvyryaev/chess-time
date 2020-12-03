package view;

import java.awt.*;
import java.util.stream.IntStream;

public enum ColorTheme {
    BROWN("Коричневый", new Color(217, 190, 171), new Color(161, 108, 72)),
    RED("Красный", new Color(255, 203, 187), new Color(224, 90, 63)),
    BLUE("Синий", new Color(191, 209, 224), new Color(87, 136, 179)),
    GREEN("Зеленый", new Color(197, 204, 192), new Color(120, 134, 107)),
    GOLD("Золотой", new Color(255, 236, 194), new Color(255, 187, 41));

    private String translate;
    private Color lightColor;
    private Color darkColor;

    ColorTheme(String translate, Color lightColor, Color darkColor) {
        this.translate = translate;
        this.lightColor = lightColor;
        this.darkColor = darkColor;
    }

    public String getTranslate() {
        return translate;
    }

    public Color getLightColor() {
        return lightColor;
    }

    public Color getDarkColor() {
        return darkColor;
    }

    public static String[] getTranslatedValues() {
        String[] result = new String[ColorTheme.values().length];
        IntStream.range(0, 5).forEach(i -> result[i] = ColorTheme.values()[i].getTranslate());
        return result;
    }
}
