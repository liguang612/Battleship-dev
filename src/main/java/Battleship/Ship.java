package Battleship;

import javafx.scene.Parent;


public class Ship extends Parent {
    private int type;
    private boolean vertical;
    private int health;
    private String name;
    private int x;
    private int y;

    public Ship(int type,boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;
    }

    public Ship(int type,boolean vertical,String name){
        this(type,vertical);
        this.name = name;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getType() {
        return type;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
