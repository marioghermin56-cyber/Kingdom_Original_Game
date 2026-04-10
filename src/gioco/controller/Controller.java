package gioco.controller;

import javax.swing.SwingUtilities;

import gioco.model.IModel;
import gioco.model.Tower;
import gioco.model.TowerSlot;
import gioco.view.IView;
import gioco.view.SwingView;
import javax.swing.Timer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;

public class Controller extends MouseAdapter{

	private IModel model;
	private IView view;
    private Timer gameTimer;
	
	public Controller(IModel model, IView view) {
		this.model = model;
		this.view = view;
		
		this.view.addArcherListener(e -> attemptToBuildTower(model.getSelectedBuildSlot(), "ARCHER"));
        this.view.addMageListener(e -> attemptToBuildTower(model.getSelectedBuildSlot(), "MAGE"));
        this.view.addBarracksListener(e -> attemptToBuildTower(model.getSelectedBuildSlot(), "BARRACKS")); 
        this.view.addCannonListener(e -> attemptToBuildTower(model.getSelectedBuildSlot(), "CANNON"));
        this.view.addRallyListener(e ->{
        	TowerSlot slot = model.getSelectedBuildSlot();
        			if(slot != null && slot.isOccupied() && slot.getTower().getType() == Tower.BARRACKS_TYPE) {
        				
        				model.startSettingRallyPoint(slot);
        				model.deselectBuildSlot();
        				view.render(model);
        			}
        });
        this.view.addUpgradeListener(e -> {
            model.upgradeSelectedTower();
            view.render(model); // Ridisegna lo schermo per mostrare i soldi aggiornati!
        });

        this.view.setStartButtonListener(e -> {
            if (e.getActionCommand().equals("START")) {
                // Invece di chiamare solo la view, avviamo il metodo completo!
                startGame(); 
            }
        });
	}
	
	public void attachToPanel(JPanel panel) {
        panel.addMouseListener(this);
    }
	
	@Override
    public void mouseClicked(MouseEvent e) {
        // Quando l'utente clicca, controlliamo se ha preso uno slot
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        checkSlotSelection(mouseX, mouseY);

    }
	


	
	private void checkSlotSelection(int mx, int my) {
		
		if (model.isSettingRallyPoint()) {
            model.setRallyPoint(mx, my);
            view.render(model);
            return; // Esce subito, non fa nient'altro!
        }
		
        List<TowerSlot> slots = model.getAvailableSlots();
        for (int i = 0; i < slots.size(); i++) {
            TowerSlot slot = slots.get(i);
            
            // Collisione click con lo slot
            if (mx >= slot.getX() - 20 && mx <= slot.getX() + 20 &&
                my >= slot.getY() - 20 && my <= slot.getY() + 20) {
                    model.selectBuildSlot(slot); // Apre il menu
                    return;
            }
        }
        model.deselectBuildSlot();
    }
	
    

    public void attemptToBuildTower(TowerSlot slot,String typeToBuild) {
		boolean succes = model.buildTower(slot , typeToBuild);
		if(!succes) {
			view.showMessage("Failed to build tower. Not enough gold or slot index not available");
		}
	}
    /*view.setStartButtonListener(e -> {
    if (e.getActionCommand().equals("START")) {
        view.switchToGame();
        // Fai partire il timer del gioco qui!
    }
    })*/;
	

    private void startGame() {
        // 1. Ordina alla View di cambiare pannello
        view.switchToGame();
        
        // 2. Crea e avvia il loop del gioco (20ms = ~50 FPS)
        if (gameTimer == null) { // Evita di far partire più timer se si clicca più volte
            gameTimer = new Timer(20, e -> {
                model.updateGame();      // Il Model aggiorna le posizioni di nemici/torri
                view.render(model); // La View ridisegna tutto basandosi sul nuovo stato
            });
            gameTimer.start();
        }
    }
}
