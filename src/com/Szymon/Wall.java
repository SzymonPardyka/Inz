package com.Szymon;

import java.util.ArrayList;
import java.util.Random;

public class Wall {

    private Random rand = new Random();


    private Point choosePlaceToStartWall(int[][] board, int boardSide){
        ArrayList<Point> availablePoints = findAvailablePoints(board,boardSide);
        Point point = availablePoints.get(rand.nextInt(availablePoints.size()));
        while(true){
            if(validatePointInWall(point, board)){
                return point;
            }
            else{
                availablePoints.remove(point);
            }
            if(!availablePoints.isEmpty()){
                point = availablePoints.get(rand.nextInt(availablePoints.size()));
            }
            else{
                return null;
            }
        }
    }
    private ArrayList<Point> findAvailablePoints(int[][] board, int boardSide){
        ArrayList<Point> availablePoints = new ArrayList<>();
        for(int i=0;i<boardSide;i++){
            for(int j=0;j<boardSide;j++){
                if(isPointAWall(i,j,board)){
                    availablePoints.add(new Point(i,j));
                }
            }
        }
        return availablePoints;
    }


    public void drawWall(int[][] board, int boardSide){
        ArrayList<Point> wall = new ArrayList<>();
        int direction=5;
        wall=sketchWalls(wall,board,boardSide,false,direction);
        for (Point point : wall) {
            board[point.getX()][point.getY()] = 2;
        }
        if(rand.nextInt(2)==1) {
            wall = sketchWalls(wall, board, boardSide, true, direction);
            for (Point point : wall) {
                board[point.getX()][point.getY()] = 2;
            }
        }
    }
    private ArrayList<Point> sketchWalls(ArrayList<Point> wall,int[][] board, int boardSide, boolean extendingWall,int direction){
        int[] distances;
        Point point;
        int doorsWidth = 7;
        if(extendingWall){
            point = wall.get(wall.size()-1);
            wall.add(point);
            distances = checkDistances(point,board);
            if((distances[0] > doorsWidth) && (direction != 0)){
                sketchUpper(wall,point,board,(distances[0]- doorsWidth));
            }
            else if(distances[1]> doorsWidth && direction!=1){
                sketchLower(wall,point,board,(distances[1]- doorsWidth));
            }
            else if(distances[2]> doorsWidth && direction!=2){
                sketchLeft(wall,point,board,(distances[2]- doorsWidth));
            }
            else if(distances[3]> doorsWidth && direction!=3){
                sketchRight(wall,point,board,(distances[3]- doorsWidth));
            }
        }
        else{
            point = choosePlaceToStartWall(board,boardSide);
            wall.add(point);
            distances = checkDistances(point,board);
            if(distances[0]> doorsWidth){
                sketchUpper(wall,point,board,(distances[0]- doorsWidth));
                direction=0;
            }
            else if(distances[1]> doorsWidth){
                sketchLower(wall,point,board,(distances[1]- doorsWidth));
                direction=1;
            }
            else if(distances[2]> doorsWidth){
                sketchLeft(wall,point,board,(distances[2]- doorsWidth));
                direction=2;
            }
            else if(distances[3]> doorsWidth){
                sketchRight(wall,point,board,(distances[3]- doorsWidth));
                direction=3;
            }
        }
        return wall;
    }


    private boolean addPointLeft(ArrayList<Point> wall, Point point, int[][] board){
        Point nextPoint = new Point(point.getX(),point.getY()-1);
        for(int i=0;i<6;i++){
            for(int j=0;j<6;j++){
                if(board[nextPoint.getX()+i][nextPoint.getY()-j]!=1 || board[nextPoint.getX()-i][nextPoint.getY()-j]!=1){
                    return false;
                }
            }
        }
        wall.add(nextPoint);
        return true;
    }
    private void sketchLeft(ArrayList<Point> wall, Point point, int[][] board, int maxLength){
        boolean adding = addPointLeft(wall,point,board);
        int length = rand.nextInt(maxLength);
        int counter=1;
        while(adding){
            adding = addPointLeft(wall,wall.get(wall.size()-1),board);
            counter++;
            if(counter==length){
                adding=false;
            }
        }
    }

    private boolean addPointRight(ArrayList<Point> wall, Point point, int[][] board){
        Point nextPoint = new Point(point.getX(),point.getY()+1);
        for(int i=0;i<6;i++){
            for(int j=0;j<6;j++){
                if(board[nextPoint.getX()+i][nextPoint.getY()+j]!=1 || board[nextPoint.getX()-i][nextPoint.getY()+j]!=1){
                    return false;
                }
            }
        }
        wall.add(nextPoint);
        return true;
    }
    private void sketchRight(ArrayList<Point> wall, Point point, int[][] board, int maxLength){
        boolean adding = addPointRight(wall,point,board);
        int length = rand.nextInt(maxLength);
        int counter=1;
        while(adding){
            adding = addPointRight(wall,wall.get(wall.size()-1),board);
            counter++;
            if(counter==length){
                adding=false;
            }
        }
    }


