package control;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import view.StartViewController;
import view.NewFileViewController;
import view.WorkViewController;
import model.Proj;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private int curNum;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Choreographer");

		initRootLayout();
		showStartView();
	}
	
	public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout, 720, 480);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
	}
	
	public void showStartView() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/StartView.fxml"));
            GridPane startView = (GridPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(startView);
            
            StartViewController controller = loader.getController();
            controller.setMainApp(this);
            System.out.print("Set to mainApp.\n");
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void testing(){
		System.out.println("testing\n");
	}

	/**
	 * Opens a dialog.
	 * 
	 */
	public int showNewFileDialog() {
		try{
	        // Load the fxml file and create a new stage for the popup dialog.
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../view/NewFileView.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("New File");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        NewFileViewController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();
	        
	        return controller.isCreateClicked();
	        
	    } catch (IOException e){
	    	e.printStackTrace();
	    	return 0;
	    }
	}
	    
	public void showWorkView(int _num){
		try {
            // Load working overview
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/WorkView.fxml"));
            AnchorPane workView = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(workView);
            
            WorkViewController controller = loader.<WorkViewController>getController();
        	assert(controller != null);
            controller.setMainApp(this);
            controller.setProj(new Proj(_num));
            System.out.print("Set to mainApp.\n");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void setCurNum(int _num){
		this.curNum=_num;
	}
	
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
