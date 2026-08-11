package gioco.controller;

import javax.swing.SwingUtilities;

import gioco.model.IModel;
import gioco.model.KingdomRushModel;
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
    private int maxUnlockedLevel; 
    
    private boolean isMusicMuted = false;
	private boolean isSoundMuted = false;
	
	public Controller(IView view) {
		this.view = view;
		this.maxUnlockedLevel = gioco.utils.SaveManager.loadProgress();
        this.view.updateUnlockedLevels(maxUnlockedLevel);
        
        this.view.addArcherListener(e -> attemptToBuildTower(this.model.getSelectedBuildSlot(), "ARCHER"));
        this.view.addMageListener(e -> attemptToBuildTower(this.model.getSelectedBuildSlot(), "MAGE"));
        this.view.addBarracksListener(e -> attemptToBuildTower(this.model.getSelectedBuildSlot(), "BARRACKS")); 
        this.view.addCannonListener(e -> attemptToBuildTower(this.model.getSelectedBuildSlot(), "CANNON"));
        
        this.view.addRallyListener(e -> {
            if(this.model != null) {
                TowerSlot slot = this.model.getSelectedBuildSlot();
                if(slot != null && slot.isOccupied() && slot.getTower().getType() == Tower.BARRACKS_TYPE) {
                    this.model.startSettingRallyPoint(slot);
                    this.model.deselectBuildSlot();
                    view.render(this.model);
                }
            }
        });
        
        this.view.addUpgradeListener(e -> {
            model.upgradeSelectedTower();
            view.render(model); 
        });

        this.view.setStartButtonListener(e -> {
            int levelNumber = Integer.parseInt(e.getActionCommand());
            startLevel(levelNumber); 
        });
        
        this.view.addMusicListener(e -> toggleMusic());
        this.view.addSoundListener(e -> toggleSound());
        
        this.view.addPauseListener(e -> {
        	model.togglePause();
        	view.render(model);
        });
        
        view.addRestartListener(e -> {
        	int levelNumber = model.getCurrentLevelNumber();
            startLevel(levelNumber); 
        });
        
        view.addQuitListener(e -> {
            if (gameTimer != null) gameTimer.stop();
            gioco.utils.SoundManager.playMusic("/assets/audio/audioMenu.wav");
            view.updateUnlockedLevels(maxUnlockedLevel);
            view.switchToMenu(); 
        });
	}
	
	private void startLevel(int levelNumber) {
        if (gameTimer != null) gameTimer.stop();
        
        this.model = new KingdomRushModel(levelNumber);
        
        if (!isMusicMuted) {
            gioco.utils.SoundManager.playMusic("/assets/audio/audioMenu2.wav"); 
        }
        
        view.render(this.model);
        view.switchToGame();
        
        gameTimer = new Timer(16, e -> {
            if (!this.model.isGameOver()) {
                this.model.updateGame();
                view.render(this.model);
                
                if (model.getCurrentWaveNumber() > model.getTotalWaves() && model.getActiveEnemies().isEmpty()) {
                    int currentLvl = model.getCurrentLevelNumber();
                    if (currentLvl >= maxUnlockedLevel) {
                        maxUnlockedLevel = currentLvl + 1;
                        
                        gioco.utils.SaveManager.saveProgress(maxUnlockedLevel);
                    }
                    view.render(this.model); 
                    ((Timer)e.getSource()).stop(); 
                }
                
            } else {
                view.render(this.model);
                ((Timer)e.getSource()).stop();
            }
        });
        gameTimer.start();
    }
	
	public void attachToPanel(JPanel panel) {
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.model == null) return;
	    double scaleX = view.getScaleX();
	    double scaleY = view.getScaleY();
	    
	    int logicalX = (int) (e.getX() / scaleX);
	    int logicalY = (int) (e.getY() / scaleY);
	    
	    boolean slotClicked = false; 
	    checkSlotSelection(logicalX,logicalY);
	    
	    for (TowerSlot slot : model.getAvailableSlots()) {
	        if (logicalX >= slot.getX() && logicalX <= slot.getX() + slot.getWidth() &&
	            logicalY >= slot.getY() && logicalY <= slot.getY() + slot.getHeight()) {
	            model.selectBuildSlot(slot); 
	            slotClicked = true;
	            break;
	        }
	    }
	    
	    if (!slotClicked) {
	        model.selectBuildSlot(null);
	    }
	    
	    view.render(model);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (this.model == null) return;
	    double scaleX = view.getScaleX();
	    double scaleY = view.getScaleY();
	    
	    int logicalX = (int) (e.getX() / scaleX);
	    int logicalY = (int) (e.getY() / scaleY);
	    
	    TowerSlot currentlyHovered = null;
	    
	    for (TowerSlot slot : model.getAvailableSlots()) {
	        if (logicalX >= slot.getX() && logicalX <= slot.getX() + slot.getWidth() &&
	            logicalY >= slot.getY() && logicalY <= slot.getY() + slot.getHeight()) {
	            currentlyHovered = slot; 
	            break; 
	        }
	    }
	    
	    if (model.getHoveredSlot() != currentlyHovered) {
	        model.setHoveredSlot(currentlyHovered);
	        view.render(model);
	    }
	}
	
	private void checkSlotSelection(int logicalX, int logicalY) {
	    if (model.isSettingRallyPoint()) {
	        model.setRallyPoint(logicalX, logicalY);
	        view.render(model);
	        return; 
	    }
	    
	    List<TowerSlot> slots = model.getAvailableSlots();
	    for (int i = 0; i < slots.size(); i++) {
	        TowerSlot slot = slots.get(i);
	        if (logicalX >= slot.getX() && logicalX <= slot.getX() + slot.getWidth() &&
	            logicalY >= slot.getY() && logicalY <= slot.getY() + slot.getHeight()) {
	                model.selectBuildSlot(slot); 
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
    
    private void toggleMusic() {
        isMusicMuted = !isMusicMuted;
        view.updateMusicIcon(isMusicMuted); 
        
        if (isMusicMuted) {
            gioco.utils.SoundManager.pauseMusic();
        } else {
            if (this.model == null) {
                gioco.utils.SoundManager.playMusic("/assets/audio/audioMenu.wav");
            } else {
                gioco.utils.SoundManager.playMusic("/assets/audio/audioMenu2.wav"); 
            }
        }
    }

    private void toggleSound() {
        isSoundMuted = !isSoundMuted;
        view.updateSoundIcon(isSoundMuted);
    }
}