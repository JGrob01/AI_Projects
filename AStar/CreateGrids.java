//
// Created by John Grobaker
//
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CreateGrids {
  double[][] edgeWeights = new double[200][200];
  double[][] hCosts = new double[200][200];
  
  /**
   * createGrids will create the 2D array for both the edge weights and h costs
   * fileName: a string that hold the name of the file to be used
   * type: determines if the file given is for the hcosts or edge weights 
   */
  public double[][] createGrids(String fileName, int type) {
    if(type == 0) {
      readEdgeWeights(fileName);
      //printGridValues(edgeWeights);
      return edgeWeights;
    } else {
      readhCostFile(fileName);
      //printGridValues(hCosts);
      return hCosts;
    }
  }
  
  /**
   * readEdgeWeights will read in each line of the file and sends it off to be added to the 2D array
   * fileName: a string that hold the name of the file to be used
   */
  public void readEdgeWeights(String fileName){
    try {
      Scanner fileReader = new Scanner(new File(fileName)); 
      String[] tempLine = fileReader.next().split(","); 
      while (fileReader.hasNext()) { 
        tempLine = fileReader.next().split(",");  
        createEdgeWeightGrid(tempLine);
      }
    } catch (IOException e) {
      e.printStackTrace();  
    }
  }
  
  /**
   * createEdgeWeightGrid will take each line of the related file and add it to the grid
   * currentLine: a string array that holds the current file line to be parsed and added to grid
   */
  public void createEdgeWeightGrid(String[] currentLine) {
    int row = Integer.parseInt(currentLine[0]);
    int col = Integer.parseInt(currentLine[1]);
    edgeWeights[row-1][col-1] = Double.parseDouble(currentLine[2]);
  }
  
  /**
   * readhCostFile will read in each line of the file and sends it off to be added to the 2D array
   * fileName: a string that hold the name of the file to be used
   */
  public void readhCostFile(String fileName){
    try {
      Scanner fileReader = new Scanner(new File(fileName)); 
      boolean startPrint = false;
      int rowCount = 0;
      while (fileReader.hasNext()) { 
        String[] tempLine = fileReader.next().split(",");  
        
        if(startPrint) {                                    //checks to see if the file line is on the correct values first
          createhCostGrid(tempLine, rowCount);
          rowCount++;
        }
        
        try {
          if(tempLine[0].equals("FROM")){
            startPrint = true;
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          startPrint = false;
        }                          
      }   
      fileReader.close();
      return;
    } catch (IOException e) {
      e.printStackTrace();  
    }  
  }
  
  /**
   * createhCostGrid will take each line of the related file and add it to the grid
   * row: makes sure that the line is parsed to the correct row of the grid
   */
  public void createhCostGrid(String[] currentLine, int row){
    int col = row;
    int start = 0;
    for(String index: currentLine){
      if(start!=0) {
        try{
          double i = Double.parseDouble(index);
          if(row<200) {
            hCosts[row][col] = Double.parseDouble(index);
            col++;
          }
        } catch(NumberFormatException ex){
          continue;
        }
      } else {
        start++;
      }
    }
  }
  
  /**
   * printGridValues helper that prints the grids out
   */
  public void printGridValues(double[][]grid){
    for(int r = 0; r<200; r++){
      for(int c = 0; c<200; c++){
        System.out.print(grid[r][c]+" ");
      }
      System.out.println("");
    }
    return;
  }
}
