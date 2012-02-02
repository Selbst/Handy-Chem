package drawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawMolecule extends JPanel {
	

	
	
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Font font = new Font("Serif", Font.PLAIN, 120);

    AttributedString as1 = new AttributedString("1234567890");
    as1.addAttribute(TextAttribute.FONT, font);
    g2d.drawString(as1.getIterator(), 15, 120);
    
    
    
    
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Text attributes");
    frame.add(new DrawMolecule());
    frame.setBackground(new Color(255, 255, 255));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(700, 700);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
  
  
  
}