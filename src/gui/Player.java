package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import multimidia.Musica;
import multimidia.PlayerHandler;

public class Player extends JFrame {
	private static final long serialVersionUID = 7731945534799370506L;

	private JPanel contentPane;
	private PlayerHandler player;
	private boolean playing = false;
	public JLabel lblleft;
	public JLabel lblprev;
	public JLabel lblpp;
	public JLabel lblrandom;
	public JLabel lblnext;

	/**
	 * Create the frame.
	 */
	public Player(final int valencia, final int ativacao, final Vector<Musica> listaPorHumor) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 533, 496);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		player = new PlayerHandler(listaPorHumor);

		JList<Musica> list = new JList<Musica>(listaPorHumor);
		list.setBackground(Color.WHITE);
		list.setBounds(43, 387, 420, -321);
		contentPane.add(list);

		JLabel lblHumor = new JLabel("Humor : ");
		lblHumor.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		lblHumor.setBounds(21, 16, 90, 33);
		contentPane.add(lblHumor);

		JLabel lblHumor2 = new JLabel("");
		lblHumor2.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		lblHumor2.setBounds(70, 16, 210, 33);
		contentPane.add(lblHumor2);

		lblleft = new JLabel("");
		lblleft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				lblleft.setIcon(new ImageIcon(Player.class.getResource("/imgs/left_red2.png")));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				 
					e.printStackTrace();
				}
				Player.this.dispose();


			}
		});
		lblleft.setIcon(new ImageIcon(Player.class.getResource("/imgs/left_red.png")));
		lblleft.setBounds(21, 413, 70, 43);
		contentPane.add(lblleft);

		lblprev = new JLabel("");
		lblprev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red2.png")));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				 
					e.printStackTrace();
				}
				
				lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red.png")));
				
			}
		});
		lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red.png")));
		lblprev.setBounds(129, 413, 80, 43);
		contentPane.add(lblprev);

		lblpp = new JLabel("");
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
		lblpp.setIcon(new ImageIcon(Player.class.getResource("/imgs/pp_red.png")));
		lblpp.setBounds(234, 413, 70, 43);
		contentPane.add(lblpp);

		lblnext = new JLabel("");
		lblnext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red2.png")));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				 
					e.printStackTrace();
				}
				
				lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red.png")));
			}
		});
		lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red.png")));
		lblnext.setBounds(352, 413, 60, 43);
		contentPane.add(lblnext);

		lblrandom = new JLabel("");
		lblrandom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblrandom.setIcon(new ImageIcon(Player.class.getResource("/imgs/random_red2.png")));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				 
					e.printStackTrace();
				}
				
				lblrandom.setIcon(new ImageIcon(Player.class.getResource("/imgs/random_red.png")));
			}
		});
		lblrandom.setIcon(new ImageIcon(Player.class.getResource("/imgs/random_red.png")));
		lblrandom.setBounds(443, 413, 74, 43);
		contentPane.add(lblrandom);
		System.out.println("size : "+ listaPorHumor.size());
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
		player.next();
	}

}
