package interfaces;

import javax.swing.*;
import java.awt.*;

/** Interface class for all SimDisplays
 * @author Graham Ritchie
 */
public abstract class SimDisplay extends JPanel
{
	/** Main repaint method. This mathod is called whenever SimUI calls this
         * SimDisplay's repaint methods It must repaint this SimDisplay's JPanel.
         * @param g Java graphics object
         */
	public void paintComponent(Graphics g)
	{
            this.setDoubleBuffered(true);
            super.paintComponent(g);
         
	}
	
}
