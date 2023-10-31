import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
/* 
 * @author John G
 *
 *  GuiCell holds the visual information of each cell of the game board
 *  
 *  Each player and ai get their own icon 
 *  Set X will add info to cell
 *  
 *  Uses mouse listener to send information to game
 */
public class GUICell  extends JPanel implements MouseListener {
  private GUIGameBoard gb;
  private ImageIcon blue;
  private ImageIcon red;
  private JLabel icon;
  private int row;
  private int col;
  
  public GUICell(GUIGameBoard g, int r, int c) {
    super(new BorderLayout());
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    addMouseListener(this);
    gb = g;
    row = r;
    col = c;
    
    ImageIcon image = null;
    image = new ImageIcon("pawnblue.png");
    Image scaled = image.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
    blue = new ImageIcon(scaled);
    
    image = null;
    image = new ImageIcon("pawnred.png");
    scaled = image.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
    red = new ImageIcon(scaled);
  }
  
  public void setPlayer(int player) {
    if(player == 0) {
      icon = new JLabel(blue);
      add(icon, BorderLayout.CENTER);
    } else {
      icon = new JLabel(red);
      add(icon, BorderLayout.CENTER);
    }
  }
  
  public void setBlocker() {
    setBackground(Color.BLACK);
  }
  
  public void setEmpty() {
    removeAll();
    revalidate();
    repaint();
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    Game game = gb.getGUI().getGameRef();
    
    if(!(game.playerSelection(row, col)))
      System.out.println("It is not your turn or invalid move.");
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

}
