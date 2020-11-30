package view;

import java.awt.*;

public enum ColorTheme {
    BROWN,
    RED,
    BLUE,
    GREEN,
    GOLD;

    public static Color getDarkColor(ColorTheme theme) {
        switch (theme) {
            case BROWN:
                return new Color(161, 108, 72);
            case RED:
                return new Color(224, 90, 63);
            case BLUE:
                return new Color(87, 136, 179);
            case GREEN:
                return new Color(120, 134, 107);
            case GOLD:
                return new Color(255, 187, 41);
        }
        return new Color(128, 76, 45);
    }

    public static Color getLightColor(ColorTheme theme) {
        switch (theme) {
            case BROWN:
                return new Color(217, 190, 171);
            case RED:
                return new Color(255, 203, 187);
            case BLUE:
                return new Color(191, 209, 224);
            case GREEN:
                return new Color(197, 204, 192);
            case GOLD:
                return new Color(255, 236, 194);
        }
        return new Color(128, 76, 45);
    }

    public static String getTranslate(ColorTheme theme) {
        switch (theme) {
            case BROWN:
                return "Коричневый";
            case RED:
                return "Красный";
            case BLUE:
                return "Синий";
            case GREEN:
                return "Зеленый";
            case GOLD:
                return "Золотой";
        }
        return null;
    }
}
