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
    
    private boolean isMusicMuted = false;
	private boolean isSoundMuted = false;
	
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
        this.view.addMusicListener(e -> toggleMusic());
        this.view.addSoundListener(e -> toggleSound());
	}
	
	public void attachToPanel(JPanel panel) {
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
	    // 1. Chiediamo alla View la scala attuale
	    double scaleX = view.getScaleX();
	    double scaleY = view.getScaleY();
	    
	    // 2. Traduzione in coordinate logiche
	    int logicalX = (int) (e.getX() / scaleX);
	    int logicalY = (int) (e.getY() / scaleY);
	    
	    boolean slotClicked = false; // Ci serve per capire se abbiamo cliccato a vuoto
	    
	    checkSlotSelection(logicalX,logicalY);
	    
	    // 3. Nuova collisione perfetta con la Hitbox di Tiled!
	    for (TowerSlot slot : model.getAvailableSlots()) {
	        if (logicalX >= slot.getX() && logicalX <= slot.getX() + slot.getWidth() &&
	            logicalY >= slot.getY() && logicalY <= slot.getY() + slot.getHeight()) {
	            
	            // Abbiamo cliccato su uno slot!
	            model.selectBuildSlot(slot); // (O setSelectedBuildSlot, usa il nome che hai nel model)
	            slotClicked = true;
	            break;
	        }
	    }
	    
	    // 4. CHICCA UX: Se ho cliccato fuori da qualsiasi slot, chiudo il menu!
	    if (!slotClicked) {
	        model.selectBuildSlot(null);
	    }
	    
	    // 5. Ridisegniamo tutto alla fine
	    view.render(model);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	    // 1. Chiediamo alla View la scala attuale per il responsive design
	    double scaleX = view.getScaleX();
	    double scaleY = view.getScaleY();
	    
	    // 2. Traduciamo le coordinate del mouse in coordinate logiche
	    int logicalX = (int) (e.getX() / scaleX);
	    int logicalY = (int) (e.getY() / scaleY);
	    
	    // Variabile temporanea per capire cosa stiamo guardando ORA
	    TowerSlot currentlyHovered = null;
	    
	    // 3. Controlliamo la collisione perfetta con la hitbox di Tiled
	    for (TowerSlot slot : model.getAvailableSlots()) {
	        if (logicalX >= slot.getX() && logicalX <= slot.getX() + slot.getWidth() &&
	            logicalY >= slot.getY() && logicalY <= slot.getY() + slot.getHeight()) {
	            
	            // Abbiamo trovato lo slot sotto il mouse!
	            currentlyHovered = slot; 
	            break; // Usciamo dal ciclo, non serve cercare oltre
	        }
	    }
	    
	    // 4. PRESTAZIONI: Aggiorniamo il Model e ridisegniamo SOLO se c'è un cambiamento.
	    // Evita che Java faccia il "render" mille volte al secondo se muovi
	    // il mouse stando fermo sempre sullo stesso slot (o sul prato vuoto).
	    if (model.getHoveredSlot() != currentlyHovered) {
	        model.setHoveredSlot(currentlyHovered);
	        view.render(model);
	    }
	}
	


	
	private void checkSlotSelection(int logicalX, int logicalY) {
	    
	    // 1. Gestione Rally Point (Rimane identica, usa già le coordinate giuste)
	    if (model.isSettingRallyPoint()) {
	        model.setRallyPoint(logicalX, logicalY);
	        view.render(model);
	        return; // Esce subito, non fa nient'altro!
	    }
	    
	    // 2. Controllo click sulle hitbox di Tiled
	    List<TowerSlot> slots = model.getAvailableSlots();
	    for (int i = 0; i < slots.size(); i++) {
	        TowerSlot slot = slots.get(i);
	        
	        // Nuova collisione geometrica con width e height!
	        if (logicalX >= slot.getX() && logicalX <= slot.getX() + slot.getWidth() &&
	            logicalY >= slot.getY() && logicalY <= slot.getY() + slot.getHeight()) {
	                
	                model.selectBuildSlot(slot); // Apre il menu
	                return; // Trovato, usciamo dal metodo!
	        }
	    }
	    
	    // 3. Se il ciclo finisce e non ha trovato nulla, clic a vuoto -> chiudi menu
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
            gameTimer = new Timer(16, e -> {
                model.updateGame();      // Il Model aggiorna le posizioni di nemici/torri
                view.render(model); // La View ridisegna tutto basandosi sul nuovo stato
            });
            gameTimer.start();
        }
    }
    
    private void toggleMusic() {
        isMusicMuted = !isMusicMuted;
        view.updateMusicIcon(isMusicMuted);
    }

    private void toggleSound() {
        isSoundMuted = !isSoundMuted;
        view.updateSoundIcon(isSoundMuted);
    }
}
