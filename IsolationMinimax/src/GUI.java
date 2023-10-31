import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
/*
 * @author John G
 * 
 * GUI class is used for the visuals
 * Classic GUI stuff
 */
public class GUI extends JFrame {
  private Game game;
  private GUIGameBoard board;
  private GUIInfo guiInfo;
  private JFrame mainMenu;
  private JFrame endMenu;
  private int gameMode;
  private boolean exMode;

  
  public GUI(Game g) {
    game = g;
    exMode = false;
  }
  
  public Game getGameRef() {
    return game;
  }
  //------------------------------------MAIN MENU CODE----------------------------------------------
  /* popMainMenu is the pop up start menu for the game
   * 
   * add the relevant buttons and their input actions
   * Can hide the main menu when playing the game
   */
  public void popMainMenu() {
    mainMenu = new JFrame("Choose your game mode!");
    mainMenu.setSize(400,400);
    mainMenu.setLayout(new GridLayout(5, 1));
    
    JButton pve = new JButton("Player vs AI");        
    JButton eve = new JButton("AI A vs AI A");
    JButton eve2 = new JButton("AI B vs AI B");
    JButton eve3 = new JButton("AI A vs AI B");
    JButton experimentBtn = new JButton("Experiment Mode: " + exMode);

    pve.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          System.out.println("Clicked Player vs AI");
          gameMode = 0;
          createGameboard(8,6);
          hideMainMenu();
          game.startPVEGame();
       }          
    });
    
    eve.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         System.out.println("Clicked AI vs AI");
         gameMode = 1;
         createGameboard(8,6);
         hideMainMenu();
         game.startEVEGame(exMode);
       }
    });
    
    eve2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked AI vs AI");
        gameMode = 1;
        createGameboard(8,6);
        hideMainMenu();
        game.startEVEGame(exMode);
      }
   });
    
    eve3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked AI vs AI");
        gameMode = 1;
        createGameboard(8,6);
        hideMainMenu();
        game.startEVEGame(exMode);
      }
   });
    
    experimentBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exMode = !exMode;
        experimentBtn.setText("Experiment Mode: " + exMode);
      }
   });
    
    mainMenu.add(pve);
    mainMenu.add(eve);    
    mainMenu.add(eve2); 
    mainMenu.add(eve3); 
    mainMenu.add(experimentBtn);

    mainMenu.setVisible(true);    
  }
  
  public void hideMainMenu() {
    mainMenu.setVisible(false);
  }
  
  public void showMainMenu() {
    mainMenu.setVisible(true);
  }
  
//------------------------------------GAMEBOARD CODE----------------------------------------------
  /* createGameboard is called when the game starts
   * 
   * @param rows the number of rows of the game board
   * @param cols the number of cols of the game board
   * 
   * Create the gui part of the game board, call the GUI Gameboard
   * Can hide the game board
   * Has getters for the references 
   */
  public void createGameboard(int rows, int cols) {
    setLayout(new BorderLayout());
    guiInfo = new GUIInfo(this);
    board = new GUIGameBoard(this, rows, cols);
    add(guiInfo, BorderLayout.EAST);
    add(board, BorderLayout.CENTER);

    setTitle("Isola");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(1000,800));
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
  
  public GUIGameBoard getGameBoard() {
    return board;
  }
  
  //wip code
  public GUIInfo getGUIInfo() {
    return guiInfo;
  }
  
  public void hideGameboard() {
    setVisible(false);
  }

//------------------------------------RESTART CODE----------------------------------------------
  /* popRestart is the pop up end menu after the game finishes
   * 
   * add the relevant buttons and their input actions
   * Can hide the menu when playing the game and after restart
   */
  public void popRestart() {
    endMenu = new JFrame("Restart Game?");
    endMenu.setSize(400,400);
    endMenu.setLayout(new GridLayout(3, 1));
  
    JButton yes = new JButton("Yes");        
    JButton no = new JButton("No");
    JButton mainMenuBtn = new JButton("Return to main menu");

    yes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Clicked Player vs AI");
        createGameboard(8,6);
        hideMainMenu();
        game.restartGame();
        
        if(gameMode == 0)
          game.startPVEGame();
        else 
          game.startEVEGame(exMode);
        
        hideEndMenu();
      }          
    });
  
    no.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
  
    mainMenuBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        hideGameboard();
        showMainMenu();
        hideEndMenu();
      }
    });
  
    endMenu.add(yes);
    endMenu.add(no); 
    endMenu.add(mainMenuBtn);    

    endMenu.setVisible(true);    
  }

  public void hideEndMenu() {
    endMenu.setVisible(false);
  }

  public void showEndMenu() {
    endMenu.setVisible(true);
  }
}