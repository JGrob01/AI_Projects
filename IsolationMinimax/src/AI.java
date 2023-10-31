//
// Created by John Grobaker
//

import java.util.Random;

/*
 * The AI class holds all logic that deals with the AI of the game
 */

public class AI {
	private Game game;
	private int heuristicType;
	private int[] location;
	private int[][][]moves;
	private int opNum;
	private int maxScore;
	
	/* Sets the global variables
	 * @param g: game reference
	 * @param type: the game mode type
	 * @param r: the starting row location of the AI
	 * @param c: the starting column location of the AI
	 * @param o: the opponent number
	 */
	public AI(Game g, int type, int r, int c, int o) {
		game = g;
		heuristicType = type;
		location = new int[2];
		location[0] = r;
		location[1] = c;
		opNum = o;
	}
	
//------------------------------------AI MOVEMENT AND BLOCKER LOGIC----------------------------------------------
	/* calculateBestMove is called when the AI needs to make a move
	 * 
	 * First generate all possible moves the AI can make
	 * Create a new structure for the best possible move and call the minimax function
	 * Get the best move and make those moves (both movement and blockers)
	 * Sets the current location globals to the next location
	 */
	public void calculateBestMove() {
	  generatePossibleMoves();
	  MovesStruct temp = new MovesStruct(-1, new int[2], new int[2]);
	  MovesStruct s = minimax(3, true, Integer.MIN_VALUE, Integer.MAX_VALUE, temp);
	  int[] moveLocation = s.getMoveLocation();
	  int[] blockerLocation = s.getBlockerLocation();
		game.aiMove(location, moveLocation[0], moveLocation[1]);
		location[0] = moveLocation[0]; 
		location[1] = moveLocation[1];
    game.aiBlocker(blockerLocation[0], blockerLocation[1]);
	}
	
	/* generatePossibleMoves will set the global moves to the current all possible moves the AI can make
	 * 
	 * First calculate how many possible moves there are
	 * Loop through the 3x3 movement grid and for each cell loop through the entire board for blocker
	 * If the movement and blocker movement is valid add it to moves
	 */
	public void generatePossibleMoves() {
	  int openMoves = openCellsAroundLocation(location);
	  int openCells = game.getTotalOpenCells();
	  int count = 0;
	  moves = new int[openMoves*openCells][2][2];
	  
	  for(int r = 0; r < 3; r++) {
      for(int c = 0; c < 3; c++) {
        int nextR = location[0] - 1 + r;
        int nextC = location[1] - 1 + c;
        
        if( nextR >= 0 && nextR < 8 && nextC >= 0 && nextC < 6 && (nextR != location[0] || nextC != location[1]) && game.checkSpace(nextR, nextC)) { //works
          game.getGameBoard()[location[0]][location[1]].clearCell();
          game.getGameBoard()[nextR][nextC].addAIToCell();
          
          for(int gbr = 0; gbr < 8; gbr++) {
            for(int gbc = 0; gbc < 6; gbc++) {
              
              if(game.getGameBoard()[gbr][gbc].getCellOccupant().equals("Empty")) {
                moves[count][0][0] = nextR;
                moves[count][0][1] = nextC;
                moves[count][1][0] = gbr;
                moves[count][1][1] = gbc;
                count++;
              }
            }
          }
          game.getGameBoard()[location[0]][location[1]].addAIToCell();
          game.getGameBoard()[nextR][nextC].clearCell();
        }
      }
	  }
	}
		
//------------------------------------ADVERSARIAL SEARCH-------------------------------------------------------
	/*minimax will find the best possible move to make
	 * 
	 * @param depth: the depth of the search, (increase for harder AI?)
	 * @param maximizingPlayer: Checker if you want to maximize the player
	 * @param alpha: the alpha value to check for pruning
	 * @param beta: the beta value to check for pruning
	 * @param currentMove: the current move(game state) to be evaluated
	 * @return: return the best possible move
	 * 
	 * First check to see if you have reach the depth threshold, if so evaluate the current move
	 * Check if you maximizing the player 
	 * regardless, initialize the best score, move and blocker
	 * Loop through all possible moves and get the evaluation of each
	 * Based on maximizing; set the best if the current returned eval is better
	 * Prune
	 * return the best move
	 */
	public MovesStruct minimax(int depth, boolean maximizingPlayer, int alpha, int beta, MovesStruct currentMove) {
    if (depth == 0) {
      if (heuristicType == 0)
        currentMove.setScore(heuristicA(currentMove.getMoveLocation(), currentMove.getBlockerLocation()));
      else
        currentMove.setScore(heuristicB(currentMove.getMoveLocation(), currentMove.getBlockerLocation()));
      
      return currentMove;
    }

    if (maximizingPlayer) {
      int bestScore = Integer.MIN_VALUE;
      int[] bestMove = new int[2];
      int[] bestBlocker = new int[2];

      for (int i = 0; i < moves.length; i++) {
        int[] nextMove = moves[i][0];
        int[] blocker = moves[i][1];
        MovesStruct temp = new MovesStruct(-1, nextMove, blocker);
        temp = minimax(depth - 1, false, alpha, beta, temp);

        if (temp.getScore() > bestScore) {
          bestScore = temp.getScore();
          bestMove = temp.getMoveLocation();
          bestBlocker = temp.getBlockerLocation();
          maxScore = temp.getScore();
        } else if (temp.getScore() == bestScore) {
          Random random = new Random();
          int randomNumber = random.nextInt(2);
          
          if (randomNumber == 0) {
            bestScore = temp.getScore();
            bestMove = temp.getMoveLocation();
            bestBlocker = temp.getBlockerLocation();
          }
        }
        
        alpha = Math.max(alpha, bestScore);

        if (beta <= alpha) 
          break;
        }
        MovesStruct bestMoves = new MovesStruct(bestScore, bestMove, bestBlocker);
        return bestMoves;
        
    } else {
        int bestScore = Integer.MAX_VALUE;
        int[] bestMove = new int[2];
        int[] bestBlocker = new int[2];

        for (int i = 0; i < moves.length; i++) {
          int[] nextMove = moves[i][0];
          int[] blocker = moves[i][1];
          MovesStruct temp = new MovesStruct(-1, nextMove, blocker);
          temp = minimax(depth - 1, true, alpha, beta, temp);

          if (temp.getScore() < bestScore) {
            bestScore = temp.getScore();
            bestMove = temp.getMoveLocation();
            bestBlocker = temp.getBlockerLocation();
          } else if (temp.getScore() == bestScore) {
            Random random = new Random();
            int randomNumber = random.nextInt(2);
            
            if (randomNumber == 0) {
              bestScore = temp.getScore();
              bestMove = temp.getMoveLocation();
              bestBlocker = temp.getBlockerLocation();
            }
          }
          
          beta = Math.min(beta, bestScore);

          if (beta <= alpha)
            break;
        }
        MovesStruct bestMoves = new MovesStruct(bestScore, bestMove, bestBlocker);
        return bestMoves;
    }
	}
	 
//------------------------------------CALCULATE HEURISITIC VALUES----------------------------------------------
	/* Heuristic A's calculation 
	 * 
	 * @param nextLocation: the next move to be made
	 * @param blockerLocation: the next blocker location
	 * @return: the score from the move h + blocker h
	 */
	 public int heuristicA(int[] nextLocation, int[] blockerLocation) {
	   return calcMoveHeuristicA(nextLocation) + calcBlockerHeuristicA(nextLocation, blockerLocation);
	 }
	 
