package br.edu.ifspcaraguatatuba.view;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -6049993120517639162L;
	private JPanel contentPane;

	private String[] tableHeader = new String[]{"Nome","Artista","Álbum"};
	private DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 10);
	
	
	private void checkDiretory () {
		String dir = System.getProperty("user.home");
		Path path = Paths.get(dir + "\\My Music");
		
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public MainFrame() {
		
		checkDiretory();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Radio Relogio");
		setBounds(100, 100, 600, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 227, 564, 171);
		contentPane.add(scrollPane);
		
		JTable table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		
		JLabel btnPrevious = new JLabel();
		btnPrevious.setBounds(173, 409, 48, 48);
		btnPrevious.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/PlayerPrevious.png")));
		contentPane.add(btnPrevious);
		
		JLabel btnPlay = new JLabel();
		btnPlay.setBounds(231, 409, 48, 48);
		btnPlay.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/PlayerPlay.png")));
		contentPane.add(btnPlay);
		
		JLabel btnPause = new JLabel();
		btnPause.setEnabled(false);
		btnPause.setBounds(289, 409, 48, 48);
		btnPause.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/PlayerPause.png")));
		contentPane.add(btnPause);
		
		JLabel btnNext = new JLabel();
		btnNext.setBounds(347, 409, 48, 48);
		btnNext.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/PlayerNext.png")));
		contentPane.add(btnNext);
	}
}
