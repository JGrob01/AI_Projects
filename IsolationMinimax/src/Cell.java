/*
 * @author John G
 * 
 * Cell class is used to make up the game board
 * A cell can be empty, AI, Player or blocker
 * The class has relevant setters a getter and a clear function
 */
public class Cell {
  private String cellOccupant;

  public Cell() {
    cellOccupant = "Empty";
  }
  public void addPlayerToCell() {
    if (cellOccupant.equals("Empty")) {
      cellOccupant = "Player";
    }
  }
  
  public void addAIToCell() {
    if (cellOccupant.equals("Empty")) {
      cellOccupant = "AI";
    }
  }

  public void addBlockerToCell() {
    if (cellOccupant.equals("Empty")) {
      cellOccupant = "Blocker";
    }
  }
  
  public void clearCell() {
    cellOccupant = "Empty";
  }
    
  public String getCellOccupant() {
    return cellOccupant;
  }
}
