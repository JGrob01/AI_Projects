//
// Created by John Grobaker
//
import java.io.File;
import java.util.Scanner;

public class ReadInput {
  /**
   * readInput will each input given to the code
   *  inputs include, filenames for the edge weights and hCosts 
   *  starting node and goal node
   */
  public String[] readInput() {
    Scanner scanner = new Scanner(System.in);
    boolean correctResponse = false;
    String [] returnArray = new String[4];
    
    while (correctResponse != true) {                                     //verifies that the given file is correct
      System.out.println("Please enter the edge weight file name and extension: ");
      returnArray[0] = scanner.nextLine();
      
      if (!new File(returnArray[0]).isFile())
        System.out.println("Invalid File");
      else
        correctResponse = true;
    }
    correctResponse = false;

    while (correctResponse != true) {                                     //verifies that the given file is correct
      System.out.println("Please enter the heuristic file name and extension: ");
      returnArray[1] = scanner.nextLine();
      
      if (!new File(returnArray[1]).isFile())
        System.out.println("Invalid File");
      else
        correctResponse = true;
    }
    correctResponse = false;

    while (correctResponse != true) {                                     //verifies that the given staringID is within range
      System.out.println("Start node (1 - 200):");
      returnArray[2] = scanner.nextLine();

      if (Integer.parseInt(returnArray[2]) <= 0 || Integer.parseInt(returnArray[2]) >= 201)
        System.out.println("Invalid starting ID");
      else
      correctResponse = true;
    }
    correctResponse = false;
    
    while (correctResponse != true) {                                     //verifies that the given endingID is within range
      System.out.println("End node (1 - 200):");
      returnArray[3] = scanner.nextLine();

      if (Integer.parseInt(returnArray[3]) <= 0 || Integer.parseInt(returnArray[3]) >= 201)
        System.out.println("Invalid starting ID");
      else
      correctResponse = true;
    }
    
    return(returnArray);
  }
}
