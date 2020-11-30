package model;

import controller.ChessListener;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Model {
    private Figure[][] gameField = new Figure[8][8];
    private Figure checkFigure;
    private Figure activeFigure;
    private boolean isFigureChanged;
    private boolean isGameStopped = true;
    private FigureColor turn;
    private FigureColor winner;
    private List<Figure> cellsOfActiveFigureAvailableToMove = new ArrayList<Figure>();
    private EatenFigures eatenFigures = new EatenFigures();
    private Stack previosGameField;
    private Stack previosTurn;
    private Stack previousEatenFigures;
    private ChessListener chessListener;

    public Model() {
        createGame();
    }

    public boolean createGame() {
        initGameField();
        cellsOfActiveFigureAvailableToMove.clear();
        eatenFigures.clear();
        activeFigure = null;
        isFigureChanged = false;
        turn = FigureColor.WHITE;
        isGameStopped = false;
        winner = null;
        previosGameField = new Stack();
        previosTurn = new Stack();
        previousEatenFigures = new Stack();
        checkFigure = null;
        return true;
    }

    public boolean setActiveFigure(int x, int y) {
        if (!checkTheBorders(x, y) || gameField[y][x].color != turn || gameField[y][x].isEmpty()) {
            return false;
        }
        activeFigure = gameField[y][x];
        isFigureChanged = true;
        cellsOfActiveFigureAvailableToMove.addAll(removeCellsIfCheck(activeFigure.x, activeFigure.y,
                getCellsAvaileableToMove(activeFigure)));
        //checkCastling if King
        if (activeFigure.type == TypeOfFigure.KING &&
                !activeFigure.equals(checkFigure) && !activeFigure.isMoved) {
            cellsOfActiveFigureAvailableToMove.addAll(checkCastling(activeFigure));
        }
        return true;
    }

    public Figure[][] getAllFigure() {
        return gameField;
    }

    public boolean moveTheFigure(int x, int y, List<Figure> cells) {
        boolean hasMove = false;
        if (checkTheBorders(x, y) && isFigureChanged && !cells.isEmpty()) {
            for (Figure f : cells) {
                if (f.x == x && f.y == y) {
                    saveState();
                    if (!specifyMove(x, y)) {
                        standartMove(x, y);
                    }
                    checkFigure = getCheckKing();
                    changeThePlayerToMove();
                    removeAllWeakness(turn);
                    hasMove = true;
                    int checkMate = isMate();
                    if (checkMate == 1 || checkMate == 2) {
                        isGameStopped = true;
                        if (checkMate == 2)
                            winner = turn == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE;
                        chessListener.showWinnerIfMate(winner);
                    }
                    break;
                }
            }
        }
        activeFigure = null;
        isFigureChanged = false;
        cellsOfActiveFigureAvailableToMove.clear();
        return hasMove;
    }

    private void standartMove(int x, int y) {
        if (!gameField[y][x].isEmpty()) {
            eatenFigures.addFigure(gameField[y][x]);
        }
        gameField[y][x] = activeFigure;
        gameField[activeFigure.y][activeFigure.x] = new Figure(TypeOfFigure.EMPTY, activeFigure.x,
                activeFigure.y);
        activeFigure.move(x, y, false);
    }

    private boolean specifyMove(int x, int y) {
        if (activeFigure.type == TypeOfFigure.KING && Math.abs(activeFigure.x - x) == 2) {
            if (x < activeFigure.x) {
                gameField[activeFigure.y][2] = activeFigure;
                gameField[activeFigure.y][4] = new Figure(TypeOfFigure.EMPTY, 4, activeFigure.y);
                activeFigure.move(2, activeFigure.y, false);
                gameField[activeFigure.y][3] = gameField[activeFigure.y][0];
                gameField[activeFigure.y][0] = new Figure(TypeOfFigure.EMPTY, 4, activeFigure.y);
                gameField[activeFigure.y][3].move(3, activeFigure.y, false);
            } else {
                gameField[activeFigure.y][6] = activeFigure;
                gameField[activeFigure.y][4] = new Figure(TypeOfFigure.EMPTY, 4, activeFigure.y);
                activeFigure.move(6, activeFigure.y, false);
                gameField[activeFigure.y][5] = gameField[activeFigure.y][7];
                gameField[activeFigure.y][7] = new Figure(TypeOfFigure.EMPTY, 7, activeFigure.y);
                gameField[activeFigure.y][5].move(5, activeFigure.y, false);
            }
            return true;
        }
        if (activeFigure.type == TypeOfFigure.PAWN) {
            if ((activeFigure.color == FigureColor.WHITE && y == 0) ||
                    (activeFigure.color == FigureColor.BLACK && y == 7)) {
                gameField[y][x] = new Figure(chessListener.askTypeOfFigure(),
                        x, y, activeFigure.color);
                gameField[activeFigure.y][activeFigure.x] = new Figure(TypeOfFigure.EMPTY, activeFigure.x, activeFigure.y);
                return true;
            }
            if (((x - activeFigure.x == 1) || (x - activeFigure.x == -1)) && gameField[y][x].type == TypeOfFigure.EMPTY) {
                gameField[y][x] = activeFigure;
                eatenFigures.addFigure(gameField[activeFigure.y][x]);
                gameField[activeFigure.y][x] = new Figure(TypeOfFigure.EMPTY, x, activeFigure.y);
                gameField[activeFigure.y][activeFigure.x] = new Figure(TypeOfFigure.EMPTY,
                        activeFigure.x, activeFigure.y);
                activeFigure.move(x, y, false);
                return true;
            }
        }
        return false;
    }

    private void changeThePlayerToMove() {
        turn = turn == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE;
    }

    private boolean checkTheBorders(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean isFigureChanged() {
        return isFigureChanged;
    }

    public boolean isGameStopped() {
        return isGameStopped;
    }

    public void setGameStopped(boolean gameStopped) {
        isGameStopped = gameStopped;
    }

    public Figure getActiveFigure() {
        return activeFigure;
    }

    public Figure getCheckFigure() {
        return checkFigure;
    }

    public EatenFigures getEatenFigures() {
        return eatenFigures;
    }

    public List<Figure> getCellsToMove() {
        return cellsOfActiveFigureAvailableToMove;
    }

    public void setWinner(FigureColor winner) {
        this.winner = winner;
    }

    public FigureColor getTurn() {
        return turn;
    }

    public FigureColor getWinner() {
        return winner;
    }

    public void setChessListener(ChessListener chessListener) {
        this.chessListener = chessListener;
    }

    private Figure getKingByColor(FigureColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameField[i][j].type == TypeOfFigure.KING && gameField[i][j].color == color)
                    return gameField[i][j];
            }
        }
        return null;
    }

    private Figure[][] getGameFieldCopy() {
        Figure[][] copy = new Figure[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = gameField[i][j].getClone();
            }
        }
        return copy;
    }

    private void saveState() {
        previosGameField.push(getGameFieldCopy());
        previosTurn.push(turn);
        try {
            previousEatenFigures.push(eatenFigures.clone());
        } catch (CloneNotSupportedException e) {
            //ignored
        }
    }

    public void rollBack(boolean setNull) {
        if (!previosTurn.isEmpty() && !previosGameField.isEmpty()) {
            gameField = (Figure[][]) previosGameField.pop();
            turn = (FigureColor) previosTurn.pop();
            eatenFigures = (EatenFigures) previousEatenFigures.pop();
            cellsOfActiveFigureAvailableToMove.clear();
            if (setNull) {
                activeFigure = null;
                isFigureChanged = false;
                getCheckKing();
            }
        }
    }

    private List<Figure> getCellsAvaileableToMove(Figure f) {
        List<Figure> result = new ArrayList<Figure>();
        switch (f.type) {
            case PAWN:
                Figure checked = checkWeaknessAfterPass(f, f.x - 1, f.y);
                if (checked != null)
                    result.add(checked);
                else {
                    checked = checkWeaknessAfterPass(f, f.x + 1, f.y);
                    if (checked != null)
                        result.add(checked);
                }
                switch (f.color) {
                    case WHITE:
                        result.addAll(checkCells(f, new int[][]{{0, -1}}, false, false, true));
                        if (!result.isEmpty() && checkTheBorders(f.x, f.y - 2) && !f.isMoved && gameField[f.y - 2][f.x].isEmpty())
                            result.add(gameField[f.y - 2][f.x]);
                        result.addAll(checkCells(f, new int[][]{{-1, -1}}, false, true, false));
                        result.addAll(checkCells(f, new int[][]{{1, -1}}, false, true, false));
                        break;
                    case BLACK:
                        result.addAll(checkCells(f, new int[][]{{0, 1}}, false, false, true));
                        if (!result.isEmpty() && checkTheBorders(f.x, f.y + 2) && !f.isMoved && gameField[f.y + 2][f.x].isEmpty())
                            result.add(gameField[f.y + 2][f.x]);
                        result.addAll(checkCells(f, new int[][]{{-1, 1}}, false, true, false));
                        result.addAll(checkCells(f, new int[][]{{1, 1}}, false, true, false));
                        break;
                }
                break;
            case KNIGHT:
                result.addAll(checkCells(f, new int[][]{{1, -2}, {-1, -2}, {2, -1}, {-2, -1},
                        {2, 1}, {-2, 1}, {1, 2}, {-1, 2}}, false, true, true));
                break;
            case BISHOP:
                result.addAll(checkCells(f, new int[][]{{1, 1}, {-1, 1}, {1, -1}, {-1, -1}},
                        true, true, true));
                break;
            case ROOK:
                result.addAll(checkCells(f, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}},
                        true, true, true));
                break;
            case QWEEN:
                result.addAll(checkCells(f, new int[][]{{1, -1}, {1, 0}, {1, 1}, {0, -1},
                        {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}}, true, true, true));
                break;
            case KING:
                result.addAll(checkCells(f, new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 1},
                        {-1, 0}, {-1, -1}, {0, -1}, {1, -1}}, false, true, true));
                break;
        }
        return result;
    }

    private List<Figure> checkCastling(Figure f) {
        List<Figure> result = new ArrayList<Figure>();
        int[][] attackedFields = getFieldsAttackedByColor(f.color == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE);
        if (!gameField[f.y][0].isEmpty() &&
                gameField[f.y][0].type == TypeOfFigure.ROOK &&
                !gameField[f.y][0].isMoved &&
                gameField[f.y][1].isEmpty() &&
                attackedFields[f.y][1] != 1 &&
                gameField[f.y][2].isEmpty() &&
                attackedFields[f.y][2] != 1 &&
                gameField[f.y][3].isEmpty() &&
                attackedFields[f.y][3] != 1)
            result.add(gameField[f.y][f.x - 2]);
        if (!gameField[f.y][7].isEmpty() &&
                gameField[f.y][7].type == TypeOfFigure.ROOK &&
                !gameField[f.y][7].isMoved &&
                gameField[f.y][5].isEmpty() &&
                attackedFields[f.y][5] != 1 &&
                gameField[f.y][6].isEmpty() &&
                attackedFields[f.y][6] != 1) {
            result.add(gameField[f.y][f.x + 2]);
        }
        return result;
    }

    private Figure checkWeaknessAfterPass(Figure f, int x, int y) {
        if (!checkTheBorders(x, y))
            return null;
        Figure checked = gameField[y][x];
        if (checked.type == TypeOfFigure.PAWN && checked.color != f.color && checked.weaknessAfterPass != null)
            return gameField[checked.weaknessAfterPass[1]][checked.weaknessAfterPass[0]];
        return null;
    }

    private void removeAllWeakness(FigureColor color) {
        for (Figure f : getAllFigureByColor(color)) {
            if (f.type == TypeOfFigure.PAWN)
                f.weaknessAfterPass = null;
        }
    }

    private List<Figure> checkCells(Figure f, int[][] dir, boolean needIter, boolean eat, boolean move) {
        List<Figure> result = new ArrayList<Figure>();
        for (int i = 0; i < dir.length; i++) {
            int coordX = f.x;
            int coordY = f.y;
            boolean next = needIter;
            do {
                coordX += dir[i][0];
                coordY += dir[i][1];
                if (!checkTheBorders(coordX, coordY)) {
                    break;
                }
                if (gameField[coordY][coordX].isEmpty() && move)
                    result.add(gameField[coordY][coordX]);
                else {
                    next = false;
                    if (!gameField[coordY][coordX].isEmpty() && gameField[coordY][coordX].color != f.color && eat)
                        result.add(gameField[coordY][coordX]);
                }
            } while (next);
        }
        return result;
    }

    private int[][] getFieldsAttackedByColor(FigureColor color) {
        int[][] result = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result[i][j] = 0;
            }
        }
        for (Figure f : getAllFigureByColor(color)) {
            for (Figure cell : getCellsAvaileableToMove(f)) {
                result[cell.y][cell.x] = 1;
            }
        }
        return result;
    }

    private List<Figure> removeCellsIfCheck(int x, int y, List<Figure> cells) {
        List<Figure> result = new ArrayList<Figure>();
        for (Figure cell : cells) {
            saveState();
            Figure active = gameField[y][x].getClone();
            gameField[y][x] = active;
            gameField[cell.y][cell.x] = active;
            gameField[y][x] = new Figure(TypeOfFigure.EMPTY, x, y);
            active.move(cell.x, cell.y, true);
            Figure myKing = getKingByColor(active.color);
            if (getFieldsAttackedByColor(active.color == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE)[myKing.y][myKing.x] != 1)
                result.add(cell);
            rollBack(false);
        }
        return result;
    }

    private Figure getCheckKing() {
        Figure whiteKing = getKingByColor(FigureColor.WHITE);
        if (getFieldsAttackedByColor(FigureColor.BLACK)[whiteKing.y][whiteKing.x] == 1)
            return whiteKing;

        Figure blackKing = getKingByColor(FigureColor.BLACK);
        if (getFieldsAttackedByColor(FigureColor.WHITE)[blackKing.y][blackKing.x] == 1)
            return blackKing;

        return null;
    }

    //2 - Mate
    //1 - Pat
    private int isMate() {
        List<Figure> availableFields = new ArrayList<Figure>();
        for (Figure f : getAllFigureByColor(turn)) {
            availableFields.addAll(removeCellsIfCheck(f.x, f.y, getCellsAvaileableToMove(f)));
        }
        if (checkFigure != null && checkFigure.color == turn && availableFields.isEmpty())
            return 2;
        else if (availableFields.isEmpty())
            return 1;
        else
            return 0;
    }

    private List<Figure> getAllFigureByColor(FigureColor color) {
        List<Figure> result = new ArrayList<Figure>();
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                if (gameField[i][j].color == color)
                    result.add(gameField[i][j]);
            }
        }
        return result;
    }

    private void initGameField() {
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                gameField[i][j] = new Figure(TypeOfFigure.EMPTY, j, i);
            }
        }
        for (int i = 0; i < 8; i++) {
            gameField[6][i] = new Figure(TypeOfFigure.PAWN, i, 6, FigureColor.WHITE);
        }
        for (int i = 0; i < 8; i++) {
            gameField[1][i] = new Figure(TypeOfFigure.PAWN, i, 1, FigureColor.BLACK);
        }
        gameField[7][0] = new Figure(TypeOfFigure.ROOK, 0, 7, FigureColor.WHITE);
        gameField[7][1] = new Figure(TypeOfFigure.KNIGHT, 1, 7, FigureColor.WHITE);
        gameField[7][2] = new Figure(TypeOfFigure.BISHOP, 2, 7, FigureColor.WHITE);
        gameField[7][3] = new Figure(TypeOfFigure.QWEEN, 3, 7, FigureColor.WHITE);
        gameField[7][4] = new Figure(TypeOfFigure.KING, 4, 7, FigureColor.WHITE);
        gameField[7][5] = new Figure(TypeOfFigure.BISHOP, 5, 7, FigureColor.WHITE);
        gameField[7][6] = new Figure(TypeOfFigure.KNIGHT, 6, 7, FigureColor.WHITE);
        gameField[7][7] = new Figure(TypeOfFigure.ROOK, 7, 7, FigureColor.WHITE);

        gameField[0][0] = new Figure(TypeOfFigure.ROOK, 0, 0, FigureColor.BLACK);
        gameField[0][1] = new Figure(TypeOfFigure.KNIGHT, 1, 0, FigureColor.BLACK);
        gameField[0][2] = new Figure(TypeOfFigure.BISHOP, 2, 0, FigureColor.BLACK);
        gameField[0][3] = new Figure(TypeOfFigure.QWEEN, 3, 0, FigureColor.BLACK);
        gameField[0][4] = new Figure(TypeOfFigure.KING, 4, 0, FigureColor.BLACK);
        gameField[0][5] = new Figure(TypeOfFigure.BISHOP, 5, 0, FigureColor.BLACK);
        gameField[0][6] = new Figure(TypeOfFigure.KNIGHT, 6, 0, FigureColor.BLACK);
        gameField[0][7] = new Figure(TypeOfFigure.ROOK, 7, 0, FigureColor.BLACK);
    }

    public void saveGame(Path path) {
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            ous.writeObject(gameField);
            ous.writeObject(turn);
            ous.writeObject(eatenFigures);
        } catch (IOException e) {
            //ignored
        }
    }

    public void loadGame(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            this.gameField = (Figure[][]) ois.readObject();
            this.turn = (FigureColor) ois.readObject();
            eatenFigures = (EatenFigures) ois.readObject();
            activeFigure = null;
            isGameStopped = false;
            winner = null;
            previosGameField = new Stack();
            previosTurn = new Stack();
            checkFigure = getCheckKing();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
