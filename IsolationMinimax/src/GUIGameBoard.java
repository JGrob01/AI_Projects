import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
/*
 * @author John G
 *
 * GUIGameBoard class is the visual side of the game board
 * 
 * The game board is made up of gui cells that each have their own input listeners
 * There are relevant setters and getters 
 * Update board will reload each cell when called so the correct information is displayed 
 */
public class GUIGameBoard extends JPanel {
  private GUI gui;
  private int row;
  private int col;
  private GUICell[][] boardCells;
  private Cell currentCell;
  
  public GUIGameBoard(GUI g, int rows, int cols) {
    super(new GridLayout(rows, cols));
    row = rows;
    col = cols;
    gui = g;
    
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    boardCells = new GUICell[rows][cols];
    
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        boardCells[r][c] = new GUICell(this, r, c);
        add(boardCells[r][c]);
      }
    }
  }
  
  public GUI getGUI() {
    return gui;
  }
  
  public GUICell getCell(int r, int c) {
    if (r < row && r >= 0) {
      if (c < col && c >= 0) {
        return boardCells[r][c];
      }
    }
    return null;
  }
  
  public void setCurrentCell(Cell cell) {
    currentCell = cell;
  }
  
  public Cell getCurrentCell() {
    return currentCell;
  }

  public void updateBoard(Cell[][] grid) {
    for (int r = 0; r < row; r++) {
      for (int c = 0; c < col; c++) {
        if(grid[r][c].getCellOccupant().equals("Player")) {
          boardCells[r][c].setPlayer(0);
        } else if (grid[r][c].getCellOccupant().equals("AI")) {
          boardCells[r][c].setPlayer(1);
        } else if (grid[r][c].getCellOccupant().equals("Blocker")) {
          boardCells[r][c].setBlocker();
        } else {
          boardCells[r][c].setEmpty();
        }
      }
    }
  }
}

