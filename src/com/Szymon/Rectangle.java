package com.Szymon;

import java.util.ArrayList;
import java.util.Random;

public class Rectangle {
    private Random rand = new Random();

    private int generateCord(int border){
        return rand.nextInt(border-2)+1;
    }
    private int generateCord(int border, int otherPoint){
        int cord = rand.nextInt(border-2)+1;
        while(Math.abs(cord-otherPoint)<11){
            cord = rand.nextInt(border-2)+1;
        }
        return cord;
    }

    public ArrayList<Point> generateRectangle(int boardSide){
        int cornerX = generateCord(boardSide);
        int cornerY = generateCord(boardSide);
        int oppositeCornerX = generateCord(boardSide,cornerX);
        int oppositeCornerY = generateCord(boardSide,cornerY);
        ArrayList<Point> rectangle = new ArrayList<>();
        setPointsInOrder(rectangle,cornerX,cornerY,oppositeCornerX,oppositeCornerY);
        addPointsHorizontally(cornerX,oppositeCornerX,cornerY,rectangle);
        addPointsHorizontally(cornerX,oppositeCornerX,oppositeCornerY,rectangle);
        addPointsVertically(cornerY,oppositeCornerY,cornerX,rectangle);
        addPointsVertically(cornerY,oppositeCornerY,oppositeCornerX,rectangle);
        return rectangle;
    }
    public ArrayList<Point> generateRectangle(int boardSide, int cornerX, int cornerY){
        int oppositeCornerX = generateCord(boardSide,cornerX);
        int oppositeCornerY = generateCord(boardSide,cornerY);
        ArrayList<Point> rectangle = new ArrayList<>();
        setPointsInOrder(rectangle,cornerX,cornerY,oppositeCornerX,oppositeCornerY);
        addPointsHorizontally(cornerX,oppositeCornerX,cornerY,rectangle);
        addPointsHorizontally(cornerX,oppositeCornerX,oppositeCornerY,rectangle);
        addPointsVertically(cornerY,oppositeCornerY,cornerX,rectangle);
        addPointsVertically(cornerY,oppositeCornerY,oppositeCornerX,rectangle);
        return rectangle;
    }

    private void setPointsInOrder (ArrayList<Point> rectangle,int cornerX, int cornerY, int oppositeCornerX, int oppositeCornerY){
        if(cornerX<oppositeCornerX){
            if(cornerY<oppositeCornerY){
                rectangle.add(new Point(cornerX,cornerY));
                rectangle.add(new Point(oppositeCornerX,oppositeCornerY));
                rectangle.add(new Point(cornerX,oppositeCornerY));
                rectangle.add(new Point(oppositeCornerX,cornerY));
            }
            else{
                rectangle.add(new Point(cornerX,oppositeCornerY));
                rectangle.add(new Point(oppositeCornerX,cornerY));
                rectangle.add(new Point(cornerX,cornerY));
                rectangle.add(new Point(oppositeCornerX,oppositeCornerY));
            }
        }
        else{
            if(cornerY<oppositeCornerY){
                rectangle.add(new Point(oppositeCornerX,cornerY));
                rectangle.add(new Point(cornerX,oppositeCornerY));
                rectangle.add(new Point(cornerX,cornerY));
                rectangle.add(new Point(oppositeCornerX,oppositeCornerY));
            }
            else{
                rectangle.add(new Point(oppositeCornerX,oppositeCornerY));
                rectangle.add(new Point(cornerX,cornerY));
                rectangle.add(new Point(cornerX,oppositeCornerY));
                rectangle.add(new Point(oppositeCornerX,cornerY));
            }
        }
    }

