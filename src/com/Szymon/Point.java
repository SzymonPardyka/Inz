package com.Szymon;

import static java.sql.Types.NULL;

public class Point {
    private int x;
    private int y;
    private int value;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getValue() {
        return value;
    }

    public void setPoint(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setPoint(int x, int y, int value){
        this.x = x;
        this.y = y;
        this.value=value;
    }
    public void setPoint(Point point){
        this.x = point.getX();
        this.y = point.getY();
        if(point.getValue()!=NULL){
            this.value=point.getValue();
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
