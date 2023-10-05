/*****************************************************************
*
* John Grobaker *
* *
* Implement our own versions of BFS and DFS search algorithms*
* *
*****************************************************************/

import java.io.*;  
import java.util.*;
import java.io.IOException;  

class Main {
  private ArrayList<LinkedList<Integer>> adjList;                         //holds the translated csv into an adjacency list to be searched through
  private boolean foundPath = false;
  
  public static void main(String[] args) {
    Main m = new Main();
    m.mainLoop();
  }

  public void mainLoop(){
    adjList = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    boolean correctResponse = false;
    String fileName = "";
    int startingID = 0;
    int endingID = 0;
    
    while (correctResponse != true) {                                     //verifies that the given file is correct
      System.out.println("Please enter the file name and extension:");
      fileName = scanner.nextLine();
      
      if (!new File(fileName).isFile())
        System.out.println("Invalid File");
      else
        correctResponse = true;
    }
    correctResponse = false;

    while (correctResponse != true) {                                     //verifies that the given staringID is within range
      System.out.println("Start node (1 – 200):");
      startingID = (int)Double.parseDouble(scanner.nextLine());

      if (startingID <= 0 || startingID >= 201)
        System.out.println("Invalid starting ID");
      else
      correctResponse = true;
    }
    correctResponse = false;
    
    while (correctResponse != true) {                                     //verifies that the given endingID is within range
      System.out.println("End node (1 – 200):");
      endingID = (int)Double.parseDouble(scanner.nextLine());

      if (endingID <= 0 || endingID >= 201)
        System.out.println("Invalid starting ID");
      else
      correctResponse = true;
    }

    readFile(fileName);
    printAdjList();
    BFS(startingID, endingID);
    foundPath = false;
    DFS(startingID, endingID);
    
  }

  public void readFile(String fileName){	                                //will read the given file, split it, and translate it into an adjacency list to be sorted
    try {
      Scanner fileReader = new Scanner(new File(fileName)); 
      
      while (fileReader.hasNext()) {                       
        String[] tempLine = fileReader.next().split(",");    
        addToList(tempLine);                                
      }   
      fileReader.close();
      return;
    } catch (IOException e) {
      e.printStackTrace();  
    }  
  }
  
  //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  public void BFS(int startID, int endID){                                //BFS setup code; creates the queue used and an array the size of the list to check if that vertex has been visited
    int visitedNodes[] = new int[adjList.size()];
    Queue<ArrayList<Integer>> queue = new LinkedList<ArrayList<Integer>>();
    BFSTravel(startID, endID, visitedNodes, queue);
    
    if(!foundPath)                                                         //If a path could not be found print
      System.out.println("Could not find a path from: " + startID + " to "+ endID);
  }

  public void BFSTravel(int current, int endID, int visitedNodes[], Queue<ArrayList<Integer>> queue){  //BFS loop code
    ArrayList<Integer> queueArray = new ArrayList<Integer>();             //this time, store the PATHS of every node not just the nodes themselves
    queueArray.add(current);
    queue.add(queueArray);
    visitedNodes[current-1] = 1;
    
    while (queue.size() != 0) {
      queueArray = queue.poll();                                          //poll the queue with next node and its path
      current = queueArray.get(queueArray.size() - 1);                    //get the current node that the path is on
      
      if(current == endID){                                               //if the current node is the end node, stop the search and print the order of traversal                            
        foundPath = true;
        printBFSQueue(queueArray);
        return;
      }
      
      for (int i = 0; i < adjList.get(current-1).size(); i++) {             //loop through the children of the current node
        int childNode = adjList.get(current-1).get(i);                      //get the next child node and    
        if (visitedNodes[childNode-1] == 0) {                               //checks if the current child node hasn't been visited yet add its path to the queue
          visitedNodes[childNode-1] = 1;
          ArrayList<Integer> newPath = new ArrayList<>(queueArray);
          newPath.add(childNode);
          queue.add(newPath);
        }
      }
    }
  }
  
  public void DFS(int startID, int endID){                                 //DFS setup code; creates the stack used and an array the size of the list to check if that vertex has been visited
    int visitedNodes[] = new int[adjList.size()];
    Stack<Integer> stack = new Stack<>();
    DFSTravel(startID, endID, visitedNodes, stack);
    
    if(!foundPath)                                                         //If a path could not be found print
      System.out.println("Could not find a path from: " + startID + " to "+ endID);
  }

  public void DFSTravel(int current, int endingID, int visitedNodes[], Stack<Integer> stack ){ //DFS recursive code
    stack.add(current);
    
    if(current == endingID && !foundPath){                                 //if the current ID is the endindID stop the recursion and print the stack
      foundPath = true;
      printDFSStack(stack);
      return;
    } 
    visitedNodes[current-1] = 1;
      for(int i = 0; i < adjList.get(current-1).size(); i++) {              //Will loop through next child nodes, added them to the stack if they haven't been visited yet 
        int childNode = adjList.get(current-1).get(i);
        if (visitedNodes[childNode-1] == 0) {
          DFSTravel( adjList.get(current-1).get(i), endingID, visitedNodes, stack);   //will visit next child node
        }
      }
  }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  public void addToList(String[] currentLine){                            //helper method; will check to see if read in line is a valid int and adds it to the list 
    try {  
      int checker = Integer.parseInt(currentLine[0]);
    } catch(NumberFormatException e){  
      return;  
    } 

    LinkedList<Integer> tempList = new LinkedList<>();
    for(String index: currentLine){
      tempList.add(Integer.parseInt(index));
    }
    adjList.add(tempList);
    return;
  }

  public void printBFSQueue(ArrayList<Integer> queue){                    //helper method; prints the current queue of the BFS method
    System.out.println("Breadth-first Search");
    for (int i = 0; i < queue.size()-1; i++) {
      System.out.print(queue.get(i) + " - ");
    }
    System.out.println(queue.get(queue.size() - 1));
  }

  
  public void printDFSStack(Stack<Integer> stack){                        //helper method; prints the current stack of the DFS method
    System.out.println("Depth-first Search");
    for(int i = 0; i < stack.size() - 1; i++) {
      System.out.print(stack.get(i) + " - ");
    }
    System.out.println(stack.get(stack.size() - 1));
  }
  
  public void printAdjList() {                                            //helper method; prints adjacency list, will check to see if the csv was translated correctly
    for(LinkedList<Integer> currentList : adjList) {                        
      for(int index : currentList) {
        System.out.print(index + " ");
      }
      System.out.println();
    }
  }
}