	 /* calcMoveHeuristicA will calculate the move heuristic 
	  * 
	  * @param nextLocation: the next move to be made
	  * @return: heuristic score
	  * 
	  * First make sure the current location is set then get the total open cells around it
	  * Next make sure the next location is set then get the total open cells around it
	  * return 1 if there is more open cells around next location
	  *        0 if the open cell counts are the same
	  *        -1 if there is less open cells around next location
	  */
	 public int calcMoveHeuristicA(int[] nextLocation) {
	   game.getGameBoard()[location[0]][location[1]].addAIToCell();
	   int currentCount = openCellsAroundLocation(location);
	   game.getGameBoard()[location[0]][location[1]].clearCell();
	    
	   game.getGameBoard()[nextLocation[0]][nextLocation[1]].addAIToCell();
	   int nextCount = openCellsAroundLocation(nextLocation);
	   game.getGameBoard()[nextLocation[0]][nextLocation[1]].clearCell();
	   
	   if (nextCount - currentCount > 0)
       return 1;
	   else if (nextCount - currentCount == 0)
       return 0;
	   else
	     return -1;
	}
	
	 /* calcBlockerHeuristicA will calculate the blocker heuristic
	  * 
	  * @param nextLocation: the next move to be made
	  * @param blockerLocation: the blocker location to be used
    * @return: heuristic score
    * 
    * First make sure the current location is set then find how many open cells are around the opponent
    * Next make sure the next location is set then add the blocker to the location and find how many open cells are around the opponent
    * return  100 if the blocker made the opp lose
    *         2 if the second is less then the first
    *         0 else
	  */
	 public int calcBlockerHeuristicA(int[] nextLocation, int[] blockerLocation) {
	   game.getGameBoard()[location[0]][location[1]].addAIToCell();
	   int opponentCount1 = openCellsAroundLocation(game.getOpLocation(opNum));
	   game.getGameBoard()[location[0]][location[1]].clearCell();
	   
	   game.getGameBoard()[nextLocation[0]][nextLocation[1]].addAIToCell();
	   game.getGameBoard()[blockerLocation[0]][blockerLocation[1]].addBlockerToCell();
	   int opponentCount2 = openCellsAroundLocation(game.getOpLocation(opNum));
	   game.getGameBoard()[nextLocation[0]][nextLocation[1]].clearCell();
	   game.getGameBoard()[blockerLocation[0]][blockerLocation[1]].clearCell();
	   
	   if (opponentCount2 == 0)
	     return 100;
	   else if (opponentCount2 < opponentCount1)
	     return 2;
	   else
       return 0;
	}
	
