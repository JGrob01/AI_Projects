import java.util.Random;
/* 
 * @author John G
 * 
 * The Game class holds all of the back end game logic 
 * The globals hold the rows, cols, gui, gameboard player and AI's
 * Also has the experiment variables 
 * 
 * Game constructor initialize the game, board and gui 
 */
public class Game {
  private int rows;
  private int columns;
  private GUI gui;
  private Cell[][] gameBoard;
  
  private boolean playerMoveTurn;
  private boolean playerBlockerTurn;
  private int[] playerPosition;
  private AI aiPlayer1;
  private AI aiPlayer2;
  
  private int gameModeType;
  private boolean expirementMode;
  
  private int totalMoves;
  private int aiWon;
  private int gameCount;
  
  public Game(int r, int c) {
    this.rows = r;
    this.columns = c;
    gameBoard = new Cell[r][c];
    expirementMode = false;
    
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        gameBoard[i][j] = new Cell();
      }
    }
    
    gui = new GUI(this);
    gui.popMainMenu();
  }

//------------------------------------GAMEMODE LOGIC---------------------------------------------- 
  /* startPVEGame is called when you want to play a player vs AI
   * 
   * Adds the Player and AI to the game and randomly chooses who goes first
   * Call pve gaem loop
   */
  public void startPVEGame() {
    System.out.println("Start PVE game");
    gameModeType = 0;
    
    gameBoard[0][3].addAIToCell();
    aiPlayer1 = new AI(this, 0, 0, 3, 0);
    
    gameBoard[7][2].addPlayerToCell();
    playerPosition = new int[2];
    playerPosition[0] = 7;
    playerPosition[1] = 2;
    
    gui.getGameBoard().updateBoard(gameBoard);
    
    Random random = new Random();
    int randomNumber = random.nextInt(2);
    
    if (randomNumber == 0) {
      playerMoveTurn = true;
    } else {
      playerMoveTurn = false;
    }
    
    pveGameLoop();
  }
  
  /* pveGameLoop will "loop" for the game
   * 
   * check for isolation 
   * then check if its the players turn or the AIs
   * The player turn is bound to variables and moves are made during input events
   * When the AI moves check for isolation
   */
  public void pveGameLoop() {
    if(checkEndIsolation()) {
      endGame();
      return;
    }
    
    if(!(playerMoveTurn || playerBlockerTurn)) {
      aiPlayer1.calculateBestMove();
      playerMoveTurn = true;
    }
    
    gui.getGameBoard().updateBoard(gameBoard);
    
    if(checkEndIsolation()) {
      endGame();
      return;
    }
  }
  
  /* startEVEGame will start a game between AIs
   * @param exMode: defines if the game is in experiment mode
   * 
   * Add the AIs to the board and randomly chooses who goes first
   * Call the game loop
   */
  public void startEVEGame(boolean exMode) {
    //System.out.println("Start AI V AI game");
    gameModeType = 1;
    expirementMode = exMode;
    
    gameBoard[0][3].addAIToCell();
    aiPlayer1 = new AI(this, 0, 0, 3, 1);
    
    gameBoard[7][2].addAIToCell();
    aiPlayer2 = new AI(this, 0, 7, 2, 0);
    
    gui.getGameBoard().updateBoard(gameBoard);
    
    boolean ai1Turn;
    Random random = new Random();
    int randomNumber = random.nextInt(2);
    
    if (randomNumber == 0) {
      ai1Turn = true;
    } else {
      ai1Turn = false;
    }
    eveGameLoop(ai1Turn);
  }
  
  /* eveGameLoop is the main loop of the game for the AI's
   * @param ai1Turn: defines which AI's turn it is
   * 
   * game loop is based on if there isn't an isolation yet
   * each AI makes its move
   */
  public void eveGameLoop(boolean ai1Turn) {
    while(!checkEndIsolation()) {
      if(ai1Turn)
        aiPlayer1.calculateBestMove();
      else
        aiPlayer2.calculateBestMove();
      
      ai1Turn = !ai1Turn;
      totalMoves++;
      
      gui.getGameBoard().updateBoard(gameBoard);  
    }
    endGame();
  }
  
  /* checkEndIsolation will check the current game board if someone is isolated 
   * 
   * @return if an isolation happened or not
   * 
   * First see which game mode it is 
   * Check to see if any of the players are isolated 
   */
  public boolean checkEndIsolation() {
    if(gameModeType == 0) {
      if(openCellsAroundLocation(playerPosition) == 0) {
        System.out.println("Player loses");
        gui.getGUIInfo().setInfoLabel("Player loses!");
        return true;
      }
      if(openCellsAroundLocation(aiPlayer1.getLocation()) == 0) {
        System.out.println("AI loses");
        gui.getGUIInfo().setInfoLabel("AI loses!");
        return true;
      }
    } else {
      if(openCellsAroundLocation(aiPlayer1.getLocation()) == 0) {
        //System.out.println("AI 1 loses");
        gui.getGUIInfo().setInfoLabel("AI 1 loses!");
        aiWon = 0;
        return true;
      }
      if(openCellsAroundLocation(aiPlayer2.getLocation()) == 0) {
        //System.out.println("AI 2 loses");
        gui.getGUIInfo().setInfoLabel("AI 2 loses!");
        aiWon = 1;
        return true;
      }
    }
    return false;
  }
  
  /* endGame is called when an isolation occurs 
   * 
   * If the game mode is AIvAI check to see if the game is also in expirement mode
   * If so do the expirement stuff
   * Else open the restart menu
   */
  public void endGame() {
    if(gameModeType == 1 && expirementMode && gameCount < 50) {
      System.out.println(aiWon+" "+totalMoves);
      gameCount++;
      totalMoves = 0;
      restartGame();
      startEVEGame(true);
    }
    
    gui.popRestart();
  }
  
  /* restartGame is called when you need to restart the game at square one
   * 
   * Re-initialize the game board
   */
  public void restartGame() {
    gameBoard = new Cell[8][6];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        gameBoard[i][j] = new Cell();
      }
    }
    gui.getGameBoard().updateBoard(gameBoard);
  }
