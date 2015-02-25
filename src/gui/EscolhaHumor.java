package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JSlider;

import multimidia.Musica;
import multimidia.Principal;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

public class EscolhaHumor extends JFrame {

	private JPanel contentPane;
	private int valencia = 1; // 1 = feliz, 0 = triste
	private int ativacao = 1; // 1 =  agitado, 0 = calmo
	private final JSlider slider_1;
	private JLabel lbl_happy;
	private JLabel lbl_sad;
	private JLabel lbl_angry;
	private JLabel lbl_relax;
	private JLabel lbl_ok;
	private static JLabel invert;
	private static boolean inverte = false; 
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EscolhaHumor frame = new EscolhaHumor();
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
	public EscolhaHumor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 533, 496);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);
		
		JLabel lblEscolhaOSeu = new JLabel("Escolha o seu humor :");
		lblEscolhaOSeu.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		lblEscolhaOSeu.setBounds(23, 11, 167, 31);
		contentPane.add(lblEscolhaOSeu);
		
		lbl_happy = new JLabel("");
		lbl_happy.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/happy_emoji_red.png")));
		lbl_happy.setBounds(280, 69, 175, 140);
		lbl_happy.setVisible(true);
		contentPane.add(lbl_happy);
		
		lbl_angry = new JLabel("");
		lbl_angry.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/angry_emoji_red.png")));
		lbl_angry.setBounds(280, 266, 151, 140);
		lbl_angry.setVisible(false);
		contentPane.add(lbl_angry);
		
		lbl_sad = new JLabel("");
		lbl_sad.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/sad_emoji_red.png")));
		lbl_sad.setBounds(64, 266, 180, 146);
		lbl_sad.setVisible(false);
		contentPane.add(lbl_sad);
		
		final JSlider slider = new JSlider();
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				//System.out.println("soltou o mouse !");
				int point = slider.getValue();
				if(point>50){
					ativacao = 1;
					if(valencia == 0 ){
						lbl_happy.setVisible(false);
						lbl_relax.setVisible(false);
						lbl_sad.setVisible(false);
						lbl_angry.setVisible(true);
					}
					if(valencia == 1){
						lbl_happy.setVisible(true);
						lbl_relax.setVisible(false);
						lbl_sad.setVisible(false);
						lbl_angry.setVisible(false);			
					}
					
				}else if (point<50){
					ativacao = 0;
					if(valencia == 0 ){
						lbl_happy.setVisible(false);
						lbl_relax.setVisible(false);
						lbl_sad.setVisible(true);
						lbl_angry.setVisible(false);
					}
					if(valencia == 1){
						lbl_happy.setVisible(false);
						lbl_relax.setVisible(true);
						lbl_sad.setVisible(false);
						lbl_angry.setVisible(false);			
					}
					
					
					
				}
				
			}
		});
		slider.setValue(70);
		slider.setBounds(40, 220, 428, 35);
		//slider.setVisible(false);
		slider.setOpaque(false);
		contentPane.add(slider);
		
		
		Hashtable labelHorizSlider = new Hashtable();
		labelHorizSlider.put(new Integer (0), new JLabel("Calmo"));
		labelHorizSlider.put(new Integer (slider.getMaximum()), new JLabel ("Agitado"));
		slider.setLabelTable(labelHorizSlider);
		slider.setPaintLabels(true);
		
		
		slider_1 = new JSlider();
		slider_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				//System.out.println("soltou o mouse !");
				int point = slider_1.getValue();
				if(point>50){
					valencia = 1;
					if(ativacao==0){
						lbl_happy.setVisible(false);
						lbl_relax.setVisible(true);
						lbl_sad.setVisible(false);
						lbl_angry.setVisible(false);
					}
					else if (ativacao==1){
						lbl_happy.setVisible(true);
						lbl_relax.setVisible(false);
						lbl_sad.setVisible(false);
						lbl_angry.setVisible(false);
					}
					
				}else if (point<50){
					valencia = 0;
					if(ativacao==0){
						lbl_happy.setVisible(false);
						lbl_relax.setVisible(false);
						lbl_sad.setVisible(true);
						lbl_angry.setVisible(false);
					}
					else if (ativacao==1){
						lbl_happy.setVisible(false);
						lbl_relax.setVisible(false);
						lbl_sad.setVisible(false);
						lbl_angry.setVisible(true);
					}
					
				}
				
			}
		});
		slider_1.setValue(70);
		slider_1.setOrientation(SwingConstants.VERTICAL);
		slider_1.setOpaque(false);
		slider_1.setBounds(242, 49, 50, 372);
		
		contentPane.add(slider_1);
		
		Hashtable labelVertSlider = new Hashtable();
		labelVertSlider.put(new Integer (0), new JLabel("Triste"));
		labelVertSlider.put(new Integer (slider_1.getMaximum()), new JLabel ("Feliz"));
		slider_1.setLabelTable(labelVertSlider);
		slider_1.setPaintLabels(true);
		
		
		lbl_relax = new JLabel("");
		lbl_relax.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/relaxed_emoji_red.png")));
		lbl_relax.setBounds(64, 69, 194, 149);
		contentPane.add(lbl_relax);
		
		invert = new JLabel("");
		invert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(inverte == true){
					inverte= false;
					invert.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/invert_red.png")));
					
					if(valencia == 0){
						valencia =1;
					}else{
						valencia =0;
					}
					
					
					if(ativacao == 0){
						ativacao =1;
					}else{
						ativacao =0;
					}
					
					System.out.println(" valencia :" +valencia +", ativacao :" + ativacao);
					
				}else{
					inverte = true;
					invert.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/invert_red2.png")));
					
					//para voltar 
					if(valencia == 0){
						valencia =1;
					}else{
						valencia =0;
					}
					
					
					if(ativacao == 0){
						ativacao =1;
					}else{
						ativacao =0;
					}
					
					System.out.println(" valencia :" +valencia +", ativacao :" + ativacao);
				}
				
				
			}
		});
		invert.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/invert_red.png")));
		invert.setHorizontalAlignment(SwingConstants.TRAILING);
		invert.setBounds(-27, 412, 85, 55);
		contentPane.add(invert);
		
		lbl_ok = new JLabel("");
		lbl_ok.setIcon(new ImageIcon(EscolhaHumor.class.getResource("/imgs/ok.png")));
		lbl_ok.setBounds(477, 427, 60, 40);
		contentPane.add(lbl_ok);
		lbl_ok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try {
					Principal.echonestMagic();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ArrayList<Musica> listaPorHumor = splitList(valencia, ativacao);
				Player framePlayer = new Player(valencia, ativacao,listaPorHumor);
				framePlayer.setVisible(true);
					
			
			}
		});
		
		lbl_relax.setVisible(false);
		ImageIcon aux = new ImageIcon(EscolhaHumor.class.getResource("/imgs/happy_emoji_red.png"));
	}
	
	
	public static ArrayList<Musica> splitList(int valencia, int ativacao){
		ArrayList<Musica> lista = Principal.listaMusicas;
		ArrayList<Musica> res = new ArrayList<Musica>();
		for(Musica m : lista){
			System.out.println("valencia : " + m.getValencia() + " Ativacao "+ m.getAtivacao() + " v " + valencia + " a "+ ativacao);
			if(m.getValencia() == valencia && m.getAtivacao() == ativacao){
				res.add(m);
			}
		}
		System.out.println(res.size());
		return res;
	
	}
}