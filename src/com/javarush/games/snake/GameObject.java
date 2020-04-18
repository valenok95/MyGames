package com.javarush.games.snake;

public class GameObject {
    public int x, y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(GameObject gameObject) {
        return (this.x == gameObject.x & this.y == gameObject.y);
    }
}
