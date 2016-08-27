package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewFileViewController {
	
	@FXML
	private TextField numOfDancers;
	
	private int num;
	private Stage dialogStage;
	private boolean createClicked = false;
	
	@FXML
	private void initialize(){
		
	}
	
	public NewFileViewController(){
		
	}
	
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	public int getNumOfDancers(){
		return num;
	}
	
	@FXML
	public int isCreateClicked() {
        return num;
    }
	
	@FXML
    private void handleCreate() {
        if (isInputValid()) {
        	
        	num = Integer.parseInt(numOfDancers.getText());
        	
            createClicked = true;
            dialogStage.close();
        }
    }

	private boolean isInputValid() {
        String errorMessage = "";

        if (numOfDancers.getText() == null || numOfDancers.getText().length() == 0) {
            errorMessage += "Please enter number of dancers.\n"; 
        }else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(numOfDancers.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid number (must be an integer)!\n"; 
            }
            if(Integer.parseInt(numOfDancers.getText())<0 || Integer.parseInt(numOfDancers.getText())>30 ){
            	errorMessage += "Please enter number from 1 to 30.\n";
            }
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
        
	}
	
}
