package br.edu.ifspcaraguatatuba;

import javax.swing.JFrame;
import javax.swing.UIManager;

import br.edu.ifspcaraguatatuba.view.MainFrame;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
			
			JFrame mainFrame = new MainFrame();
			mainFrame.setVisible(true);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
