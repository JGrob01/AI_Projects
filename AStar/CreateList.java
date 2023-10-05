//
// Created by John Grobaker
//
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CreateList {
  private ArrayList<LinkedList<Integer>> edgeWeights;  
  private int currentIndex;
  
  /**
   * createEdgeWeightList will take the file given and parse through it, creating an array list that will hold the paths hrough each node
   * fileName: a string that hold the name of the file to be used
   */
  public ArrayList<LinkedList<Integer>> createEdgeWeightList(String fileName) {
    edgeWeights = new ArrayList<>();
    currentIndex = 1;
    LinkedList<Integer> tempList = new LinkedList<>();
    
    try {
      Scanner fileReader = new Scanner(new File(fileName)); 
      String[] tempLine = fileReader.next().split(","); 
      
      while (fileReader.hasNext()) { 
        tempLine = fileReader.next().split(",");   
        
        try {  
          int checker = Integer.parseInt(tempLine[0]);
        } catch(NumberFormatException e){  
          return edgeWeights;  
        } 
        
        if(Integer.parseInt(tempLine[0]) == currentIndex) {     //because of the format of the file, make sure that each node has a path through all node 
          tempList.add(Integer.parseInt(tempLine[1]));          //ex. node 1's connected nodes are on different lines so make sure 1 is filled out before moving on to 2
        } else {
          currentIndex++;
          edgeWeights.add(tempList);
          tempList = new LinkedList<>();
          tempList.add(Integer.parseInt(tempLine[1]));
        }
      }  
      edgeWeights.add(tempList);
      fileReader.close();
    } catch (IOException e) {
      e.printStackTrace();  
    } 
    
    //printList();
    return edgeWeights;
  }
  
  /**
   * printList helper function to print out the list
   */
  public void printList() {                                  
    for(LinkedList<Integer> currentList : edgeWeights) {                        
      for(int index : currentList) {
        System.out.print(index + " ");
      }
      System.out.println();
    }
  }
}