	 /* Heuristic B's calculation 
	* 
	* @param nextLocation: the next move to be made
	* @param blockerLocation: the next blocker location
	* @return: the score from the move h + blocker h
	*/
	public int heuristicB(int[] nextLocation, int[] blockerLocation) {
    return calcMoveHeuristicB(nextLocation) + calcBlockerHeuristicB(blockerLocation);
  }
	
	/* calcMoveHeuristicB will calculate the move heuristic 
	 * 
	 * @param nextLocation: the next move to be made
   * @return: heuristic score
   * 
   * First make sure the current location is set then get the total open cells around it
   * Next make sure the next location is set then get the total open cells around it
   * return the difference between the two
	 */
	public int calcMoveHeuristicB(int[] nextLocation) {
	  game.getGameBoard()[location[0]][location[1]].addAIToCell();
    int currentCount = openCellsAroundLocation(location);
    game.getGameBoard()[location[0]][location[1]].clearCell();
    
    game.getGameBoard()[nextLocation[0]][nextLocation[1]].addAIToCell();
    int nextCount = openCellsAroundLocation(nextLocation);
    game.getGameBoard()[nextLocation[0]][nextLocation[1]].clearCell();
    
    return nextCount - currentCount;
  }
	
	/* calcBlockerHeuristicB will calculate the blocker heuristic
   * 
   * @param blockerLocation: the blocker location to be used
   * @return: heuristic score
   * 
   * First make sure the current location is set then loop through the board
   * Count the number of cells that have 3 or more non-empty cells adjacent to them
   * Check to see if the added blocker isolates the opponent
   * Return the count found
   */
	public int calcBlockerHeuristicB(int[] blockerLocation) {
	  int hToken = 0;
	  
	  game.getGameBoard()[location[0]][location[1]].addAIToCell();
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 6; col++) {
        int[] tempLocation = new int[2];
        tempLocation[0] = row;
        tempLocation[1] = col;
        if (!(game.checkSpace(row, col)) && openCellsAroundLocation(tempLocation) >= 3) {
          hToken += 1;          
        }
      }
    }
    game.getGameBoard()[location[0]][location[1]].clearCell();
    
    game.getGameBoard()[blockerLocation[0]][blockerLocation[1]].addBlockerToCell();
    if(game.checkEndIsolation())
      hToken = 100;
    game.getGameBoard()[blockerLocation[0]][blockerLocation[1]].clearCell();

    return hToken;
  }
	
//------------------------------------HELPER METHODS----------------------------------------------------	
	/* openCellsAroundLocation is a helper function that finds the amount of cells open around a given location
	 * 
	 * @param loc: the location
	 * @return: total number of open cells around loc
	 * 
	 * Basically loops through the adjacent cells, if they are empty add to count
	 */
	public int openCellsAroundLocation(int [] loc) {
		int count = 0;
		int [] nextLocation = new int[2];
		
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 3; c++) {
				try {
					nextLocation[0] = loc[0] - 1 + r;
					nextLocation[1] = loc[1] - 1 + c;
					if(game.checkSpace(nextLocation[0], nextLocation[1]))
						count++;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		return count;
	}
	
	/* getLocation gets the location of the AI
	 * Used in the game logic
	 */
	public int[] getLocation() { return location; }
	
	/* MovesStruct holds a move the AI can make and the evaluation score of the move
	 * score: the evaluation score used in search
	 * move: an array that holds the coordinates of a move
	 * blocker: an array that holds the coordinates of a blocker move
	 * 
	 * bunch of setters and getters
	 */
	public class MovesStruct {
	  private int scoreValue;
	  private int[] moveLocation;
	  private int[] blockerLocation;

	  public MovesStruct(int score, int[] move, int[] blocker) {
	      this.scoreValue = score;
	      this.moveLocation = move;
	      this.blockerLocation = blocker;
	  }

	  public void setScore(int score) { scoreValue = score; }

	  public void setMoveLocation(int[] move) { moveLocation = move; }
  
	  public void setBlockerLocation(int[] blocker) { blockerLocation = blocker; }
	  
	  public int getScore() { return scoreValue; }

	  public int[] getMoveLocation() { return moveLocation; }
	  
	  public int[] getBlockerLocation() { return blockerLocation; }
	}
}