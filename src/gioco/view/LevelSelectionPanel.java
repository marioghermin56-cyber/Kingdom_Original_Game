package gioco.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LevelSelectionPanel extends JPanel {

	public LevelSelectionPanel(ActionListener levelSelectionListener) {
		
		setLayout(new GridBagLayout());
		setBackground(Color.DARK_GRAY);
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridLayout(2,1, 0, 20));//3 righe, 1 colonna, spazio vericale di 20px
		buttonContainer.setOpaque(false);//rende il pannello trasparente
		
		JButton level1Button = createLevelButton("Gioca Livello 1", "1", levelSelectionListener);
		JButton level2Button = createLevelButton("Gioca Livello 2", "2", levelSelectionListener);
		
		buttonContainer.add(level1Button);
		buttonContainer.add(level2Button);
		
		add(buttonContainer);
	}
    
	private JButton createLevelButton(String text, String command, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setPreferredSize(new Dimension(250, 60));
        button.setActionCommand(command); // Questo "1", "2" o "3" verrà letto dal Controller!
        button.addActionListener(listener);
        return button;
    }
}