//------------------------------------PLAYER & AI MOVEMNT LOGIC----------------------------------------------
  /* playerSelection called when input action from gui is detected
   * 
   * @param targetR: the target row of cell touched
   * @param targetC: the target col of cell touched
   * @return: false if its not the players move or invalid move
   */
  public boolean playerSelection(int targetR, int targetC) {
    if(playerMoveTurn) {
      return playerMoveSelection(targetR, targetC);
    } else if (playerBlockerTurn) {
      return playerBlockerSelection(targetR, targetC);
    } else {
     return false; 
    }
  }
  
  /* playerMoveSelection is called from the gui when the player wants to make a move
   * 
   * @param targetR: is the target row of cell
   * @param targetC: is the target col of cell
   * @return: if the cell chosen is a valid cell to move
   * 
   * Check to see if the cell chosen is an adjacent cell
   * if so move the player if the cell is empty
   */
  public boolean playerMoveSelection(int targetR, int targetC) {
    int dx = Math.abs(targetR - playerPosition[0]);
    int dy = Math.abs(targetC - playerPosition[1]);

    if (dx <= 1 && dy <= 1) {
    	if(checkSpace(targetR, targetC)) {	
    		gameBoard[targetR][targetC].addPlayerToCell();
    		gameBoard[playerPosition[0]][playerPosition[1]].clearCell();
    		playerPosition[0] = targetR;
    		playerPosition[1] = targetC;
    		playerBlockerTurn = true;
    		playerMoveTurn = false;
    		gui.getGameBoard().updateBoard(gameBoard);
    		return true;
    	}
    	return false;
    } else {
      return false;
    }
  }
  
  /* playerBlockerSelection is called when the player is placing a blocker
   * 
   * @param targetR: the target row of the cell touched
   * @param targetC: the target col of the cell touched
   * @return: if it was a valid move
   */
  public boolean playerBlockerSelection(int targetR, int targetC) {
	  if(checkSpace(targetR, targetC)) {	
		  gameBoard[targetR][targetC].addBlockerToCell();
		  gui.getGameBoard().updateBoard(gameBoard);
		  playerBlockerTurn = false;
		  pveGameLoop();
		  return true;
	  }
	  return false;
  }
  
  /* aiMove is called when the AI wants to move
   * 
   * @param aiLocation: the current location of the AI
   * @param targetR: the target row of the AI move
   * @param targetC: the target col of the AI move
   * 
   * Will add the AI to the new spot and remove the old AI spot
   */
  public void aiMove(int[] aiLocation, int targetR, int targetC) {
    gameBoard[targetR][targetC].addAIToCell();
    gameBoard[aiLocation[0]][aiLocation[1]].clearCell();
  }
  
  /* aiBlocker is called when the AI wants to block
   * 
   * @param targetR: the target row of the blocker
   * @param targetC: the target col of the blocker
   * 
   * Will add the blocker to the cell
   */
  public void aiBlocker(int targetR, int targetC) {
    gameBoard[targetR][targetC].addBlockerToCell();
  }

//------------------------------------GAME HELPER METHODS----------------------------------------------
  
  // gets a ref to the game board 
  public Cell[][] getGameBoard(){ return gameBoard; }
  
  /* getOpLocation gets the location of the opponent
   * 
   * @param opNum: the opponents ID number 
   * @return: The location of the opponent
   * 
   * Finds which op location to get and returns it
   */
  public int[] getOpLocation(int opNum) {
    if(gameModeType == 0)
      return playerPosition;
    else if(gameModeType == 1 && opNum == 0)
      return aiPlayer1.getLocation();
    else 
      return aiPlayer2.getLocation();
  }
  
  /* getTotalOpenCells returns the total number of empty cells
   * @return the total number of empty cells
   */
    public int getTotalOpenCells() {
      int count = 0;
      for(int r = 0; r < 8; r++) {
        for(int c = 0; c < 6; c++) {
          if(checkSpace(r, c))
            count++;
        }
      }
      return count;
    }
  
  /* checkSpace checks to see if given cell is empty 
   * 
   * @param targetR target row of cell
   * @param targetC tatget col of cell
   * @return true if empty
   */
  public boolean checkSpace(int targetR, int targetC) {
	  if(gameBoard[targetR][targetC].getCellOccupant().equals("Empty"))
		  return true;
	  else
		  return false;
  }
  
  /* openCellsAroundLocation returns the open cells around a given location
   * 
   * @param l the location to be looked at
   * @return the total number of open cells
   */
  public int openCellsAroundLocation(int [] l) {
    int count = 0;
    int [] nextLocation = new int[2];
    
    for(int r = 0; r < 3; r++) {
      for(int c = 0; c < 3; c++) {
        try {
          nextLocation[0] = l[0] - 1 + r;
          nextLocation[1] = l[1] - 1 + c;
          if(checkSpace(nextLocation[0], nextLocation[1]))
            count++;
        } catch (ArrayIndexOutOfBoundsException e) {
          continue;
        }
      }
    }
    return count;
  }
  
  /*
   * Helper to print the current state of the board 
   */
  public void printBoard() {
    for(int r = 0; r < rows; r++) {
      for(int c = 0; c < columns; c++) {
        if(checkSpace(r,c))
          System.out.print("0 ");
        else
          System.out.print("1 ");
      }
      System.out.println("");
    }
  }
}
