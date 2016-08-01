package goat;

import javax.swing.JOptionPane;

public class Launcher {

	public static void main(String[] args) 
	{
		String answer = JOptionPane.showInputDialog("How many goats would you like?");
		int numGoats = Integer.parseInt(answer);
		for(int i = 0; i < numGoats; i++)
			new TranslucentFrame();
	}

}
