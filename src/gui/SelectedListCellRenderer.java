package gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;



public class SelectedListCellRenderer extends DefaultListCellRenderer {
	
 
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (index == Player.numMusica) {
			c.setFont(new Font("Microsoft JhengHei UI", Font.BOLD, 16));

		}
		
		System.out.println("index " + index+ ",  numMusica : "+ Player.numMusica);
		return c;
	}
}
