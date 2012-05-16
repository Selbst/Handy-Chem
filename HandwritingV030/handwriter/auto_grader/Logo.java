package auto_grader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Logo extends JPanel {

	public Logo() {
	    JButton bx = new JButton("Top left");
	    add(bx);
	    System.out.println(isOpaque());
	    setSize(500, 500);
	    this.setBackground(new Color(100, 100, 100));
	    setVisible(true);
	}
	
	  public void paint(Graphics g) {
		    Graphics2D g2d = (Graphics2D) g;
		    
		    
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		    Font font = new Font("Serif", Font.PLAIN, 120);
		    AttributedString as1 = new AttributedString("1234567890");
		    as1.addAttribute(TextAttribute.FONT, font);
		    g2d.drawString(as1.getIterator(), 15, 120);

		    
		    
		    
		    
		    
	  }
	
	
}
