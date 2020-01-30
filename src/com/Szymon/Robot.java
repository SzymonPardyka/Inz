package com.Szymon;

import java.util.ArrayList;
import java.util.Scanner;

public class Robot {

    private static final String BLUE_B = "\u001B[44m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String GREY = "\033[1;97m";
    private static final String RED = "\033[55;41m";
    private static final String YELLOW = "\033[43m";
    private static final String WHITE = "\033[47m";

    private ArrayList<Point> position;
    private ArrayList<Point> mapPosition;
    private ArrayList<Point> followingWallStartPosition;
    private int facingDirection;
    private int[][] myMap;
    private int[][] wallMap;
    private int mapSide;
    private boolean handSteering=false;
    private Scanner scanner = new Scanner(System.in);

    public Robot(ArrayList<Point> position,int boardSide){
        this.myMap = new int[boardSide*2][boardSide*2];
        this.wallMap = new int[boardSide*2][boardSide*2];
        this.position = position;
        this.facingDirection=0;
        this.mapSide = boardSide*2;
        this.mapPosition = putRobotOnMap(boardSide);
        this.followingWallStartPosition = new ArrayList<>();
    }
    public void putRobotOnBoard(int[][] board){
        int x,y;
        for(int i=0;i<2;i++){
            x=this.position.get(i).getX();
            y=this.position.get(i).getY();
            board[x][y]=4;
        }
        for(int i=2;i<4;i++){
            x=this.position.get(i).getX();
            y=this.position.get(i).getY();
            board[x][y]=3;
        }
        whatCanISee(board);
    }
    private ArrayList<Point> putRobotOnMap(int maxBoardSide){
        ArrayList<Point> mapPosition = new ArrayList<>();
        this.myMap[maxBoardSide-1][maxBoardSide-1]=4;
        this.myMap[maxBoardSide-1][maxBoardSide]=4;
        this.myMap[maxBoardSide][maxBoardSide-1]=3;
        this.myMap[maxBoardSide][maxBoardSide]=3;
        mapPosition.add(new Point(maxBoardSide-1,maxBoardSide-1));
        mapPosition.add(new Point(maxBoardSide-1,maxBoardSide));
        mapPosition.add(new Point(maxBoardSide,maxBoardSide-1));
        mapPosition.add(new Point(maxBoardSide,maxBoardSide));
        return  mapPosition;
    }
    private void drawRobotOnBoard(int[][] board){
        board[this.position.get(0).getX()][this.position.get(0).getY()]=4;
        board[this.position.get(1).getX()][this.position.get(1).getY()]=4;
        board[this.position.get(2).getX()][this.position.get(2).getY()]=3;
        board[this.position.get(3).getX()][this.position.get(3).getY()]=3;
    }
    private void drawRobotOnMap(){
        myMap[this.mapPosition.get(0).getX()][this.mapPosition.get(0).getY()]=4;
        myMap[this.mapPosition.get(1).getX()][this.mapPosition.get(1).getY()]=4;
        myMap[this.mapPosition.get(2).getX()][this.mapPosition.get(2).getY()]=3;
        myMap[this.mapPosition.get(3).getX()][this.mapPosition.get(3).getY()]=3;
    }
    private void eraseRobotBoard(int[][] board){
        board[this.position.get(0).getX()][this.position.get(0).getY()]=1;
        board[this.position.get(1).getX()][this.position.get(1).getY()]=1;
        board[this.position.get(2).getX()][this.position.get(2).getY()]=1;
        board[this.position.get(3).getX()][this.position.get(3).getY()]=1;
    }
    private void eraseRobotMap(){
        myMap[this.mapPosition.get(0).getX()][this.mapPosition.get(0).getY()]=1;
        myMap[this.mapPosition.get(1).getX()][this.mapPosition.get(1).getY()]=1;
        myMap[this.mapPosition.get(2).getX()][this.mapPosition.get(2).getY()]=1;
        myMap[this.mapPosition.get(3).getX()][this.mapPosition.get(3).getY()]=1;
    }


    private void validateGraph(Graph graph,int[][] board){
        while(!graph.validateGraph()){
            rotateLeft(board);
            moveForward(board);
            graph.adjustGraph(myMap,mapSide);
        }
    }
    private boolean haveIMappedAllWalls(){
       for(int i=0;i<4;i++){
           if(!arePointsEqual(this.followingWallStartPosition.get(i),this.mapPosition.get(i))){
               return false;
           }
       }
       return true;
    }
    private boolean isMapDone(){
        ArrayList<Point> ones = findOnes();
        for(int i=0;i<ones.size();i++){
            if(!checkNeighbourhood(ones.get(i))){
                return false;
            }
        }
        return true;
    }
    private boolean checkNeighbourhood(Point point){
        int x=point.getX();
        int y=point.getY();
        if(myMap[x+1][y] == 0 || myMap[x-1][y] == 0 || myMap[x][y+1] == 0 || myMap[x][y-1] == 0){
            return false;
        }
        return true;
    }
    private ArrayList<Point> findOnes(){
        ArrayList<Point> ones = new ArrayList<>();
        for(int i=0;i<mapSide;i++){
            for(int j=0;j<mapSide;j++){
                if(myMap[i][j]==1){
                    ones.add(new Point(i,j));
                }
            }
        }
        return ones;
    }
    private boolean arePointsEqual(Point p1, Point p2){
        if(p1.getX() == p2.getX() && p1.getY() == p2.getY()){
            return true;
        }
        return false;
    }
    private void swapPoints(int p1, int p2){
        Point point1 = this.position.get(p1);
        Point point2 = this.position.get(p2);
        this.position.set(p1,point2);
        this.position.set(p2,point1);
        point1 = this.mapPosition.get(p1);
        point2 = this.mapPosition.get(p2);
        this.mapPosition.set(p1,point2);
        this.mapPosition.set(p2,point1);
    }



    private void rotateLeft(int[][] board){
        swapPoints(0,2);
        swapPoints(2,3);
        swapPoints(3,1);
        if(this.facingDirection==0){
            facingDirection=3;
        }
        else{
            this.facingDirection--;
        }
        drawRobotOnBoard(board);
        drawRobotOnMap();
        whatCanISee(board);
    }
    private void rotateRight(int[][] board){
        swapPoints(0,2);
        swapPoints(0,1);
        swapPoints(1,3);
        if(this.facingDirection==3){
            facingDirection=0;
        }
        else{
            this.facingDirection++;
        }
        drawRobotOnBoard(board);
        drawRobotOnMap();
        whatCanISee(board);
    }
    private boolean canIMoveForward(int[][] board){
        int frontState = checkFront(board);
        if(frontState==2 || frontState==3 || frontState==4 ||
                frontState==5 || frontState==6 || frontState==8){
            return false;
        }
        return true;
    }
    private void moveForward(int[][] board){
        if(canIMoveForward(board)) {
            switch (this.facingDirection) {
                case 0:
                    moveUpward(board);
                    break;
                case 1:
                    moveRight(board);
                    break;
                case 2:
                    moveDownward(board);
                    break;
                case 3:
                    moveLeft(board);
                    break;
            }
        }
        else{
            this.handSteering=true;
            int choice = 1;
            while(choice!=0){
                printMap();
                choice = printMenu();
                switch (choice){
                    case 1:
                        moveForward(board);
                        break;
                    case 2:
                        moveBackward(board);
                        break;
                    case 3:
                        rotateLeft(board);
                        break;
                    case 4:
                        rotateRight(board);
                        break;
                    case 5:
                        findFirstWall(board);
                        break;
                    case 6:
                        followWall(board);
                        break;
                    default:
                        break;

                }
            }
        }
        whatCanISee(board);
    }
    private void moveBackward(int[][] board){
        switch(this.facingDirection){
            case 0:
                moveDownward(board);
                break;
            case 1:
                moveLeft(board);
                break;
            case 3:
                moveRight(board);
                break;
            case 2:
                moveUpward(board);
                break;
        }
    }
    private void moveUpward(int[][] board){
        eraseRobotBoard(board);
        this.position.get(0).setPoint(this.position.get(0).getX()-1,this.position.get(0).getY());
        this.position.get(1).setPoint(this.position.get(1).getX()-1,this.position.get(1).getY());
        this.position.get(2).setPoint(this.position.get(2).getX()-1,this.position.get(2).getY());
        this.position.get(3).setPoint(this.position.get(3).getX()-1,this.position.get(3).getY());
        drawRobotOnBoard(board);

        eraseRobotMap();
        this.mapPosition.get(0).setPoint(this.mapPosition.get(0).getX()-1,this.mapPosition.get(0).getY());
        this.mapPosition.get(1).setPoint(this.mapPosition.get(1).getX()-1,this.mapPosition.get(1).getY());
        this.mapPosition.get(2).setPoint(this.mapPosition.get(2).getX()-1,this.mapPosition.get(2).getY());
        this.mapPosition.get(3).setPoint(this.mapPosition.get(3).getX()-1,this.mapPosition.get(3).getY());
        drawRobotOnMap();

    }
    private void moveDownward(int[][] board){
        eraseRobotBoard(board);
        this.position.get(0).setPoint(this.position.get(0).getX()+1,this.position.get(0).getY());
        this.position.get(1).setPoint(this.position.get(1).getX()+1,this.position.get(1).getY());
        this.position.get(2).setPoint(this.position.get(2).getX()+1,this.position.get(2).getY());
        this.position.get(3).setPoint(this.position.get(3).getX()+1,this.position.get(3).getY());
        drawRobotOnBoard(board);

        eraseRobotMap();
        this.mapPosition.get(0).setPoint(this.mapPosition.get(0).getX()+1,this.mapPosition.get(0).getY());
        this.mapPosition.get(1).setPoint(this.mapPosition.get(1).getX()+1,this.mapPosition.get(1).getY());
        this.mapPosition.get(2).setPoint(this.mapPosition.get(2).getX()+1,this.mapPosition.get(2).getY());
        this.mapPosition.get(3).setPoint(this.mapPosition.get(3).getX()+1,this.mapPosition.get(3).getY());
        drawRobotOnMap();
    }
    private void moveLeft(int[][] board){
        eraseRobotBoard(board);
        this.position.get(0).setPoint(this.position.get(0).getX(),this.position.get(0).getY()-1);
        this.position.get(1).setPoint(this.position.get(1).getX(),this.position.get(1).getY()-1);
        this.position.get(2).setPoint(this.position.get(2).getX(),this.position.get(2).getY()-1);
        this.position.get(3).setPoint(this.position.get(3).getX(),this.position.get(3).getY()-1);
        drawRobotOnBoard(board);

        eraseRobotMap();
        this.mapPosition.get(0).setPoint(this.mapPosition.get(0).getX(),this.mapPosition.get(0).getY()-1);
        this.mapPosition.get(1).setPoint(this.mapPosition.get(1).getX(),this.mapPosition.get(1).getY()-1);
        this.mapPosition.get(2).setPoint(this.mapPosition.get(2).getX(),this.mapPosition.get(2).getY()-1);
        this.mapPosition.get(3).setPoint(this.mapPosition.get(3).getX(),this.mapPosition.get(3).getY()-1);
        drawRobotOnMap();
    }
    private void moveRight(int[][] board){
        eraseRobotBoard(board);
        this.position.get(0).setPoint(this.position.get(0).getX(),this.position.get(0).getY()+1);
        this.position.get(1).setPoint(this.position.get(1).getX(),this.position.get(1).getY()+1);
        this.position.get(2).setPoint(this.position.get(2).getX(),this.position.get(2).getY()+1);
        this.position.get(3).setPoint(this.position.get(3).getX(),this.position.get(3).getY()+1);
        drawRobotOnBoard(board);

        eraseRobotMap();
        this.mapPosition.get(0).setPoint(this.mapPosition.get(0).getX(),this.mapPosition.get(0).getY()+1);
        this.mapPosition.get(1).setPoint(this.mapPosition.get(1).getX(),this.mapPosition.get(1).getY()+1);
        this.mapPosition.get(2).setPoint(this.mapPosition.get(2).getX(),this.mapPosition.get(2).getY()+1);
        this.mapPosition.get(3).setPoint(this.mapPosition.get(3).getX(),this.mapPosition.get(3).getY()+1);
        drawRobotOnMap();
    }



    public double work(int[][] board){
        findFirstWall(board);
        followWall(board);
        remakeMap();
        while(!isMapDone()) {
            Graph graph = new Graph(myMap, mapSide);
            validateGraph(graph, board);
            Vertex robotPosition = graph.findRobotPosition();
            PathFind pathFind = new PathFind(graph.getGraphMap());
            ArrayList<Integer> path = pathFind.findPathToClosestZero(robotPosition);
            for (int i = 1; i < path.size(); i++) {
                graph = new Graph(myMap, mapSide);
                robotPosition = graph.findRobotPosition();
                makeStepFormPath(path.get(i), robotPosition.getNumber(), board);
                if (handSteering) {
                    break;
                }
            }
        }
        printMap();
        return test(board);
    }
    private void setFollowingWallStartPosition(){
        this.followingWallStartPosition.add(new Point (this.mapPosition.get(0).getX(),this.mapPosition.get(0).getY()));
        this.followingWallStartPosition.add(new Point (this.mapPosition.get(1).getX(),this.mapPosition.get(1).getY()));
        this.followingWallStartPosition.add(new Point (this.mapPosition.get(2).getX(),this.mapPosition.get(2).getY()));
        this.followingWallStartPosition.add(new Point (this.mapPosition.get(3).getX(),this.mapPosition.get(3).getY()));
    }
    private void findFirstWall(int[][] board){
        boolean canISeeWall = false;
        int frontState;
        while(!canISeeWall){
            frontState = checkFront(board);
            switch (frontState){
                case 1:
                    frontCase1(board);
                    canISeeWall=true;
                    break;
                case 2:
                    frontCase2(board);
                    canISeeWall=true;
                    break;
                case 3:
                    frontCase3(board);
                    canISeeWall=true;
                    break;
                case 4:
                    frontCase4(board);
                    canISeeWall=true;
                    break;
                case 5:
                    frontCase5(board);
                    canISeeWall=true;
                    break;
                case 6:
                    frontCase6(board);
                    canISeeWall=true;
                    break;
                case 7:
                    frontCase7(board);
                    canISeeWall=true;
                    break;
                case 8:
                    frontCase8(board);
                    canISeeWall=true;
                    break;
                case 9:
                    frontCase9(board);
                    break;
                case 10:
                    frontCase10(board);
                    canISeeWall=true;
                    break;
                case 11:
                    frontCase11(board);
                    canISeeWall=true;
                    break;
                case 12:
                    frontCase12(board);
                    canISeeWall=true;
                    break;
                default:
                    frontDefault(board);
                    break;
            }
        }
        setFollowingWallStartPosition();
    }
    private void followWall(int[][] board){
        setFollowingWallStartPosition();
        int frontState;
        boolean haveIFinished=false;
        while(!haveIFinished){
            frontState = checkFront(board);
            switch (frontState){
                case 1:
                    haveIFinished=frontCase1b(board);
                    break;
                case 2:
                    haveIFinished=frontCase2b(board);
                    break;
                case 3:
                    haveIFinished=frontCase3b(board);
                    break;
                case 4:
                    haveIFinished=frontCase4b(board);
                    break;
                case 5:
                    haveIFinished=frontCase5b(board);
                    break;
                case 6:
                    haveIFinished=frontCase6b(board);
                    break;
                case 7:
                    haveIFinished=frontCase7b(board);
                    break;
                case 8:
                    haveIFinished=frontCase8b(board);
                    break;
                case 9:
                    haveIFinished=frontCase9b(board);
                    break;
                case 10:
                    haveIFinished=frontCase10b(board);
                    break;
                case 11:
                    haveIFinished=frontCase11b(board);
                    break;
                case 12:
                    haveIFinished=frontCase12b(board);
                    break;
                default:
                    haveIFinished=frontDefaultb(board);
                    break;
            }
            //isMappingWallFinished=haveIMappedAllWalls();
        }
    }
    private void makeStepFormPath (int step, int position, int[][] board){
        int sub = position-step;
        setDirection(sub,board);
        moveForward(board);
        moveForward(board);
    }
    private void setDirection(int sub, int[][] board){
        int pathDirection = checkPathDirection(sub);
        switch (this.facingDirection-pathDirection){
            case -3:
                rotateLeft(board);
                break;
            case -2:
                rotateRight(board);
                rotateRight(board);
                break;
            case -1:
                rotateRight(board);
                break;
            case 0:
                break;
            case 1:
                rotateLeft(board);
                break;
            case 2:
                rotateRight(board);
                rotateRight(board);
                break;
            case 3:
                rotateRight(board);
                break;
        }
    }
    private int checkPathDirection(int sub){
        switch (sub){
            case 50:
                return 0;
            case -1:
                return 1;
            case -50:
                return 2;
            case 1:
                return 3;
        }
        return -1;
    }



    private void whatCanISee(int[][] board){
        switch (this.facingDirection){
            case 0:
                whatCanISeeUpwards(board);
                break;
            case 1:
                whatCanISeeRight(board);
                break;
            case 2:
                whatCanISeeDownwards(board);
                break;
            case 3:
                whatCanISeeLeft(board);
                break;
        }
    }
    private void whatCanISeeUpwards(int[][] board){
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(scanLUDiagonal(position.get(0),mapPosition.get(0),board));
        visiblePoints.addAll(scanRUDiagonal(position.get(1),mapPosition.get(1),board));
        for(int i=0;i<visiblePoints.size();i++){
            myMap[visiblePoints.get(i).getX()][visiblePoints.get(i).getY()]=visiblePoints.get(i).getValue();
        }
    }
    private void whatCanISeeDownwards(int[][] board){
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(scanLLDiagonal(position.get(1),mapPosition.get(1),board));
        visiblePoints.addAll(scanRLDiagonal(position.get(0),mapPosition.get(0),board));
        for(int i=0;i<visiblePoints.size();i++){
            myMap[visiblePoints.get(i).getX()][visiblePoints.get(i).getY()]=visiblePoints.get(i).getValue();
        }
    }
    private void whatCanISeeRight(int[][] board){
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(scanRUDiagonal(position.get(0),mapPosition.get(0),board));
        visiblePoints.addAll(scanRLDiagonal(position.get(1),mapPosition.get(1),board));
        for(int i=0;i<visiblePoints.size();i++){
            myMap[visiblePoints.get(i).getX()][visiblePoints.get(i).getY()]=visiblePoints.get(i).getValue();
        }
    }
    private void whatCanISeeLeft(int[][] board){
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(scanLLDiagonal(position.get(0),mapPosition.get(0),board));
        visiblePoints.addAll(scanLUDiagonal(position.get(1),mapPosition.get(1),board));
        for(int i=0;i<visiblePoints.size();i++){
            myMap[visiblePoints.get(i).getX()][visiblePoints.get(i).getY()]=visiblePoints.get(i).getValue();
        }
    }



    private ArrayList<Point> scanUpper(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
    ArrayList<Point> visiblePoints = new ArrayList<>();
    int x = scanStartPointOnBoard.getX();
    int y = scanStartPointOnBoard.getY();
    int xOnMap = scanStartPointOnMap.getX();
    int yOnMap = scanStartPointOnMap.getY();
    int value;
    for(int i=1;i<5;i++){
        value = board[x-i][y];
        visiblePoints.add(new Point(xOnMap-i,yOnMap,value));
        if(value==2){
            break;
        }
    }
    return visiblePoints;
}
    private ArrayList<Point> scanLower(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = new ArrayList<>();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        int value;
        for(int i=1;i<5;i++){
            value = board[x+i][y];
            visiblePoints.add(new Point(xOnMap+i,yOnMap,value));
            if(value==2){
                break;
            }
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanRight(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = new ArrayList<>();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        int value;
        for(int i=1;i<5;i++){
            value = board[x][y+i];
            visiblePoints.add(new Point(xOnMap,yOnMap+i,value));
            if(value==2){
                break;
            }
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanLeft(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = new ArrayList<>();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        int value;
        for(int i=1;i<5;i++){
            value = board[x][y-i];
            visiblePoints.add(new Point(xOnMap,yOnMap-i,value));
            if(value==2){
                break;
            }
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanLeftUpper(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanLeft(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x,y-i);
            Point mapPoint = new Point(xOnMap,yOnMap-i);
            visiblePoints.addAll(scanUpper(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanRightUpper(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanRight(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x,y+i);
            Point mapPoint = new Point(xOnMap,yOnMap+i);
            visiblePoints.addAll(scanUpper(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanLeftLower(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanLeft(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x,y-i);
            Point mapPoint = new Point(xOnMap,yOnMap-i);
            visiblePoints.addAll(scanLower(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanRightLower(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanRight(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x,y+i);
            Point mapPoint = new Point(xOnMap,yOnMap+i);
            visiblePoints.addAll(scanLower(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanLowerLeft(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanLower(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x+i,y);
            Point mapPoint = new Point(xOnMap+i,yOnMap);
            visiblePoints.addAll(scanLeft(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanUpperLeft(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanUpper(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x-i,y);
            Point mapPoint = new Point(xOnMap-i,yOnMap);
            visiblePoints.addAll(scanLeft(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanLowerRight(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanLower(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x+i,y);
            Point mapPoint = new Point(xOnMap+i,yOnMap);
            visiblePoints.addAll(scanRight(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanUpperRight(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> visiblePoints = scanUpper(scanStartPointOnBoard,scanStartPointOnMap,board);
        if(visiblePoints.size()==4){
            visiblePoints.remove(3);
        }
        int size = visiblePoints.size();
        int x = scanStartPointOnBoard.getX();
        int y = scanStartPointOnBoard.getY();
        int xOnMap = scanStartPointOnMap.getX();
        int yOnMap = scanStartPointOnMap.getY();
        for(int i=0;i<=size;i++){
            Point boardPoint = new Point(x-i,y);
            Point mapPoint = new Point(xOnMap-i,yOnMap);
            visiblePoints.addAll(scanRight(boardPoint,mapPoint,board));
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanLUDiagonal(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> leftUpper = scanLeftUpper(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> upperLeft = scanUpperLeft(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(leftUpper);
        for(int i=0;i<upperLeft.size();i++){
            if(!visiblePoints.contains(upperLeft.get(i))){
                visiblePoints.add(upperLeft.get(i));
            }
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanRUDiagonal(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> rightUpper = scanRightUpper(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> upperRight = scanUpperRight(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(rightUpper);
        for(int i=0;i<upperRight.size();i++){
            if(!visiblePoints.contains(upperRight.get(i))){
                visiblePoints.add(upperRight.get(i));
            }
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanLLDiagonal(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> leftLower = scanLeftLower(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> lowerLeft = scanLowerLeft(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(leftLower);
        for(int i=0;i<lowerLeft.size();i++){
            if(!visiblePoints.contains(lowerLeft.get(i))){
                visiblePoints.add(lowerLeft.get(i));
            }
        }
        return visiblePoints;
    }
    private ArrayList<Point> scanRLDiagonal(Point scanStartPointOnBoard, Point scanStartPointOnMap, int[][] board){
        ArrayList<Point> rightLower = scanRightLower(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> lowerRight = scanLowerRight(scanStartPointOnBoard,scanStartPointOnMap,board);
        ArrayList<Point> visiblePoints = new ArrayList<>();
        visiblePoints.addAll(rightLower);
        for(int i=0;i<lowerRight.size();i++){
            if(!visiblePoints.contains(lowerRight.get(i))){
                visiblePoints.add(lowerRight.get(i));
            }
        }
        return visiblePoints;
    }



    private ArrayList<Point> setFront(){
        ArrayList<Point> front = new ArrayList<>();
        int x = position.get(0).getX();
        int y = position.get(0).getY();
        switch (this.facingDirection){
            case 0:
                for(int i=0;i<4;i++){
                    front.add(new Point(x-2,y-1+i));
                }
                front.add(new Point(x,y+3));
                front.add(new Point(x+1,y+3));
                break;
            case 1:
                for(int i=0;i<4;i++){
                    front.add(new Point(x-1+i,y+2));
                }
                front.add(new Point(x+3,y));
                front.add(new Point(x+3,y-1));
                break;
            case 2:
                for(int i=0;i<4;i++){
                    front.add(new Point(x+2,y+1-i));
                }
                front.add(new Point(x,y-3));
                front.add(new Point(x-1,y-3));
                break;
            case 3:
                for(int i=0;i<4;i++){
                    front.add(new Point(x+1-i,y-2));
                }
                front.add(new Point(x-3,y));
                front.add(new Point(x-3,y+1));
                break;
        }
        return front;
    }
    private int checkFront(int[][] board){
        ArrayList<Point> front = setFront();
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==1 &&
                pointToBoardValue(front.get(2),board)==1 && pointToBoardValue(front.get(3),board)==2){
            return 1;
        }
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==1 &&
                pointToBoardValue(front.get(2),board)==2 && pointToBoardValue(front.get(3),board)==2){
            return 2;
        }
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==2 &&
                pointToBoardValue(front.get(2),board)==2 && pointToBoardValue(front.get(3),board)==1){
            return 3;
        }
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==2 &&
                pointToBoardValue(front.get(2),board)==2 && pointToBoardValue(front.get(3),board)==2){
            return 4;
        }
        if(pointToBoardValue(front.get(0),board)==2 && pointToBoardValue(front.get(1),board)==2 &&
                pointToBoardValue(front.get(2),board)==2 && pointToBoardValue(front.get(3),board)==1){
            return 5;
        }
        if(pointToBoardValue(front.get(0),board)==2 && pointToBoardValue(front.get(1),board)==2 &&
                pointToBoardValue(front.get(2),board)==1 && pointToBoardValue(front.get(3),board)==1){
            return 6;
        }
        if(pointToBoardValue(front.get(0),board)==2 && pointToBoardValue(front.get(1),board)==1 &&
                pointToBoardValue(front.get(2),board)==1 && pointToBoardValue(front.get(3),board)==1){
            return 7;
        }
        if(pointToBoardValue(front.get(0),board)==2 && pointToBoardValue(front.get(1),board)==2 &&
                pointToBoardValue(front.get(2),board)==2 && pointToBoardValue(front.get(3),board)==2){
            return 8;
        }
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==1 &&
                pointToBoardValue(front.get(2),board)==1 && pointToBoardValue(front.get(3),board)==1 &&
                pointToBoardValue(front.get(4),board)==1 && pointToBoardValue(front.get(5),board)==1 ){
            return 9;
        }
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==1 &&
                pointToBoardValue(front.get(2),board)==1 && pointToBoardValue(front.get(3),board)==1 &&
                pointToBoardValue(front.get(4),board)==2 && pointToBoardValue(front.get(5),board)==2 ){
            return 10;
        }
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==1 &&
                pointToBoardValue(front.get(2),board)==1 && pointToBoardValue(front.get(3),board)==1 &&
                pointToBoardValue(front.get(4),board)==1 && pointToBoardValue(front.get(5),board)==2 ){
            return 11;
        }
        if(pointToBoardValue(front.get(0),board)==1 && pointToBoardValue(front.get(1),board)==1 &&
                pointToBoardValue(front.get(2),board)==1 && pointToBoardValue(front.get(3),board)==1 &&
                pointToBoardValue(front.get(4),board)==2 && pointToBoardValue(front.get(5),board)==1 ){
            return 12;
        }
        return 0;
    }
    private boolean checkFrontOnWallMap(){
        ArrayList<Point> front = setFront();
        for(int i=0;i<6;i++){
            if(wallMap[front.get(i).getX()][front.get(i).getY()]!=5){
                return true;
            }
        }
        return false;
    }
    private boolean frontCase1b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        moveForward(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        rotateRight(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        moveForward(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase1(int[][] board){
        rotateLeft(board);
        moveForward(board);
        rotateRight(board);
    }
    private boolean frontCase2b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        moveForward(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        moveForward(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        rotateRight(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        moveForward(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase2(int[][] board){
        rotateLeft(board);
        moveForward(board);
        moveForward(board);
        rotateRight(board);
        moveForward(board);
    }
    private boolean frontCase3b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase3(int[][] board){
        rotateLeft(board);
    }
    private boolean frontCase4b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase4(int[][] board){
        rotateLeft(board);
    }
    private boolean frontCase5b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase5(int[][] board){
        rotateLeft(board);
    }
    private boolean frontCase7b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase7(int[][] board){
        rotateLeft(board);
    }
    private boolean frontCase6b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase6(int[][] board){
        rotateLeft(board);
    }
    private boolean frontCase8b(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase8(int[][] board){
        rotateLeft(board);
    }
    private boolean frontCase9b(int[][] board){
        boolean finish;
        moveForward(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase9(int[][] board){
        moveForward(board);
    }
    private boolean frontCase10b(int[][] board){
        boolean finish;
        moveForward(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase10(int[][] board){
        moveForward(board);
    }
    private boolean frontCase11b(int[][] board){
        boolean finish;
        moveForward(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        moveForward(board);
        finish=haveIMappedAllWalls();
        if(finish){
            return true;
        }
        rotateRight(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase11(int[][] board){
        moveForward(board);
        moveForward(board);
        rotateRight(board);
    }
    private boolean  frontCase12b(int[][] board){
        boolean finish;
        moveForward(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontCase12(int[][] board){
        moveForward(board);
    }
    private boolean  frontDefaultb(int[][] board){
        boolean finish;
        rotateLeft(board);
        finish=haveIMappedAllWalls();
        return finish;
    }
    private void frontDefault(int[][] board){
        rotateLeft(board);
    }


    public void printPosition(ArrayList<Point> position){
        for(int i=0;i<4;i++){
            System.out.print(position.get(i).getX()+" "+position.get(i).getY()+" * ");
        }
        System.out.println();
    }
    public void printPartialMap(){
        for(int i = mapPosition.get(0).getX()-5; i< mapPosition.get(0).getX()+5; i++){
            for(int j = mapPosition.get(0).getY()-5; j< mapPosition.get(0).getY()+5; j++){
                if(myMap[i][j]==0){
                    System.out.print(GREY +" "+myMap[i][j]+ANSI_RESET);
                }
                if(myMap[i][j]==1){
                    System.out.print(WHITE +" "+myMap[i][j]+ANSI_RESET);
                }
                if(myMap[i][j]==2){
                    System.out.print(BLUE_B+" "+myMap[i][j]+ANSI_RESET);
                }
                if(myMap[i][j]==3){
                    System.out.print(YELLOW+" "+myMap[i][j]+ANSI_RESET);
                }
                if(myMap[i][j]==4){
                    System.out.print(RED+" "+myMap[i][j]+ANSI_RESET);
                }
                if(myMap[i][j]==5){
                    System.out.print(BLUE_B+" "+myMap[i][j]+ANSI_RESET);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public void printMap(){
        for(int i = 0; i<this.mapSide; i++){
            for(int j = 0; j<this.mapSide; j++){
                if(myMap[i][j]==0){
                    System.out.print(GREY +"  "+ANSI_RESET);
                }
                if(myMap[i][j]==1){
                    System.out.print(WHITE +"  "+ANSI_RESET);
                }
                if(myMap[i][j]==2){
                    System.out.print(BLUE_B+"  "+ANSI_RESET);
                }
                if(myMap[i][j]==3){
                    System.out.print(YELLOW+"  "+ANSI_RESET);
                }
                if(myMap[i][j]==4){
                    System.out.print(RED+"  "+ANSI_RESET);
                }
                if(myMap[i][j]==5){
                    System.out.print(BLUE_B+"  "+ANSI_RESET);
                }
            }
            System.out.println();
        }
    }
    //+myMap[i][j]+
    private int printMenu(){
        System.out.println("Choose action to do: ");
        System.out.println("\t 1 -> move forward");
        System.out.println("\t 2 -> move backward");
        System.out.println("\t 3 -> rotate left");
        System.out.println("\t 4 -> rotate right");
        System.out.println("\t 5 -> find wall");
        System.out.println("\t 6 -> follow wall (robot must be positioned next to the wall)");
        System.out.println("\t 0 -> quit");
        return scanner.nextInt();

    }
    private void remakeMap(){
        for(int i=0;i<mapSide;i++){
            for(int j=0;j<mapSide;j++){
                if(myMap[i][j]==2){
                    wallMap[i][j]=5;
                }
            }
        }
    }
    private int pointToBoardValue(Point point, int[][] board){
        return board[point.getX()][point.getY()];
    }
    private boolean isPointAWall(Point point, int[][] board){
        if(pointToBoardValue(point, board)==2){
            return true;
        }
        return false;
    }


    private double test(int[][] board){
        int twoBoard=0;
        int oneBoard=0;
        int twoMap=0;
        int oneMap=0;
        for(int i=0;i<mapSide/2;i++){
            for(int j=0;j<mapSide/2;j++){
                if(board[i][j]==1){
                    oneBoard++;
                }
                else if(board[i][j]==2){
                    twoBoard++;
                }
            }
        }
        for(int i=0;i<mapSide;i++){
            for(int j=0;j<mapSide;j++){
                if(myMap[i][j]==1){
                    oneMap++;
                }
                else if(myMap[i][j]==2){
                    twoMap++;
                }
            }
        }
//        System.out.println("Obstacles in enviroment: "+twoBoard);
//        System.out.println("Free spaces in enviroment: "+oneBoard);
//        System.out.println("Obstacles in map: "+twoMap);
//        System.out.println("Free spaces in map: "+oneMap);
        int mapSum = twoMap + oneMap;
        int boardSum = twoBoard + oneBoard;
//        System.out.println("mapsSum: " + mapSum);
//        System.out.println("boardSum: " + boardSum);
        return ((double)mapSum)/boardSum*100;
//        System.out.println("Effeciency: " + result + "%");
    }


}
