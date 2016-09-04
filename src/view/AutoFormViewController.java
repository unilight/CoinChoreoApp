package view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class AutoFormViewController {
	public static final String CLOSE = "close";
	public Stage autoFormStage;
	
	@FXML
	abstract void initialize();
	
	public void setStage(Stage dialogStage) {
		this.autoFormStage = dialogStage;
	}
	
	@FXML
	abstract void handleGenerate();
	
	public abstract List<String> getArgs();
}
