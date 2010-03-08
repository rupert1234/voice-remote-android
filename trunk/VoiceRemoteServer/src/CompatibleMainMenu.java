import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class CompatibleMainMenu extends JFrame {

	public CompatibleMainMenu()
	{
		setTitle("VoiceRemote menu");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel=new JPanel();
		
		panel.setLayout(new GridLayout(3,1));
		
		JButton settings=new JButton("Show settings");
		settings.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent e) {
				Main.settings.setVisible(true);
			}
		});
		panel.add(settings);
		
		JButton mic=new JButton("Show microphone");
		mic.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent arg0) {
				Main.microphone_input.setVisible(true);				
			}
		});
		panel.add(mic);
		
		JButton exit=new JButton("Exit");
		exit.addActionListener(new AbstractAction() {			
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		panel.add(exit);
		
		add(panel);
		
		setSize(new Dimension(300,300));
		
		setVisible(true);
	}
	
}
