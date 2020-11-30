package controller;

import model.FigureColor;
import model.TypeOfFigure;

public interface ChessListener {
    void showWinnerIfMate(FigureColor winner);
    TypeOfFigure askTypeOfFigure();
}
