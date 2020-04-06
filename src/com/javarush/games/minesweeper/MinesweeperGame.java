package com.javarush.games.minesweeper;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {

    private static final int SIDE = 9; // Размерность поля
    private int score; // Количество очков
    private static final String MINE = "\uD83D\uDCA3"; // символ МИНЫ
    private static final String FLAG = "\uD83D\uDEA9"; // символ МИНЫ
    private int countMinesOnField; // количество мин на поле
    private int countFlags; // количество флагов на поле
    private int countClosedTiles = SIDE * SIDE; // количество закрытых ячеек на поле
    private boolean isGameStopped; // Признак приостановки игры
    private boolean isGameHacked; // Признак приостановки игры
    private GameObject[][] gameField = new GameObject[SIDE][SIDE]; // ПОЛЕ


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) {
            restart();
        } else {
            openTile(x, y);
            if (!isGameStopped)
                score -= 5;
            setScore(score);
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        hackGame();
        //markTile(x, y);
    }


    private void createGame() {
        score = SIDE * SIDE * 5;
        setScore(score);
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                boolean isMine = false;
                if (getRandomNumber(10) == 1) {
                    isMine = true;
                    countMinesOnField++;
                }
                gameField[j][i] = new GameObject(i, j, isMine);
                GameObject cell = gameField[j][i];
                setCellColor(cell.x, cell.y, Color.ORANGE);
                setCellValue(cell.x, cell.y, "");
            }
        }
        countFlags = countMinesOnField;
        countMineNeighbors();
    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (!gameField[y][x].isMine) {
                    int count = 0;
                    List<GameObject> list = getNeighbors(gameField[y][x]);
                    for (GameObject object : list) {
                        if (object.isMine) {
                            count++;
                        }
                    }

                    gameField[y][x].countMineNeighbors = count;
                }
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void openTile(int x, int y) {
        GameObject element = gameField[y][x];
        if (!isGameStopped && !element.isFlag && !element.isOpen) {
            element.isOpen = true;
            countClosedTiles--;

            if (element.isMine) {
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else {
                if (element.countMineNeighbors != 0) {
                    setCellNumber(x, y, element.countMineNeighbors);
                } else {
                    setCellValue(x, y, "");
                }
                setCellColor(x, y, Color.GREEN);
                if (element.countMineNeighbors == 0) {
                    List<GameObject> objects = getNeighbors(element);
                    for (GameObject object : objects) {
                        if (!object.isOpen)
                            openTile(object.x, object.y);
                    }
                }
                if (countClosedTiles == countMinesOnField) {
                    win();
                }
            }
        }
    }

    private void markTile(int x, int y) {
        if (!isGameStopped) {
            GameObject element = gameField[y][x];
            if (!element.isOpen && !element.isFlag && countFlags > 0) {
                element.isFlag = true;
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.BLUE);
            } else if (element.isFlag) {
                element.isFlag = false;
                setCellValue(x, y, "");
                setCellColor(x, y, Color.ORANGE);
                countFlags++;
            }
        }
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "Успех!", Color.BLACK, 100);
    }

    private void gameOver() {
        score = -100500;
        setScore(score);
        isGameStopped = true;
        showMessageDialog(Color.TURQUOISE, "ПОТРАЧЕНО!", Color.BLUEVIOLET, 50);
    }

    private void restart() {
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        countFlags = 0;
        countMinesOnField = 0;
        score = 0;
        setScore(score);
        createGame();
    }

    private void hackGame() {
        if (!isGameHacked) {
            isGameHacked=true;
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (gameField[j][i].isMine) {
                        setCellValueEx(i, j, Color.BLUE, MINE);
                    }
                }
            }
        } else {
            isGameHacked=false;
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (gameField[j][i].isMine) {
                        setCellValueEx(i, j, Color.ORANGE, "");
                    }
                }
            }
        }
    }

}
