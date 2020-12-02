package model;

import java.io.Serializable;
import java.util.*;

public class EatenFigures implements Serializable, Cloneable {
    private HashMap<TypeOfFigure, Integer> whiteFigures = new HashMap<>();
    private HashMap<TypeOfFigure, Integer> blackFigures = new HashMap<>();

    public EatenFigures() {
        whiteFigures.put(TypeOfFigure.QWEEN, 0);
        whiteFigures.put(TypeOfFigure.ROOK, 0);
        whiteFigures.put(TypeOfFigure.BISHOP, 0);
        whiteFigures.put(TypeOfFigure.KNIGHT, 0);
        whiteFigures.put(TypeOfFigure.PAWN, 0);

        blackFigures.put(TypeOfFigure.QWEEN, 0);
        blackFigures.put(TypeOfFigure.ROOK, 0);
        blackFigures.put(TypeOfFigure.BISHOP, 0);
        blackFigures.put(TypeOfFigure.KNIGHT, 0);
        blackFigures.put(TypeOfFigure.PAWN, 0);
    }

    private EatenFigures(Object whiteFigures, Object blackFigures) {
        this.whiteFigures = (HashMap<TypeOfFigure, Integer>) whiteFigures;
        this.blackFigures = (HashMap<TypeOfFigure, Integer>) blackFigures;
    }

    public void addFigure(Figure f) {
        Map<TypeOfFigure, Integer> needMap = f.color == FigureColor.WHITE ? whiteFigures : blackFigures;
        needMap.put(f.type, needMap.get(f.type) + 1);
    }

    public void clear() {
        for (Map.Entry<TypeOfFigure, Integer> pair : whiteFigures.entrySet()) {
            pair.setValue(0);
        }
        for (Map.Entry<TypeOfFigure, Integer> pair : blackFigures.entrySet()) {
            pair.setValue(0);
        }
    }

    public TreeMap<TypeOfFigure, Integer> getWhiteFigures() {
        return getSortedMap(whiteFigures);
    }

    public TreeMap<TypeOfFigure, Integer> getBlackFigures() {
        return getSortedMap(blackFigures);
    }

    private TreeMap<TypeOfFigure, Integer> getSortedMap(HashMap<TypeOfFigure, Integer> map) {
        TreeMap<TypeOfFigure, Integer> sorted = new TreeMap<>((o1, o2) -> Integer.compare(o2.ordinal(), o1.ordinal()));
        sorted.putAll(map);
        return sorted;
    }

    @Override
    protected Object clone() {
        return new EatenFigures(whiteFigures.clone(), blackFigures.clone());
    }

}
