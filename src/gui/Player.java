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
import multimidia.Principal;

import java.awt.Font;
import java.util.ArrayList;

public class Player extends JFrame {

	private JPanel contentPane;
	private static int valencia;
	private static int ativacao;
	private static ArrayList<Musica> listaPorHumor;
	
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
	public Player(final int valencia, final int ativacao,ArrayList<Musica> listaPorHumor) {
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
	
}
