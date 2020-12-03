package view;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum FigureStyle {
    STYLE_1("Стиль 1"),
    STYLE_2("Стиль 2"),
    STYLE_3("Стиль 3"),
    STYLE_4("Стиль 4"),
    STYLE_5("Стиль 5");

    private String translate;

    FigureStyle(String translate) {
        this.translate = translate;
    }

    public String getTranslate() {
        return translate;
    }

    public static FigureStyle getStyleByTranslate(String text) {
        int number = Integer.parseInt(text.substring(text.length() - 1));
        return FigureStyle.valueOf("STYLE_" + number);
    }

    public static String[] getThanslatedsValues() {
        String[] result = new String[FigureStyle.values().length];
        IntStream.range(0, 5).forEach(i -> result[i] = FigureStyle.values()[i].getTranslate());
        return result;
    }
}
