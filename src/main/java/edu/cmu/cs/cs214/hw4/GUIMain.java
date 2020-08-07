package edu.cmu.cs.cs214.hw4;

import edu.cmu.cs.cs214.hw4.core.Carcassonne;
import edu.cmu.cs.cs214.hw4.gui.GameControllerFrame;
import javax.swing.SwingUtilities;

/**
 * Main method class to start the game;
 */
public class GUIMain {
	/**
	 * Main method to start the game;
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Carcassonne carcassonne = null;
				GameControllerFrame controlFrame = null;
				try {
					controlFrame = new GameControllerFrame();
					controlFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
