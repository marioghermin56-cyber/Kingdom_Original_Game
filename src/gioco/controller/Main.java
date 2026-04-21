package gioco.controller;

import gioco.model.IModel;
import gioco.model.KingdomRushModel;
import gioco.view.*;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main {
//matti sei un coglione//
    public static void main(String[] args) {
        // L'interfaccia grafica Swing va gestita nel suo thread specifico
        SwingUtilities.invokeLater(() -> {
            // 1. Inizializziamo l'MVC
            // Usiamo il Model esistente, ma la NUOVA SwingView
            IModel model = new KingdomRushModel(20, 300); // Più oro per testare
            SwingView view = new SwingView();
            Controller controller = new Controller(model, view);

            // 2. Colleghiamo il Controller al pannello grafico per i click
            controller.attachToPanel(view.getGamePanel());


            // 4. CREIAMO IL GAME LOOP (Timer automatico)
            // Spara un evento ogni 33ms (circa 30 FPS)
            Timer gameLoop = new Timer(100, (e) -> {
                if (!model.isGameOver()) {
                    
                    // A. Aggiorna Logica (Movimento, Spari, Collisioni)
                    model.updateGame();

                    // B. Ridisegna la Grafica
                    view.render(model);


                } else {
                    // Se è game over, fermiamo il loop e ridisegniamo un'ultima volta
                    view.render(model);
                    ((Timer)e.getSource()).stop();
                }
            });

            // 5. VIA! Parte l'azione.
            gameLoop.start();
        });
    }
}