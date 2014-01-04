package tools.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class LogPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogPanel(String message, String details){
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		
		JTextArea log = new JTextArea(20,100);
		log.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		log.setEditable(false);
		//log.setFont(Font.getFont("Courier New"));
		//message.replaceAll(System.getProperty("line.separator"), "\n");
		log.append(message);
		//log.setLineWrap(true);
		add(new JScrollPane(log),gbc);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 1;
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(new JLabel(details),gbc);
	}
}
