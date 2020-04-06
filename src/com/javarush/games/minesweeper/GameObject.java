package com.javarush.games.minesweeper;

public class GameObject {
    public int x, y;
    public boolean isMine;
    public boolean isOpen;
    public boolean isFlag;
    public int countMineNeighbors;

    public GameObject(int x, int y,boolean mine) {
        this.x = x;
        this.y = y;
        isMine=mine;
    }
}