    public void addPointsHorizontally(int cornerX, int oppositeCornerX, int yCord, ArrayList<Point> rectangle){
        if(cornerX<oppositeCornerX) {
            for (int i = cornerX + 1; i < oppositeCornerX; i++) {
                rectangle.add(new Point(i, yCord));
            }
        }
        else{
            for (int i = oppositeCornerX + 1; i < cornerX; i++) {
                rectangle.add(new Point(i, yCord));
            }
        }
    }
    public void addPointsVertically(int cornerY, int oppositeCornerY, int xCord, ArrayList<Point> rectangle){
        if(cornerY<oppositeCornerY) {
            for (int i = cornerY + 1; i < oppositeCornerY; i++) {
                rectangle.add(new Point(xCord, i));
            }
        }
        else {
            for (int i = oppositeCornerY + 1; i < cornerY; i++) {
                rectangle.add(new Point(xCord, i));
            }
        }
    }

    private void drawRectangleWithWalls(ArrayList<Point> rectanglePoints, int[][] board){
        for(int i=0;i<rectanglePoints.size();i++){
            int value = board[rectanglePoints.get(i).getX()][rectanglePoints.get(i).getY()];
            if(value == 0 || value == 1){
                board[rectanglePoints.get(i).getX()][rectanglePoints.get(i).getY()]=2;
            }
        }
    }
    private void drawRectangleWithoutWalls(ArrayList<Point> rectanglePoints, int[][] board){
        for (int i=0; i<rectanglePoints.size(); i++) {
            if (board[rectanglePoints.get(i).getX()][rectanglePoints.get(i).getY()] == 0) {
                board[rectanglePoints.get(i).getX()][rectanglePoints.get(i).getY()] = 2;
            }
        }
    }
    private void fillRectangle(ArrayList<Point> rectanglePoints,int[][] board){
        for(int i=rectanglePoints.get(0).getX()+1; i<rectanglePoints.get(1).getX(); i++) {
            for (int j=rectanglePoints.get(0).getY()+1; j<rectanglePoints.get(1).getY(); j++) {
                board[i][j]=1;
            }
        }
    }

    public void createRectangle(int boardSide, int[][] board){
        ArrayList<Point> rectanglePoints = generateRectangle(boardSide);
        drawRectangleWithWalls(rectanglePoints,board);
        fillRectangle(rectanglePoints,board);
    }
    public void createRectangle(int cornerX, int cornerY, int boardSide, int[][] board){
        ArrayList<Point> rectanglePoints = generateRectangle(boardSide,cornerX,cornerY);         //walls + doors
        drawRectangleWithoutWalls(rectanglePoints,board);
        fillRectangle(rectanglePoints,board);
    }




    // test functions
    private void fillRectangleWithZeros(ArrayList<Point> rectanglePoints,int[][] board){
        for(int i=rectanglePoints.get(0).getX()+1; i<rectanglePoints.get(1).getX(); i++) {
            for (int j=rectanglePoints.get(0).getY()+1; j<rectanglePoints.get(1).getY(); j++) {
                board[i][j]=0;
            }
        }
    }
    public void testRectangle(int[][] board){
        ArrayList<Point> rectanglePoints = new ArrayList<>();
        rectanglePoints.add(new Point(10,10));
        rectanglePoints.add(new Point(40,40));
        rectanglePoints.add(new Point(10,40));
        rectanglePoints.add(new Point(40,10));
        addPointsHorizontally(10,40,10,rectanglePoints);
        addPointsHorizontally(10,40,40,rectanglePoints);
        addPointsVertically(10,40,10,rectanglePoints);
        addPointsVertically(10,40,40,rectanglePoints);
        drawRectangleWithWalls(rectanglePoints,board);
        fillRectangle(rectanglePoints,board);
//        rectanglePoints.removeAll(rectanglePoints);
//        rectanglePoints.add(new Point(22,22));
//        rectanglePoints.add(new Point(28,28));
//        rectanglePoints.add(new Point(22,28));
//        rectanglePoints.add(new Point(28,22));
//        addPointsHorizontally(22,28,22,rectanglePoints);
//        addPointsHorizontally(22,28,28,rectanglePoints);
//        addPointsVertically(22,28,22,rectanglePoints);
//        addPointsVertically(22,28,28,rectanglePoints);
//        drawRectangleWithWalls(rectanglePoints,board);
//        fillRectangleWithZeros(rectanglePoints,board);
    }

}
