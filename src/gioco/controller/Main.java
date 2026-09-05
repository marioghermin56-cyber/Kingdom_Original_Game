package gioco.controller;

import gioco.view.SwingView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            SwingView view = new SwingView();

            
            Controller controller = new Controller(view);

           
            controller.attachToPanel(view.getGamePanel());
            
           
        });
    }
}