package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import multimidia.Musica;
import multimidia.PlayerHandler;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

public class Player extends JFrame  {
	private static final long serialVersionUID = 7731945534799370506L;
	private JPanel contentPane;
	private PlayerHandler player;
	private boolean playing = false;
	public JLabel lblleft;
	public JLabel lblprev;
	public JLabel lblpp;
	public JLabel lblrandom;
	public JLabel lblnext;
	public static JList list;
	public static int numMusica = -1;
	public static Vector <String> aux; 
	public static Vector <Musica> listaPorHumor;
	private JScrollPane scrollPane;
	/**
	 * Create the frame.
	 */
	public Player(final int valencia, final int ativacao, final Vector<Musica> listaPorHumor) {

		
		this.listaPorHumor = listaPorHumor;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 533, 496);
		setLocationRelativeTo(null); 
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);


		aux = new Vector <String>(); 
		for (Musica m : listaPorHumor){ 
			aux.add("   " + m.getNome()); 
		}

		list = new JList(aux); 
		list.setBounds(35, 84, 461, 269);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(new Color(204, 204, 255));
		list.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 16));
		list.setCellRenderer(new SelectedListCellRenderer());
		contentPane.setLayout(null);
		//contentPane.add(list);
		
		player = new PlayerHandler(listaPorHumor, numMusica, list);


		JLabel lblHumor = new JLabel("Humor : ");
		lblHumor.setBounds(158, 16, 103, 33);
		lblHumor.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 20));
		contentPane.add(lblHumor);

		JLabel lblHumor2 = new JLabel("");
		lblHumor2.setBounds(245, 16, 210, 33);
		lblHumor2.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 20));
		contentPane.add(lblHumor2);

		lblleft = new JLabel("");
		lblleft.setBounds(10, 16, 60, 43);
		lblleft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				player.stop();
				Player.this.dispose();
				aux.removeAllElements();
				listaPorHumor.removeAllElements();

			}
		});
		lblleft.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0){
				lblleft.setIcon(new ImageIcon(Player.class.getResource("/imgs/left_red2.png")));
			}
		});
		lblleft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0){
				lblleft.setIcon(new ImageIcon(Player.class.getResource("/imgs/left_red.png")));
			}
		});

		lblleft.setIcon(new ImageIcon(Player.class.getResource("/imgs/left_red.png")));
		contentPane.add(lblleft);



		lblprev = new JLabel("");
		lblprev.setBounds(129, 413, 80, 43);
		lblprev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red2.png")));
				try {
					lblpp.setIcon(new ImageIcon(Player.class.getResource("/imgs/pp_red2.png")));
					playing = true;
					numMusica = player.previous();
					list.getComponent(numMusica).setFont(new Font("Microsoft JhengHei UI", Font.BOLD, 16));
					
					list.setSelectedIndex(numMusica);
					SelectedListCellRenderer.getDefaultLocale();

				} catch (Exception e) {
					e.printStackTrace();
				}
				lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red.png")));
			}
		});
		lblprev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0){
				lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red2.png")));
			}
		});
		lblprev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0){
				lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red.png")));
			}
		});
		lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red.png")));
		contentPane.add(lblprev);


		lblpp = new JLabel("");
		lblpp.setBounds(234, 413, 70, 43);
		// esta funcionando como botao de play/pause.
		// execuçao da musica começa logo quando inicializa a janela,
		// sem o usuario precisar pressionar o play.
		lblpp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(playing == false){
					lblpp.setIcon(new ImageIcon(Player.class.getResource("/imgs/pp_red2.png")));
					playing = true;
					try{
						player.resume();
					} catch (BasicPlayerException e){
						e.printStackTrace();
					}
				}else{
					playing = false;
					lblpp.setIcon(new ImageIcon(Player.class.getResource("/imgs/pp_red.png")));
					player.pause();
				}
			}
		});
		lblpp.setIcon(new ImageIcon(Player.class.getResource("/imgs/pp_red2.png")));
		contentPane.add(lblpp);


		lblnext = new JLabel("");
		lblnext.setBounds(352, 413, 60, 43);
		lblnext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					playing = true;
					numMusica= player.next();
					System.out.println("numMusica : " + numMusica);
					list.setSelectedIndex(numMusica);
					SelectedListCellRenderer.getDefaultLocale();
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red.png")));
			}
		});
		lblnext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0){
				lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red2.png")));
			}
		});
		lblnext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0){
				lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red.png")));
			}
		});
		lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red.png")));
		contentPane.add(lblnext);


		this.setResizable(false);
		if((valencia == 0) && (ativacao==0)){
			lblHumor2.setText("Deprimido");
			System.out.println(lblHumor2.getText());
		}else if((valencia == 0) && (ativacao==1)){
			lblHumor2.setText("Estressado");
			System.out.println(lblHumor2.getText());
		}else if((valencia == 1) && (ativacao==0)){
			lblHumor2.setText("Relaxado");
			System.out.println(lblHumor2.getText());
		}else if((valencia == 1) && (ativacao==1)){
			lblHumor2.setText("Entusiasmado");
			System.out.println(lblHumor2.getText());
		}
		// execuçao da musica começa logo quando inicializa a janela,
		// sem o usuario precisar pressionar o play.
		playing = true;
		numMusica = player.next();
		list.setSelectedIndex(numMusica);
		SelectedListCellRenderer.getDefaultLocale();
		
		

		scrollPane = new JScrollPane(list);
		scrollPane.setBounds(35, 77, 461, 325);
		contentPane.add(scrollPane);
		//list.getComponent(numMusica).setFont(new Font("Microsoft JhengHei UI", Font.BOLD, 16));
	}

}