    private boolean addPointUpper(ArrayList<Point> wall, Point point, int[][] board){
        Point nextPoint = new Point(point.getX()-1,point.getY());
        for(int i=0;i<6;i++){
            for(int j=0;j<6;j++){
                if(board[nextPoint.getX()-i][nextPoint.getY()+j]!=1 || board[nextPoint.getX()-i][nextPoint.getY()-j]!=1){
                    return false;
                }
            }
        }
        wall.add(nextPoint);
        return true;
    }
    private void sketchUpper(ArrayList<Point> wall, Point point, int[][] board, int maxLength){
        boolean adding = addPointUpper(wall,point,board);
        int length = rand.nextInt(maxLength);
        int counter=1;
        while(adding){
            adding = addPointUpper(wall,wall.get(wall.size()-1),board);
            counter++;
            if(counter==length){
                adding=false;
            }
        }
    }

    private boolean addPointLower(ArrayList<Point> wall, Point point, int[][] board){
        Point nextPoint = new Point(point.getX()+1,point.getY());
        for(int i=0;i<6;i++){
            for(int j=0;j<6;j++){
                if(board[nextPoint.getX()+i][nextPoint.getY()+j]!=1 || board[nextPoint.getX()+i][nextPoint.getY()-j]!=1){
                    return false;
                }
            }
        }
        wall.add(nextPoint);
        return true;
    }
    private void sketchLower(ArrayList<Point> wall, Point point, int[][] board, int maxLength){
        boolean adding = addPointLower(wall,point,board);
        int length = rand.nextInt(maxLength);
        int counter=1;
        while(adding){
            adding = addPointLower(wall,wall.get(wall.size()-1),board);
            counter++;
            if(counter==length){
                adding=false;
            }
        }
    }


    private boolean validateFreeSpacesAroundPoint(Point point, int[][] board){
        int[] distances = checkDistances(point,board);
        if(distances[0]>8 || distances[1]>8 || distances[2]>8 || distances[3]>8){
            return true;
        }
        return false;
    }
    private boolean validateWallAroundPoint(Point point, int[][] board){
        int[] distances = checkDistances(point,board);
        int counter =0;
        for(int i=4;i<8;i++){
            if(distances[i]>7){
                counter++;
            }
        }
        if(counter>1){
            return true;
        }
        return false;
    }
    private boolean validatePointInWall(Point point, int[][] board){
        if(validateFreeSpacesAroundPoint(point,board) && validateWallAroundPoint(point,board)){
            return true;
        }
        return false;
    }


    private boolean checkUpper(int x, int y, int[][] board, int value){
        if(board[x-1][y]==value){
            return true;
        }
        return false;
    }
    private boolean checkLower(int x, int y, int[][] board, int value){

        if(board[x+1][y]==value){
            return true;
        }
        return false;
    }
    private boolean checkLeft(int x, int y, int[][] board, int value){

        if(board[x][y-1]==value){
            return true;
        }
        return false;
    }
    private boolean checkRight(int x, int y, int[][] board, int value){
        if(board[x][y+1]==value){
            return true;
        }
        return false;
    }
    private boolean isPointAWall(int x, int y, int[][] board){
        if(board[x][y]==2){
            return true;
        }
        return false;
    }
    private boolean isPointAWall(Point point, int[][] board){
        int x = point.getX();
        int y = point.getY();
        if(board[x][y]==2){
            return true;
        }
        return false;
    }


    private int countFreePoints(int direction, Point point,int[][] board){
        int i=0;
        int counter=0;
        switch (direction){
            case 0:
                while(checkUpper(point.getX()-i,point.getY(),board,1)){
                    counter++;
                    i++;
                }
                break;
            case 1:
                while(checkLower(point.getX()+i,point.getY(),board,1)){
                    counter++;
                    i++;
                }
                break;
            case 2:
                while(checkLeft(point.getX(),point.getY()-i,board,1)){
                    counter++;
                    i++;
                }
                break;
            case 3:
                while(checkRight(point.getX(),point.getY()+i,board,1)){
                    counter++;
                    i++;
                }
                break;
        }
        return counter;
    }
    private int countWallPoints(int direction, Point point,int[][] board){
        int i=0;
        int counter=0;
        switch (direction){
            case 0:
                while(checkUpper(point.getX()-i,point.getY(),board,2)){
                    counter++;
                    i++;
                }
                break;
            case 1:
                while(checkLower(point.getX()+i,point.getY(),board,2)){
                    counter++;
                    i++;
                }
                break;
            case 2:
                while(checkLeft(point.getX(),point.getY()-i,board,2)){
                    counter++;
                    i++;
                }
                break;
            case 3:
                while(checkRight(point.getX(),point.getY()+i,board,2)){
                    counter++;
                    i++;
                }
                break;
        }
        return counter;
    }
    private int[] checkDistances(Point point, int[][] board){
        int[] distances = new int[8];
        for(int i=0;i<4;i++){
            distances[i]=countFreePoints(i,point,board);
        }
        for(int i=4;i<8;i++){
            distances[i]=countWallPoints(i-4,point,board);
        }
        return distances;
    }
}
