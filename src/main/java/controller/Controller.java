package controller;

import view.View;
import model.FigureColor;
import model.Model;
import model.TypeOfFigure;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Controller implements MouseListener, MouseMotionListener, ActionListener, ChessListener, WindowListener {
    private Model model;
    private View view;
    private ChessTimer timer;
    private int timeCount = 0;
    private boolean isTimeGame = false;
    private static final String pathToSaveFolder = URLDecoder.decode(ClassLoader.getSystemResource("save").getFile().replaceFirst("/", "") + "/");
    private String whitePlayer;
    private String blackPlayer;
    private Properties res;
    private static final String pathToRes = URLDecoder.decode(ClassLoader.getSystemResource("res.properties").getFile().replaceFirst("/", ""));

    static {
        String dir = pathToSaveFolder.substring(0, pathToSaveFolder.length() - 1);
        new File(dir).mkdir();
        try {
            File file = new File(pathToRes);
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            //ignored
        }
    }

    public Controller(Model model) {
        this.model = model;
        timer = new ChessTimer(this);
        res = new Properties();
        try {
            res.load(new FileInputStream(pathToRes));
        } catch (IOException e) {
        }
        whitePlayer = res.containsKey("whitePlayerName") ? res.getProperty("whitePlayerName") : "Player 1";
        blackPlayer = res.containsKey("blackPlayerName") ? res.getProperty("blackPlayerName") : "Player 2";
    }

    public void setTimeCount(int timeCount) {
        this.timeCount = timeCount;
        timer.setTime(timeCount);
    }

    public void setView(View view) {
        this.view = view;
        timer.setView(view);
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public Properties getRes() {
        return res;
    }

    public void setPlayerName(FigureColor color) {
        String name;
        while (true) {
            name = view.askString("Введите имя:");
            if (name.length() <= 12)
                break;
            view.showMessage("Имя должно быть не более 12 символов.");
        }
        if (color == FigureColor.WHITE)
            whitePlayer = name;
        else if (color == FigureColor.BLACK)
            blackPlayer = name;
        view.updateScore();
    }

    public void swapNames() {
        if (model.isGameStopped()) {
            String tmp = whitePlayer;
            whitePlayer = blackPlayer;
            blackPlayer = tmp;
            view.updateScore();
        }
    }

    //Main
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(model, controller);
        controller.setView(view);
        model.setChessListener(controller);
    }

    private int[] getAvailableCoordinates(int x, int y) {
        boolean reverse = view.isReverseView() && model.getTurn() == FigureColor.BLACK;
        int[] result = new int[2];
        result[0] = reverse ? 7 - (x / view.CELL_SIZE) : (x / view.CELL_SIZE);
        result[1] = reverse ? 7 - (y / view.CELL_SIZE) : (y / view.CELL_SIZE);
        return result;
    }

    public boolean isTimeGame() {
        return isTimeGame;
    }

    public void createNewGame() {
        if (model.isGameStopped() || view.askQuestion("Вы хотите прервать текущую партию?") == 0) {
            stopGame();
            swapNames();
            model.createGame();
            if (timeCount != 0) {
                isTimeGame = true;
                timer.setTime(timeCount);
                timer.reset();
                timer.startTimer();
            } else
                isTimeGame = false;
            view.updateView();
        }
    }

    public void stopGame() {
        model.setGameStopped(true);
        timer.stop();
        timer.reset();
        view.updateView();
    }

    public void doAction(int x, int y) {
        if (!model.isGameStopped()) {
            int[] coord = getAvailableCoordinates(x, y);
            if (model.isFigureChanged()) {
                if (model.moveTheFigure(coord[0], coord[1], model.getCellsToMove()))
                    timer.changeActiveTimer();
            } else {
                model.setActiveFigure(coord[0], coord[1]);
            }
            view.updateView();
        }
    }

    public void saveGame() {
        if (!model.isGameStopped() && !isTimeGame) {
            while (true) {
                String filename = pathToSaveFolder + view.askString("Введите название партии:") + ".chess";
                if (Files.notExists(Paths.get(filename))) {
                    model.saveGame(Paths.get(filename));
                    break;
                }
                view.showMessage("Такая партия уже существует.");
            }
        } else
            view.showMessage("Невозможно сохранить игру.");
    }

    public void loadGame() {
        JFileChooser jfc = new JFileChooser(pathToSaveFolder);
        jfc.setDialogTitle("Выберите файл:");
        int result = jfc.showOpenDialog(view);
        if (result == jfc.APPROVE_OPTION) {
            model.loadGame(jfc.getSelectedFile().toPath());
        }
        view.updateView();
    }

    private void saveProperties() {
        res.setProperty("ColorTheme", view.getColorTheme().toString());
        res.setProperty("whitePlayerName", whitePlayer);
        res.setProperty("blackPlayerName", blackPlayer);
        res.setProperty("markActiveFields", Boolean.toString(view.isMarkActiveFields()));
        res.setProperty("reverseView", Boolean.toString(view.isReverseView()));
        res.setProperty("figureStyle", view.getFigureStyle().toString());
        try {
            res.store(new FileOutputStream(pathToRes), "");
        } catch (IOException e) {
        }
    }

    public void stopGameIfTime(FigureColor color) {
        model.setGameStopped(true);
        model.setWinner(color);
        view.updateView();
        isTimeGame = false;
        view.showMessage(String.format("%s победили. Время закончилось.",
                color == FigureColor.WHITE ? "Белые" : "Черные"));
    }

    public String getTime(FigureColor color) {
        return timer.getTime(color);
    }

    public void mouseClicked(MouseEvent e) {
        doAction(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!model.isGameStopped()) {
            int[] coord = getAvailableCoordinates(e.getX(), e.getY());
            model.setActiveFigure(coord[0], coord[1]);
            if (model.isFigureChanged()) {
                view.updateView();
                view.doBeforeMoving(e.getX() / 80, e.getY() / 80, e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (model.isFigureChanged()) {
            int[] coord = getAvailableCoordinates(e.getX(), e.getY());
            if (model.moveTheFigure(coord[0], coord[1], model.getCellsToMove()))
                timer.changeActiveTimer();
        }
        view.doPostMoving();
        view.updateView();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        view.doMoving(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        if (!model.isGameStopped()) {
            String command = e.getActionCommand();
            switch (command) {
                case "\u2B8C":
                    if (!isTimeGame) {
                        model.rollBack(true);
                        view.updateView();
                    }
                    break;
                case "\u2690":
                    stopGame();
                    model.setWinner(model.getTurn() == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE);
                    view.showMessage(String.format("%s победили. Противник сдался.",
                            model.getWinner() == FigureColor.WHITE ? "Белые" : "Черные"));
                    break;
                case "\u2696":
                    if (view.askQuestion("Противник предлагает ничью. Вы согласны?") == 0) {
                        stopGame();
                        view.showMessage("Ничья.");
                    }
                    break;
            }
        }
    }

    //ChessListener
    @Override
    public void showWinnerIfMate(FigureColor winner) {
        stopGame();
        if (winner == null) {
            view.showMessage("Пат. Противник не может сделать ход.");
            return;
        }
        String win = "";
        switch (winner) {
            case WHITE:
                win = "Белые";
                break;
            case BLACK:
                win = "Черные";
                break;
        }
        view.showMessage(String.format("Шах и мат. %s победили", win));
    }

    @Override
    public TypeOfFigure askTypeOfFigure() {
        String type = view.askFigure("Выберите фигуру:");
        switch (type) {
            case "Ферзь":
                return TypeOfFigure.QWEEN;
            case "Ладья":
                return TypeOfFigure.ROOK;
            case "Слон":
                return TypeOfFigure.BISHOP;
            case "Конь":
                return TypeOfFigure.KNIGHT;
        }
        return null;
    }

    //WindowListener implementation
    @Override
    public void windowOpened(WindowEvent e) {
        Path filename = Paths.get(pathToSaveFolder + "default.chess");
        if (Files.exists(filename)) {
            model.loadGame(filename);
            view.updateView();
            try {
                Files.delete(filename);
            } catch (IOException ioException) {
                view.showMessage("Ошибка при удалении файла default.chess.");
            }
        } else {
            if (timeCount != 0) {
                isTimeGame = true;
                timer.setTime(timeCount);
                timer.reset();
            }
            model.createGame();
        }
        view.updateView();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        saveProperties();
        if (!model.isGameStopped() && !isTimeGame) {
            Path filename = Paths.get(pathToSaveFolder + "default.chess");
            if (Files.exists(filename)) {
                try {
                    Files.delete(filename);
                } catch (IOException ex) {
                    view.showMessage("Ошибка при удалении файла default.chess.");
                }
            }
            model.saveGame(filename);
        }
        e.getWindow().setVisible(false);
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
