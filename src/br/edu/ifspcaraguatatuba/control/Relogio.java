package br.edu.ifspcaraguatatuba.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class Relogio extends Thread {

	private Date date;
	private JLabel clock;
	
	public Relogio(JLabel clock) {
		this.clock = clock;
	}
	
	
	
	@Override
	public void run() {
		
		try {
			while (true) {
				StringBuffer data = new StringBuffer();
				date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				sdf.format(date);
				
				clock.setText(data.toString() + sdf.format(date));
				Thread.sleep(1000);
				clock.revalidate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Date getDate () {
		return this.date;
	}
	
	
}