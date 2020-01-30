package com.Szymon;

import java.util.ArrayList;

public class PathFind {

    private ArrayList<Integer> path;
    private Vertex[][] graph;
    private int graphSize;

    public PathFind(Vertex[][] graph){
        this.graph = graph;
        this.graphSize = graph.length;
        this.path = new ArrayList<>();
    }

    public ArrayList<Integer> findPathToClosestZero(Vertex robotPosition){
        ArrayList<Vertex> start = new ArrayList<>();
        start.add(robotPosition);
        moveToUnvisitedNeighbour(start);
        return reversePath();
    }
    private ArrayList<Integer> reversePath(){
        ArrayList<Integer> reversePath = new ArrayList<>();
        for(int i=path.size()-1;i>=0;i--){
            reversePath.add(path.get(i));
        }
        return reversePath;
    }
    private int moveToUnvisitedNeighbour(ArrayList<Vertex> start){
        ArrayList<Vertex> source = new ArrayList<>();
        ArrayList<Vertex> allNeighbours = new ArrayList<>();
        for(int i=0;i<start.size();i++){
            if(start.get(i).getVertexValue()==0){
                path.add(start.get(i).getNumber());
                return start.get(i).getNumber();
            }
        }
        if(!areThereUnvisitedLeft()){
            return -1;
        }
        for(int i=0;i<start.size();i++){
            ArrayList<Vertex> myNeighbours = findUnvisitedNeighbours(start.get(i));
            for(int j=0;j<myNeighbours.size();j++){
                source.add(start.get(i));
                myNeighbours.get(j).setVisited(true);
                int x = myNeighbours.get(j).getX();
                int y = myNeighbours.get(j).getY();
                graph[x][y].setVisited(true);
            }
            allNeighbours.addAll(myNeighbours);
        }
        int vertexNumber=moveToUnvisitedNeighbour(allNeighbours);
        for(int i=0;i<allNeighbours.size();i++){
            if(allNeighbours.get(i).getNumber()==vertexNumber){
                path.add(source.get(i).getNumber());
                return source.get(i).getNumber();
            }
        }
        return -1;
    }
    private ArrayList<Vertex> findUnvisitedNeighbours(Vertex start){
        ArrayList<Vertex> neighbours = new ArrayList<>();
        int x = start.getX();
        int y = start.getY();
        if(!graph[x-1][y].isVisited() && (graph[x-1][y].getVertexValue()==1 || graph[x-1][y].getVertexValue()==0)){
            neighbours.add(new Vertex(graph[x-1][y]));
        }
        if(!graph[x+1][y].isVisited() && (graph[x+1][y].getVertexValue()==1 || graph[x+1][y].getVertexValue()==0)){
            neighbours.add(new Vertex(graph[x+1][y]));
        }
        if(!graph[x][y-1].isVisited() && (graph[x][y-1].getVertexValue()==1 || graph[x][y-1].getVertexValue()==0)){
            neighbours.add(new Vertex(graph[x][y-1]));
        }
        if(!graph[x][y+1].isVisited() && (graph[x][y+1].getVertexValue()==1 || graph[x][y+1].getVertexValue()==0)){
            neighbours.add(new Vertex(graph[x][y+1]));
        }
        return neighbours;
    }
    private boolean areThereUnvisitedLeft(){
        for(int i=0;i<graphSize;i++){
            for(int j=0;j<graphSize;j++){
                if(!graph[i][j].isVisited() && graph[i][j].getVertexValue()==1){
                    return true;
                }
            }
        }
        return false;
    }


}
