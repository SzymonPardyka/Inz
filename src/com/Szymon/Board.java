package com.Szymon;

import java.util.ArrayList;
import java.util.Random;


public class Board {

    private static final String BLUE_B = "\u001B[44m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String GREY = "\033[1;97m";
    private static final String RED = "\033[55;41m";
    private static final String YELLOW = "\033[43m";
    private static final String WHITE = "\033[47m";

    Random rand = new Random();
    private int[][] board;
    private int boardSide;
    private Rectangle rectangle = new Rectangle();
    private Wall wall = new Wall();

    public Board(int boardSide) {
        this.boardSide = boardSide;
        this.board = createBoard(boardSide);
    }
    public int[][] createBoard(int boardSide){
        return new int[boardSide][boardSide];
    }
    public void printBoard(){
        for(int i=0;i<boardSide;i++){
            for(int j=0;j<boardSide;j++){
                if(board[i][j]==0){
                    System.out.print(GREY +"  "+ANSI_RESET);
                }
                if(board[i][j]==1){
                    System.out.print(WHITE +"  "+ANSI_RESET);
                }
                if(board[i][j]==2){
                    System.out.print(BLUE_B+"  "+ANSI_RESET);
                }
                if(board[i][j]==3){
                    System.out.print(YELLOW+"  "+ANSI_RESET);
                }
                if(board[i][j]==4){
                    System.out.print(RED+"  "+ANSI_RESET);
                }
            }
            System.out.println();
        }
    }
    //board[i][j]+
    public void clearBoard(){
        for(int i=0;i<boardSide;i++){
            for(int j=0;j<boardSide;j++){
                board[i][j]=0;
            }
        }
    }

    private int[] generateCord(int parameter){
        int[] point = new int[2];
        point[0] = rand.nextInt(parameter);
        point[1] = rand.nextInt(parameter);
        while(true){
            if(board[point[0]][point[1]]!=1){
                point[0]=rand.nextInt(parameter);
                point[1]=rand.nextInt(parameter);
            }
            else{
                break;
            }
        }
        return point;
    }
    private int[] choosePointToCorner(){
        int[] point = generateCord(boardSide);
        while(!validatePoint(point)){
            point = generateCord(boardSide);
        }
        return point;
    }
    private boolean validatePoint(int[] point){
        int x=point[0];
        int y=point[1];
        for(int i=1;i<5;i++){
            if(board[x+i][y]!=1 || board[x-i][y]!=1 || board[x][y+i]!=1 || board[x][y-i]!=1 ||
                    board[x+i][y+i]!=1 || board[x-i][y-i]!=1 || board[x-i][y+i]!=1 || board[x+i][y-i]!=1){
                return false;
            }
        }
        return true;
    }
    private int[] setRoomPlan(){
        int[] roomPlan = new int[2];
        roomPlan[0]=rand.nextInt(4)+2;
        switch (roomPlan[0]){
            case 2:
                roomPlan[1]=rand.nextInt(2);
                break;
            case 3:
                roomPlan[1]=rand.nextInt(3);
                break;
            case 4:
                roomPlan[1]=rand.nextInt(3);
                break;
            case 5:
                roomPlan[1]=rand.nextInt(4);
                break;
        }
        return roomPlan;
    }


    private ArrayList<Point> placeRobot(){
        ArrayList<Point> availablePoints = findAvailablePoints();
        Point point = availablePoints.get(rand.nextInt(availablePoints.size()));
        while(!isTherePlaceForRobot(point)){
            availablePoints.remove(point);
            point = availablePoints.get(rand.nextInt(availablePoints.size()));
        }
        return setRobotPosition(point);
    }
    private ArrayList<Point> findAvailablePoints(){
        ArrayList<Point> availablePoints = new ArrayList<>();
        for(int i=0;i<boardSide;i++){
            for(int j=0;j<boardSide;j++){
                if(board[i][j]==1){
                    availablePoints.add(new Point(i,j));
                }
            }
        }
        return availablePoints;
    }
    private boolean isTherePlaceForRobot(Point point){
        int x=point.getX();
        int y=point.getY();
        if(board[x][y+1]==1 && board[x+1][y]==1 && board[x+1][y+1]==1 && board[x][y+2]==1
                && board[x+1][y+2]==1 && board[x+2][y+2]==1 && board[x+2][y+1]==1
                && board[x+2][y]==1 && board[x+2][y-1]==1 && board[x+1][y-1]==1
                && board[x][y-1]==1 && board[x-1][y-1]==1 && board[x-1][y]==1
                && board[x-1][y+1]==1 && board[x-1][y+2]==1){
            return true;
        }
        return false;
    }
    private ArrayList<Point> setRobotPosition(Point point){
        ArrayList<Point> position = new ArrayList<>();
        int x=point.getX();
        int y=point.getY();
        position.add(point);
        position.add(new Point(x,y+1));
        position.add(new Point(x+1,y));
        position.add(new Point(x+1,y+1));
        return position;
    }

    public void createRoom(){
        double result=0;
        for(int i=0;i<1;i++){
            int[] roomPlan = setRoomPlan();
            int[] cords;
//            rectangle.createRectangle(boardSide,board);
//            for(int j=0;j<roomPlan[0]-1;j++){
//                cords = choosePointToCorner();
//                rectangle.createRectangle(cords[0],cords[1],boardSide,board);
//            }
//            for(int j=0;j<roomPlan[1];j++){
//                wall.drawWall(board,boardSide);
//            }
            rectangle.createRectangle(boardSide,board);
            for(int j=0;j<5;j++){
                cords = choosePointToCorner();
                rectangle.createRectangle(cords[0],cords[1],boardSide,board);
            }
            for(int j=0;j<3;j++){
                wall.drawWall(board,boardSide);
            }
            printBoard();
            Robot robot = new Robot(placeRobot(),boardSide);
            robot.putRobotOnBoard(board);
            for(int j=0;j<1;j++) {
                result = result+robot.work(board);
            }
            clearBoard();
        }
    }

    //test functions
    private ArrayList<Point> testPlaceRobot(){
        Point point = new Point(34,25);
        return setRobotPosition(point);
    }
}
