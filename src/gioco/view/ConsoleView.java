package gioco.view;

import gioco.model.Enemy;
import gioco.model.IModel;
import gioco.model.TowerSlot;
import java.util.List;
import java.awt.event.ActionListener;


public class ConsoleView implements IView {

	@Override
	public void render(IModel model) {
		System.out.println("--GAME STATE--");
		System.out.println("Health : " + model.getPlayerHealth());
		System.out.println("Gold : " + model.getGold());
		System.out.println("Available Tower slots : ");
		
		List<TowerSlot> slots = model.getAvailableSlots();
		for(int i = 0; i <slots.size(); i++) {
			TowerSlot slot = slots.get(i);
			String status = slot.isOccupied() ?  "Occupied" : "Available";
			System.out.println("Slot " + i + " (x : " + slot.getX() + " y : " + slot.getY() + ") -> " + status );
		}
		System.out.println("-------------");
		System.out.println("Numero nemici attivi " + model.getActiveEnemies().size());
		for(Enemy e : model.getActiveEnemies()) {
			System.out.println("Nemico alle coordinate ( " + e.getX() + ", " + e.getY() + ")");
		}
	}
	
	@Override
	public void showMessage(String message) {
		System.out.println("MESSAGGIO >> " + message);
	}
	
	@Override
	public void addArcherListener(ActionListener listener) {
		// Non implementato per la console
	}
	
	public void addMageListener(ActionListener listener) {
		// Non implementato per la console
	}
	
	public void addCannonListener(ActionListener listener) {
		// Non implementato per la console
	}
	
	public void addBarracksListener(ActionListener listener) {
		// Non implementato per la console
	}

	 @Override
    public void addUpgradeListener(ActionListener listener) {
    	
    }

	@Override
	public void addRallyListener(ActionListener listener) {

	}

	@Override
	public void setStartButtonListener(ActionListener listener) {

	}
	@Override
	public void switchToGame() {

	}
}