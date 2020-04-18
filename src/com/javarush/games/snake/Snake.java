package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public boolean isAlive = true;
    public Direction direction = Direction.LEFT;
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    private List<GameObject> snakeParts = new ArrayList<>();

    public void draw(Game game) {
        if (isAlive) {
            for (GameObject e : snakeParts) {
                game.setCellValueEx(e.x, e.y, Color.NONE, BODY_SIGN, Color.TURQUOISE, 75);
            }
            game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, Color.TURQUOISE, 75);
        } else {
            for (GameObject e : snakeParts) {
                game.setCellValueEx(e.x, e.y, Color.NONE, BODY_SIGN, Color.RED, 75);
            }
            game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, Color.RED, 75);
        }
    }

    public Snake(int x, int y) {
        GameObject gameObject1 = new GameObject(x, y);
        GameObject gameObject2 = new GameObject(x + 1, y);
        GameObject gameObject3 = new GameObject(x + 2, y);
        snakeParts.add(gameObject1);
        snakeParts.add(gameObject2);
        snakeParts.add(gameObject3);
    }

    public void setDirection(Direction direction) {
        switch (this.direction) {
            case RIGHT:
                if ((snakeParts.get(0).y == snakeParts.get(1).y) && direction == Direction.LEFT) {
                    return;
                }
                break;
            case LEFT:
                if ((snakeParts.get(0).y == snakeParts.get(1).y) && direction == Direction.RIGHT) {
                    return;
                }
                break;
            case DOWN:
                if ((snakeParts.get(0).x == snakeParts.get(1).x) && direction == Direction.UP) {
                    return;
                }
                break;
            case UP:
                if ((snakeParts.get(0).x == snakeParts.get(1).x) && direction == Direction.DOWN) {
                    return;
                }
                break;
        }
        this.direction = direction;
    }

    public void move(Apple apple) {
        GameObject head = createNewHead(); //Если новая голова не вышла за пределы поля + не наткнулась на саму себя:
        if (head.x >= 0 && head.x < SnakeGame.WIDTH && head.y >= 0 && head.y < SnakeGame.HEIGHT && !checkCollision(head)) {
            snakeParts.add(0, head);
            if (head.x == apple.x && head.y == apple.y) {
                apple.isAlive = false;
            } else {
                removeTail();
            }
        } else {
            isAlive = false;
        }

    }

    public GameObject createNewHead() {
        int headX = snakeParts.get(0).x;
        int headY = snakeParts.get(0).y;
        switch (direction) {
            case UP:
                return new GameObject(headX, headY - 1);
            case DOWN:
                return new GameObject(headX, headY + 1);
            case LEFT:
                return new GameObject(headX - 1, headY);
            case RIGHT:
                return new GameObject(headX + 1, headY);
            default:
                return null;
        }
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject) {
        boolean result = false;
        for (GameObject element : snakeParts) {
            if (element.equals(gameObject)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
