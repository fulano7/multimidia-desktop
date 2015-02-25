package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import multimidia.Musica;
<<<<<<< HEAD
import multimidia.PlayerHandler;
=======
>>>>>>> 0dc3ed2366d8d7240567dea2fd58c1bb39842b6f
import multimidia.Principal;

import java.awt.Font;
import java.util.ArrayList;

<<<<<<< HEAD
import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

=======
>>>>>>> 0dc3ed2366d8d7240567dea2fd58c1bb39842b6f
public class Player extends JFrame {

	private JPanel contentPane;
	private static int valencia;
	private static int ativacao;
	private static ArrayList<Musica> listaPorHumor;
<<<<<<< HEAD
	private PlayerHandler player = new PlayerHandler();
=======
>>>>>>> 0dc3ed2366d8d7240567dea2fd58c1bb39842b6f
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Player frame = new Player(valencia, ativacao,listaPorHumor);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
<<<<<<< HEAD
	public Player(final int valencia, final int ativacao,final ArrayList<Musica> listaPorHumor) {
=======
	public Player(final int valencia, final int ativacao,ArrayList<Musica> listaPorHumor) {
>>>>>>> 0dc3ed2366d8d7240567dea2fd58c1bb39842b6f
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 533, 496);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//JList list = new JList();
		//list.setBackground(Color.WHITE);
		//list.setBounds(43, 387, 420, -321);
		//contentPane.add(list);
		
		JLabel lblHumor = new JLabel("Humor : ");
		lblHumor.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		lblHumor.setBounds(21, 16, 90, 33);
		contentPane.add(lblHumor);
		
		
		JLabel lblHumor2 = new JLabel("");
		lblHumor2.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		lblHumor2.setBounds(70, 16, 210, 33);
		contentPane.add(lblHumor2);
<<<<<<< HEAD
		
		final JLabel lblleft = new JLabel("");
		lblleft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("clicouu!");
				//lblleft.setIcon(new ImageIcon(Player.class.getResource("/imgs/left_red2.png")));
				//try {
				//	Thread.sleep(1500);
				//} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				//Player.this.dispose();
				player.setCurrent(listaPorHumor.get(0).getCaminho());
				while (player.stopped==false);
				player.setCurrent(listaPorHumor.get(1).getCaminho());
				
			}
		});
		lblleft.setIcon(new ImageIcon(Player.class.getResource("/imgs/left_red.png")));
		lblleft.setBounds(21, 413, 70, 43);
		contentPane.add(lblleft);
		
		JLabel lblprev = new JLabel("");
		lblprev.setIcon(new ImageIcon(Player.class.getResource("/imgs/back_red.png")));
		lblprev.setBounds(129, 413, 80, 43);
		contentPane.add(lblprev);
		
		JLabel lblpp = new JLabel("");
		lblpp.setIcon(new ImageIcon(Player.class.getResource("/imgs/pp_red.png")));
		lblpp.setBounds(234, 413, 70, 43);
		contentPane.add(lblpp);
		
		JLabel lblnext = new JLabel("");
		lblnext.setIcon(new ImageIcon(Player.class.getResource("/imgs/next_red.png")));
		lblnext.setBounds(352, 413, 60, 43);
		contentPane.add(lblnext);
		
		JLabel lblrandom = new JLabel("");
		lblrandom.setIcon(new ImageIcon(Player.class.getResource("/imgs/random_red.png")));
		lblrandom.setBounds(443, 413, 74, 43);
		contentPane.add(lblrandom);
=======
>>>>>>> 0dc3ed2366d8d7240567dea2fd58c1bb39842b6f
		this.valencia = valencia;
		this.ativacao = ativacao;
		this.listaPorHumor = listaPorHumor;
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
		
		
	}
<<<<<<< HEAD
=======
	
>>>>>>> 0dc3ed2366d8d7240567dea2fd58c1bb39842b6f
}
