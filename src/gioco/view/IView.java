package gioco.view;

import java.awt.event.ActionListener;
import gioco.model.IModel;

public interface IView {

	void render(IModel model);
	void showMessage(String message);
	double getScaleX();
	double getScaleY();
	
	// --- Metodi Audio ---
	void addMusicListener(ActionListener listener);
	void addSoundListener(ActionListener listener);
	void updateMusicIcon(boolean isMuted);
	void updateSoundIcon(boolean isMuted);
	
	// --- Metodi Bottoni Gioco ---
	void addArcherListener(ActionListener listener);
	void addMageListener(ActionListener listener);
	void addBarracksListener(ActionListener listener);
	void addCannonListener(ActionListener listener);
	void addUpgradeListener(ActionListener listener);
	void addRallyListener(ActionListener listener);
	
	// --- Metodi Gestione View ---
	void setStartButtonListener(ActionListener listener);
	void switchToGame();

}