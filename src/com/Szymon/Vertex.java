package com.Szymon;

public class Vertex {
    private Point point1;
    private Point point2;
    private Point point3;
    private Point point4;
    private int x;
    private int y;
    private int vertexValue;
    private int number;
    private boolean visited;

    public Vertex(Point point1, Point point2, Point point3, Point point4, int[][] map, int number, int x, int y){
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.number = number;
        this.visited = false;
        this.x=x;
        this.y=y;
        setValue(map);
    }
    public Vertex(Vertex vertex){
        this.point1 = vertex.getPoint1();
        this.point2 = vertex.getPoint2();
        this.point3 = vertex.getPoint3();
        this.point4 = vertex.getPoint4();
        this.number = vertex.getNumber();
        this.visited = vertex.isVisited();
        this.x=vertex.getX();
        this.y=vertex.getY();
        this.vertexValue = vertex.getVertexValue();
    }

    private void setValue(int[][] map){
        if(checkValue(map,point1)==2 || checkValue(map,point2)==2 || checkValue(map,point3)==2 || checkValue(map,point4)==2){
            this.vertexValue = 2;
        }
        else if(checkValue(map,point1)==0 || checkValue(map,point2)==0 || checkValue(map,point3)==0 || checkValue(map,point4)==0){
            this.vertexValue = 0;
        }
        else if(checkValue(map,point1)==1 && checkValue(map,point2)==1 && checkValue(map,point3)==1 && checkValue(map,point4)==1){
            this.vertexValue = 1;
        }
        else{
            this.vertexValue = 4;
        }
    }
    public void setValue(int value){
        this.vertexValue = value;
    }

    private int checkValue(int[][] map, Point point){
        int x = point.getX();
        int y = point.getY();
        if(map[x][y]==0){
            return 0;
        }
        if(map[x][y]==1){
            return 1;
        }
        if(map[x][y]==2){
            return 2;
        }
        return 4;
    }
    public int getVertexValue() {
        return vertexValue;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getNumber() {
        return number;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }


    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public Point getPoint3() {
        return point3;
    }

    public Point getPoint4() {
        return point4;
    }

    public boolean isVisited() {
        return visited;
    }
}
