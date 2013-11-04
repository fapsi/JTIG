package tools.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ButtonTabComponent extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	 
    public ButtonTabComponent(final JTabbedPane pane) {

        setOpaque(false);
        //make JLabel read titles from JTabbedPane
        JLabel label = new JLabel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        add(label);
        
        JButton button = new JButton("x");
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(41,10));
        button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = pane.indexOfTabComponent(ButtonTabComponent.this);
	            if (i != -1) {
	                pane.remove(i);
	            }
			}
		});
        add(button);
        
    }
}
