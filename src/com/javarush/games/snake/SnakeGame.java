package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private static final int GOAL = 28;
    private int score;
    private Snake snake;
    private Apple apple;
    private int turnDelay;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        score=0;
        setScore(score);
        isGameStopped = false;
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        turnDelay = 300;
        drawScene();
        setTurnTimer(turnDelay);
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.BLUE, "", Color.DARKSEAGREEN);
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case DOWN:
                snake.setDirection(Direction.DOWN);
                break;
            case RIGHT:
                snake.setDirection(Direction.RIGHT);
                break;
            case LEFT:
                snake.setDirection(Direction.LEFT);
                break;
            case UP:
                snake.setDirection(Direction.UP);
                break;
            case SPACE:
                if (isGameStopped) {
                    createGame();
                    break;
                }
        }
    }

    @Override
    public void onTurn(int i) {
        if (!apple.isAlive) {
            setScore(score+=5);
            createNewApple();
            setTurnTimer(turnDelay-=10);
        }
        if (!snake.isAlive) {
            gameOver();
        } else {
            snake.move(apple);
        }
        if (GOAL < snake.getLength()) {
            win();
        }
        drawScene();
    }

    private void createNewApple() {
        do {
            apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        }
        while (snake.checkCollision(apple));
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "GAME OVER", Color.RED, 75);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "YOU WIN", Color.RED, 75);
    }
}
