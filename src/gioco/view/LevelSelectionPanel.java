package gioco.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LevelSelectionPanel extends JPanel {

	public LevelSelectionPanel(ActionListener levelSelectionListener) {
		
		setLayout(new GridBagLayout());
		setBackground(Color.DARK_GRAY);
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridLayout(3,1, 0, 20));
		buttonContainer.setOpaque(false);
		
		JButton level1Button = createLevelButton("Gioca Livello 1", "1", levelSelectionListener);
		JButton level2Button = createLevelButton("Gioca Livello 2", "2", levelSelectionListener);
		JButton level3Button = createLevelButton("Gioca Livello 3", "3", levelSelectionListener);
		
		buttonContainer.add(level1Button);
		buttonContainer.add(level2Button);
		buttonContainer.add(level3Button);
		
		add(buttonContainer);
	}
    
	private JButton createLevelButton(String text, String command, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setPreferredSize(new Dimension(250, 60));
        button.setActionCommand(command); 
        button.addActionListener(listener);
        return button;
    }
}
