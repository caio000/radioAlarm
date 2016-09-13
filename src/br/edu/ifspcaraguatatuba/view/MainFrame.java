package br.edu.ifspcaraguatatuba.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import br.edu.ifspcaraguatatuba.control.Relogio;
import br.edu.ifspcaraguatatuba.control.Tocador;
import java.awt.Font;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -6049993120517639162L;
	private JPanel contentPane;
	
	private boolean isPaused = false;

	private String[] tableHeader = new String[]{"Musica"};
	private DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);
	
	private JButton btnPause;
	private JButton btnPlay;
	private JTable table;
	private JLabel lblHora;
	
	private ArrayList<File> musicas = new ArrayList<>();
	
	private Tocador tocador;
	
	private Relogio relogio;
	
	
	
	public MainFrame() {
		initComponent();
		relogio = new Relogio(this.lblHora);
		relogio.start();
	}
	
	
	
	private void checkDiretory () {
		String dir = System.getProperty("user.home");
		Path path = Paths.get(dir + "/My Music");
		
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void pegaMusicas () {
		File arq = new File(System.getProperty("user.home") + "/My Music");
		
		for (File m: arq.listFiles()) {
			if(m.getName().endsWith(".mp3")) {
				musicas.add(m.getAbsoluteFile());
				tableModel.addRow(new String[]{m.getName()});
			}
				
		}
	}
	
	private void checkPlayerStatus() {
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				while(tocador.getPlayerStatus() != 3) {
					System.out.println("Tocando...");
				}
				
				// ao acabar as musicas ativar o bot�o de play.
				System.out.println("acabou as musicas");
				btnPlay.setEnabled(true);
				btnPause.setEnabled(false);
			}
		};
		
		
		Thread thread = new Thread(r);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	
	public void initComponent() {
		
		checkDiretory();
		pegaMusicas();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Radio Relogio");
		setBounds(100, 100, 377, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 227, 339, 171);
		contentPane.add(scrollPane);
		
		table = new JTable() {
			private static final long serialVersionUID = 1216934504916940639L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.addMouseListener(new MouseAdapter() { // click na tabela
			@Override
			public void mouseClicked(MouseEvent mouse) {
				
				// verifica se o mouse foi clicado duas vezes
				if (mouse.getClickCount() == 2) {
					// TODO implementar fun��o que inicia a musica selecionada
					int line = table.getSelectedRow();
					
					if (tocador != null) {
						System.out.println("inicia a musica");
						tocador.pause();
						tocador.setMusicIndex(line);
						tocador.play();
						
					} else {
						tocador = new Tocador(musicas);
						tocador.setMusicIndex(line);
						tocador.play();
						
						btnPlay.setEnabled(false);
						btnPause.setEnabled(true);
					}
					
					
					
				}
				
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		
		btnPlay = new JButton();
		btnPlay.addMouseListener(new MouseAdapter() { // Click do bot�o play
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if (btnPlay.isEnabled() && isPaused == false) {
					
					try {
						
						if (musicas.size() > 0) {
							tocador = new Tocador(musicas);
							tocador.play();
							checkPlayerStatus();
							
							btnPlay.setEnabled(false);
							btnPause.setEnabled(true);
						} else {
							throw new Exception("Não há nenhuma musica para tocar");
						}
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				} else if (btnPlay.isEnabled() && isPaused == true) {
					tocador.resume();
					btnPlay.setEnabled(false);
					btnPause.setEnabled(true);
				}
			}
		});
		btnPlay.setBounds(127, 409, 48, 48);
		btnPlay.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/PlayerPlay.png")));
		contentPane.add(btnPlay);
		
		btnPause = new JButton();
		btnPause.addActionListener(new ActionListener() { // Click do bot�o pausa
			public void actionPerformed(ActionEvent arg0) {
				tocador.pause();
				isPaused = true;
				btnPlay.setEnabled(true);
				btnPause.setEnabled(false);
			}
		});
		btnPause.setEnabled(false);
		btnPause.setBounds(185, 409, 48, 48);
		btnPause.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/PlayerPause.png")));
		contentPane.add(btnPause);
		
		lblHora = new JLabel("");
		lblHora.setHorizontalAlignment(SwingConstants.CENTER);
		lblHora.setFont(new Font("DejaVu Serif", Font.BOLD, 40));
		lblHora.setBounds(10, 43, 355, 98);
		contentPane.add(lblHora);
		
		JLabel btnAddMusic = new JLabel("");
		btnAddMusic.addMouseListener(new MouseAdapter() { // Botão adicionar musica
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Mp3 Files", "mp3"));
				fc.setAcceptAllFileFilterUsed(false);
				
				int result = fc.showOpenDialog(contentPane);
				
				// verifica se o arquivo foi selecionado.
				if (result == JFileChooser.APPROVE_OPTION) {
					
					File selectedFile = fc.getSelectedFile();
					musicas.add(selectedFile);
					tableModel.addRow(new String[]{selectedFile.getName()});
				}
				
			}
		});
		btnAddMusic.setBounds(10, 184, 32, 32);
		btnAddMusic.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/Add.png")));
		contentPane.add(btnAddMusic);
		
		JLabel btnRemoveMusic = new JLabel("");
		btnRemoveMusic.addMouseListener(new MouseAdapter() { // Botão de remover
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try {
					int row = table.getSelectedRow(); // pega a linha da tabela que o usuário clicou.
					
					if (row >= 0) {
						musicas.remove(row);
						tableModel.removeRow(row);
					} else 
						throw new Exception("Selecione uma musica");
				}catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane, e.getMessage());
				}
				
			}
		});
		btnRemoveMusic.setBounds(52, 184, 32, 32);
		btnRemoveMusic.setIcon(new ImageIcon(MainFrame.class.getResource("/br/edu/ifspcaraguatatuba/image/Delete.png")));
		contentPane.add(btnRemoveMusic);
		
		JButton btnFalarHora = new JButton("Falar hora");
		btnFalarHora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String file = "HRS" + relogio.getDate().getHours() + ".mp3";
				String file2 = "MIN" + relogio.getDate().getMinutes() + ".mp3";
				String path = System.getProperty("user.dir") + "/voices_time/";
				
				String audioHora = path + file;
				String audioMin  = path + file2;
				
				ArrayList<File> horas = new ArrayList<>();
				horas.add(new File(audioHora));
				horas.add(new File(audioMin));
				
				Tocador falaHora = new Tocador(horas);
				falaHora.play();
				
				
				System.out.println(path);
			}
		});
		btnFalarHora.setBounds(203, 190, 117, 25);
		contentPane.add(btnFalarHora);
		
	}
}
