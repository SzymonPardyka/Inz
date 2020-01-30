package com.Szymon;

import java.util.ArrayList;
import java.util.Random;

public class Circle {

    private Random rand = new Random();
    private ArrayList<Point> circle;

    private int generateCord(int parameter){
        return rand.nextInt(parameter);
    }

    public ArrayList<Point> generateCircle(Point center, int radius){
        circle = new ArrayList<>();
        addFirstQuarter(center,radius,circle);
        addSecondQuarter(center,radius,circle);
        addThirdQuarter(center,radius,circle);
        addFourthQuarter(center,radius,circle);
        return circle;
    }
    public int[] generateCenterRadius(int boardSide){
        int[] values = new int[3];
        values[0] = generateCord(boardSide-boardSide/3)+boardSide/5;
        values[1] = generateCord(boardSide-boardSide/3)+boardSide/5;
        int maxRadius = findMaxRadius(values[0],values[1],boardSide);
        //System.out.println(maxRadius);
        values[2] = generateCord(maxRadius/2)+maxRadius/2;
        return values;
    }

    private int findMaxRadius(Point center, int boardSide){
        int centerX=center.getX();
        int centerY=center.getY();
        int[] distances = new int[]{boardSide-centerX-1,boardSide-centerY-1,centerX,centerY};
        int maxRadius = distances[0];
        for(int i=0;i<distances.length;i++){
            if(distances[i]<maxRadius){
                maxRadius=distances[i];
            }
        }
        return maxRadius;
    }
    private int findMaxRadius(int centerX, int centerY, int boardSide){
        int[] distances = new int[]{boardSide-centerX-1,boardSide-centerY-1,centerX,centerY};
        int maxRadius = distances[0];
        for(int i=0;i<distances.length;i++){
            if(distances[i]<maxRadius){
                maxRadius=distances[i];
            }
        }
        return maxRadius;
    }

    private int calculateDistance(int centerX, int centerY, int pointX, int pointY){
        return ((centerX-pointX)*(centerX-pointX)+(centerY-pointY)*(centerY-pointY));
    }
    private int calculateDistance(Point center, int pointX, int pointY){
        return ((center.getX()-pointX)*(center.getX()-pointX)+(center.getY()-pointY)*(center.getY()-pointY));
    }
    private int calculateDistance(Point center, Point point){
        return ((center.getX()-point.getX())*(center.getX()-point.getX())+
                (center.getY()-point.getY())*(center.getY()-point.getY()));
    }

    private Point chooseBestPoint(Point center, Point a, Point b, Point c, int radius){
        int distanceA = calculateDistance(center,a);
        int distanceB = calculateDistance(center,b);
        int distanceC = calculateDistance(center,c);
        int radiusSquared = radius*radius;
        if(Math.abs(distanceA-radiusSquared) <= Math.abs(distanceB-radiusSquared) && Math.abs(distanceA-radiusSquared) <= Math.abs(distanceC-radiusSquared)){
            return a;
        }
        else if(Math.abs(distanceB-radiusSquared) <= Math.abs(distanceA-radiusSquared) && Math.abs(distanceB-radiusSquared) <= Math.abs(distanceC-radiusSquared)){
            return b;
        }
        else if(Math.abs(distanceC-radiusSquared) <= Math.abs(distanceA-radiusSquared) && Math.abs(distanceC-radiusSquared) <= Math.abs(distanceB-radiusSquared)){
            return c;
        }
        return null;
    }

    private void addFirstQuarter(Point center, int radius, ArrayList<Point> quarter){
        int baseX = center.getX()-radius;
        int baseY = center.getY();
        while(!(baseX==center.getX() && baseY==center.getY()+radius)){
            Point a = new Point(baseX+1,baseY);
            Point b = new Point(baseX+1,baseY+1);
            Point c = new Point(baseX,baseY+1);
            Point best = chooseBestPoint(center,a,b,c,radius);
            baseX = best.getX();
            baseY = best.getY();
            quarter.add(best);
        }
    }
    private void addSecondQuarter(Point center, int radius, ArrayList<Point> quarter){
        int baseX = center.getX();
        int baseY = center.getY()+radius;
        while(!(baseX==center.getX()+radius && baseY==center.getY())){
            Point a = new Point(baseX+1,baseY);
            Point b = new Point(baseX+1,baseY-1);
            Point c = new Point(baseX,baseY-1);
            Point best = chooseBestPoint(center,a,b,c,radius);
            baseX = best.getX();
            baseY = best.getY();
            quarter.add(best);
        }
    }
    private void addThirdQuarter(Point center, int radius, ArrayList<Point> quarter){
        int baseX = center.getX()+radius;
        int baseY = center.getY();
        while(!(baseX==center.getX() && baseY==center.getY()-radius)){
            Point a = new Point(baseX-1,baseY);
            Point b = new Point(baseX-1,baseY-1);
            Point c = new Point(baseX,baseY-1);
            Point best = chooseBestPoint(center,a,b,c,radius);
            baseX = best.getX();
            baseY = best.getY();
            quarter.add(best);
        }
    }
    private void addFourthQuarter(Point center, int radius, ArrayList<Point> quarter){
        int baseX = center.getX();
        int baseY = center.getY()-radius;
        while(!(baseX==center.getX()-radius && baseY==center.getY())){
            Point a = new Point(baseX-1,baseY);
            Point b = new Point(baseX-1,baseY+1);
            Point c = new Point(baseX,baseY+1);
            Point best = chooseBestPoint(center,a,b,c,radius);
            baseX = best.getX();
            baseY = best.getY();
            quarter.add(best);
        }
    }

    private void createFirstCircle(int[][] board, int boardSide){
        int[] values = generateCenterRadius(boardSide);
        Point center = new Point (values[0],values[1]);
        int[][] sketchBoard = new int[boardSide][boardSide];
        board[values[0]][values[1]]=1;
        ArrayList<Point> circlePoints = generateCircle(center,values[2]);
        for(int i=0;i<circlePoints.size();i++){
            sketchBoard[circlePoints.get(i).getX()][circlePoints.get(i).getY()]=2;
            if(board[circlePoints.get(i).getX()][circlePoints.get(i).getY()]==0){
                board[circlePoints.get(i).getX()][circlePoints.get(i).getY()]=2;
            }
        }
        for(int i=0;i<circlePoints.size()/2;i++){
            if(sketchBoard[circlePoints.get(i).getX()][circlePoints.get(i).getY()-1]==0){
                int border = (circlePoints.get(i).getY()-values[1])*2-1;
                for(int j=1;j<=border;j++){
                    if(board[circlePoints.get(i).getX()][circlePoints.get(i).getY()-j]!=1) {
                        board[circlePoints.get(i).getX()][circlePoints.get(i).getY() - j] = 1;
                    }
                }
            }
        }
    }

}
