package com.Szymon;

import java.util.ArrayList;

public class Graph {

    private static final String BLUE_B = "\u001B[44m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String GREY = "\033[1;97m";
    private static final String RED = "\033[55;41m";
    private static final String YELLOW = "\033[43m";
    private static final String WHITE = "\033[47m";


    private int mapSide;
    private Vertex[][] graphMap;

    public Graph(int[][] map, int mapSide){
        this.mapSide = mapSide;
        this.graphMap = fillGraph(map, mapSide);
    }
    private Vertex[][] fillGraph(int[][] map, int mapSide){
        Vertex[][] graphMap = new Vertex[mapSide/2][mapSide/2];
        int number=0;
        for(int i=0;i<mapSide/2;i++){
            for(int j=0;j<mapSide/2;j++){
                Point p1 = new Point(i*2,j*2);
                Point p2 = new Point(i*2+1,j*2);
                Point p3 = new Point(i*2+1,j*2);
                Point p4 = new Point(i*2+1,j*2+1);
                graphMap[i][j] = new Vertex(p1,p2,p3,p4,map,number,i,j);
                number++;
            }
        }
        return graphMap;
    }
    public void narrowGraph(){
        ArrayList<Vertex> edges = new ArrayList<>();
        for(int i=0;i<mapSide/2;i++){
            for(int j=0;j<mapSide/2;j++){
                //System.out.println(graphMap[i][j].getNumber());
                if(graphMap[i][j].getVertexValue()==2){
                    //System.out.println(graphMap[i][j].getX()+" "+graphMap[i][j].getY());
                    edges.add(new Vertex(graphMap[i][j]));
                }
            }
        }
        //System.out.println(edges.size());
        for(int i=0;i<edges.size();i++){
            expandWall(edges.get(i));
        }
    }
    private void expandWall(Vertex wall){
        int x = wall.getX();
        int y = wall.getY();
        //System.out.println(x+" "+y);
        if(this.graphMap[x+1][y].getNumber()==1){
            this.graphMap[x+1][y].setValue(2);
        }
        if(this.graphMap[x-1][y].getNumber()==1){
            this.graphMap[x-1][y].setValue(2);
        }
        if(this.graphMap[x][y+1].getNumber()==1){
            this.graphMap[x][y+1].setValue(2);
        }
        if(this.graphMap[x][y-1].getNumber()==1){
            this.graphMap[x][y-1].setValue(2);
        }
    }
    public boolean validateGraph() {
        int robotCounter = 0;
        for (int i=0;i<mapSide/2;i++) {
            for(int j=0;j<mapSide/2;j++){
                if(graphMap[i][j].getVertexValue()==4){
                    robotCounter++;
                }
                if(robotCounter==2){
                    return false;
                }
            }
        }
        return true;
    }
    public void adjustGraph(int[][] map, int mapSide){
        Vertex[][] graphMap = new Vertex[mapSide/2][mapSide/2];
        int number=0;
        for(int i=0;i<mapSide/2;i++){
            for(int j=0;j<mapSide/2;j++){
                Point p1 = new Point(i*2,j*2);
                Point p2 = new Point(i*2+1,j*2);
                Point p3 = new Point(i*2+1,j*2);
                Point p4 = new Point(i*2+1,j*2+1);
                graphMap[i][j] = new Vertex(p1,p2,p3,p4,map,number,i,j);
                number++;
            }
        }
        this.graphMap = graphMap;
    }
    public void printGraph(){
        for(int i=0;i<mapSide/2;i++){
            for(int j=0;j<mapSide/2;j++){
                if(graphMap[i][j].getVertexValue()==0){
                    System.out.print(GREY +"  "+ANSI_RESET);
                }
                if(graphMap[i][j].getVertexValue()==1){
                    System.out.print(WHITE +"  "+ANSI_RESET);
                }
                if(graphMap[i][j].getVertexValue()==2){
                    System.out.print(BLUE_B+"  "+ANSI_RESET);
                }
                if(graphMap[i][j].getVertexValue()==3){
                    System.out.print(YELLOW+"  "+ANSI_RESET);
                }
                if(graphMap[i][j].getVertexValue()==4){
                    System.out.print(RED+"  "+ANSI_RESET);
                }
            }
            System.out.println();
        }
    }
    public Vertex findClosestUnmapedArea(){
        ArrayList<Vertex> zeros = findZeros();
        Vertex robotPosition = findRobotPosition();
        int smallestDistanceIndex = 0;
        int smallestDistance = calculateDistance(robotPosition,zeros.get(0));
        for(int i=1;i<zeros.size();i++){
            if(calculateDistance(robotPosition,zeros.get(i))<smallestDistance){
                smallestDistanceIndex=i;
            }
        }
        int x = zeros.get(smallestDistanceIndex).getX();
        int y = zeros.get(smallestDistanceIndex).getY();
        graphMap[x][y].setValue(3);
        return zeros.get(smallestDistanceIndex);
    }
    private ArrayList<Vertex> findZeros(){
        ArrayList<Vertex> zeros = new ArrayList<>();
        for(int i=0;i<mapSide/2;i++){
            for(int j=0;j<mapSide/2;j++){
                if(graphMap[i][j].getVertexValue()==0){
                    zeros.add(graphMap[i][j]);
                }
            }
        }
        System.out.println(zeros.size());
        return zeros;
    }
    public Vertex findRobotPosition(){
        for(int i=0;i<mapSide/2;i++){
            for(int j=0;j<mapSide/2;j++){
                if(graphMap[i][j].getVertexValue()==4){
                    return graphMap[i][j];
                }
            }
        }
        return null;
    }
    private int calculateDistance(Vertex robot, Vertex zero){
        int robotX = robot.getX();
        int robotY = robot.getY();
        int zeroX = zero.getX();
        int zeroY = zero.getY();
        return (int) Math.sqrt((robotX-zeroX)*(robotX-zeroX) + (robotY-zeroY)*(robotY-zeroY));
    }

    public Vertex[][] getGraphMap() {
        return graphMap;
    }
}
