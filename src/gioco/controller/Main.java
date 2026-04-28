package gioco.controller;

import gioco.view.SwingView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Creiamo la View (la parte grafica)
            SwingView view = new SwingView();

            // 2. Creiamo il Controller passandogli solo la View.
            // Non passiamo più il Model qui!
            Controller controller = new Controller(view);

            // 3. Colleghiamo i click del mouse al pannello di gioco
            controller.attachToPanel(view.getGamePanel());
            
            // Il programma ora resta in attesa nel Menu Principale.
        });
    }
}