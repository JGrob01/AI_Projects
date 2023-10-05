//
// Created by John Grobaker
//
import java.util.*;

public class AStar {
  /**
  * runAStar will run the A Star algorithm and find the shortest path from a start node to an end node
  * startingID: The start node provided by you
  * endingID: The goal node provided by you
  * edgeWeights: A 2D array, formated like a grid to easily find the weights
  * edgeWeightsList: A list version that tells what nodes are connected to the current node
  * hCosts: 2D array, formated like a grid to easily find the h costs of nodes
  */
  public boolean runAStar(int startingID, int endingID, double[][] edgeWeights, ArrayList<LinkedList<Integer>> edgeWeightsList, double[][] hCosts) {
    LinkedHashMap<ArrayList<Integer>, Double> pathMap = new LinkedHashMap<ArrayList<Integer>, Double>();  //this map with hold all paths visited and their respective costs
    ArrayList<Integer> visitedList = new ArrayList<Integer>();                                            //this array list will hold nodes that have already been visited          
    double f = -1;
    
    ArrayList<Integer> tempList = new ArrayList<Integer>();
    tempList.add(startingID);
    pathMap.put(tempList, hCosts[startingID-1][endingID-1]);                                           //First add the starting and node and its path to the map
    
    while (pathMap.size() != 0) {                                                                      //Keep going until the map has no paths left
      ArrayList<Integer> currentPath = new ArrayList<Integer>(); 
      double fMin = Integer.MAX_VALUE;
      
      for (Map.Entry<ArrayList<Integer>, Double> entry : pathMap.entrySet()) {                         //Iterate through the map to find the least costly path to now follow
        if(fMin > entry.getValue()) {                                            
          fMin = entry.getValue();
          currentPath = entry.getKey();
        }
      }  
      
      int currentNode = currentPath.get(currentPath.size() - 1);                
      
      if(currentNode == endingID){                                                                      //Checker to see if the least cost path's end node is our goal node                              
        printPath(currentPath, pathMap.get(currentPath));
        return true;
      }
      
      visitedList.add(currentNode);
      
      for (int i = 0; i < edgeWeightsList.get(currentNode-1).size(); i++) {                             //Go to each child node of the current node and find their f value,            
        int childNode = edgeWeightsList.get(currentNode-1).get(i); 
        ArrayList<Integer> tempPath = new ArrayList<>(currentPath);
        tempPath.add(childNode);
        
        if(!(pathMap.containsKey(tempPath) || visitedList.indexOf(childNode) != -1)) {                  //Check to see if they are already visited or in the map already
          f = edgeWeights[currentNode-1][childNode-1] + hCosts[childNode-1][endingID-1] + (pathMap.get(currentPath) - hCosts[currentNode-1][endingID-1]); 
          pathMap.put(tempPath, f);                                                                     //Calculate their f value and add them to the map
        }
      }
      pathMap.remove(currentPath, fMin);
    }
    return false;
  }
  /**
   * printPath will print the final minimum path from the start node to the end node
   * path: an array list that holds the path from the start node to the goal node
   * cost: double value that holds the cost to get to the goal node
   */
  public void printPath(ArrayList<Integer> path, double cost) {
    System.out.println("A* minimum cost path");
    System.out.print("["+cost+"] ");
    for (int i = 0; i < path.size()-1; i++) {
      System.out.print(path.get(i) + " - ");
    }
    System.out.println(path.get(path.size() - 1));
  }
}
