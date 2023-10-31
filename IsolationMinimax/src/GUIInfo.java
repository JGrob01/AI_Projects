import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/* 
 * @author John G
 * 
 * GUIInfo is used to display information for the player to see
 */
public class GUIInfo extends JPanel{
  private GUI gui;
  private JLabel infoLabel;
  
  public GUIInfo(GUI g) {
    gui = g;
    
    setBackground(Color.LIGHT_GRAY);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(200, 800));
    infoLabel = new JLabel();
    infoLabel.setText("Welcome to the game!");
    add(infoLabel,BorderLayout.CENTER);
  }
  
  public void setInfoLabel(String text) {
    
  }
}
