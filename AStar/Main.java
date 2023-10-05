//
// Created by John Grobaker
//
import java.util.ArrayList;
import java.util.LinkedList;

class Main {
  
  /**
   * main method to start the code
   */
  public static void main(String[] args) {
    Main m = new Main();
    ReadInput r = new ReadInput();
    CreateGrids g = new CreateGrids();
    CreateList l = new CreateList();
    AStar a = new AStar();
    m.mainLoop(r, g, l, a);
  }

  /**
   * mainLoop will read in the files, add them to the appropriate structures
   *  then use A* to find the minimum cost path
   */
  public void mainLoop(ReadInput r, CreateGrids g, CreateList l, AStar a){
    double[][] edgeWeights = new double[200][200];
    ArrayList<LinkedList<Integer>> edgeWeightsList = new ArrayList<>();  
    double[][] hCosts = new double[200][200];
    
    String [] inputs = r.readInput();
    edgeWeights = g.createGrids(inputs[0], 0);
    edgeWeightsList = l.createEdgeWeightList(inputs[0]);
    hCosts = g.createGrids(inputs[1], 1);
    
    boolean didFind = a.runAStar(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]), edgeWeights, edgeWeightsList, hCosts);
    if(!didFind)
      System.out.println("Could not find a path from: " + Integer.parseInt(inputs[2]) + " to "+ Integer.parseInt(inputs[3]));
  }
}