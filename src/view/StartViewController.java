package view;

import control.MainApp;
import javafx.fxml.FXML;

public class StartViewController {

	
	private MainApp mainApp;
	
	public StartViewController(){
		
	}
	
	@FXML
	public void initialize(){
		
	}
	
	public void setMainApp(MainApp _mainApp){
		this.mainApp = _mainApp;
	}
	
	@FXML
	private void handleNew(){
		int num = mainApp.showNewFileDialog();
		System.out.println(num);
		mainApp.showWorkView(num);
	}
	
}
