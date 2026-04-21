package gioco.view;

import java.awt.Desktop.Action;
import java.awt.event.ActionListener;
import gioco.model.IModel;

public interface IView {

	void render(IModel model);
	void showMessage(String message);
	double getScaleX();
	double getScaleY();
	void addArcherListener(ActionListener listener);
	void addMageListener(ActionListener listener);
	void addBarracksListener(ActionListener listener);
	void addCannonListener(ActionListener listener);
	void addUpgradeListener(ActionListener listener);
	void addRallyListener(ActionListener listener);
	void setStartButtonListener(ActionListener listener);
	void switchToGame();

}
