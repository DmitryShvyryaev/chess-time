package model;

import java.io.Serializable;
import java.util.Objects;

public class Figure implements Serializable {
    public TypeOfFigure type;
    public int x;
    public int y;
    public int[] weaknessAfterPass;
    public FigureColor color;
    public boolean isMoved = false;

    public Figure(TypeOfFigure type, int x, int y, FigureColor color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
        weaknessAfterPass = null;
    }

    public Figure(TypeOfFigure type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        weaknessAfterPass = null;
    }

    public Figure(TypeOfFigure type, int x, int y, FigureColor color, boolean isMoved, int[] weaknessAfterPass) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
        this.isMoved = isMoved;
        this.weaknessAfterPass = weaknessAfterPass;
    }

    public boolean move(int x, int y, boolean isFake) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            return false;
        if (!isFake && weaknessAfterPass != null)
            weaknessAfterPass = null;
        if (!isFake && (y - this.y == 2 || y - this.y == -2))
            weaknessAfterPass = new int[]{this.x, (color == FigureColor.WHITE ? this.y - 1 : this.y + 1)};
        this.x = x;
        this.y = y;
        isMoved = true;
        return true;
    }

    public Figure getClone() {
        return new Figure(type, x, y, color, isMoved, weaknessAfterPass);
    }

    public boolean isEmpty() {
        return type == TypeOfFigure.EMPTY;
    }

    @Override
    public String toString() {
        return "Figure{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return x == figure.x &&
                y == figure.y &&
                isMoved == figure.isMoved &&
                type == figure.type &&
                color == figure.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, x, y, color, isMoved);
    }
}
