package view;

import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AutoFormDiagonalViewController extends AutoFormViewController{

	public static final List<String> POSITIONS = Arrays.asList("Stage Center", "Current Position");
	public static final List<String> SIZES = Arrays.asList("Current Group", "Half Stage");
	public static final List<String> ORIENT = Arrays.asList("/", "\\");
	
	@FXML
	private ComboBox<String> positionCombobox;
	@FXML
	private ComboBox<String> sizeCombobox;
	@FXML
	private ComboBox<String> orientCombobox;

	private String position;
	private String size;
	private String orient;

	@Override
	@FXML
	void initialize() {
		positionCombobox.getItems().addAll(POSITIONS);
		positionCombobox.setValue(POSITIONS.get(0));
		sizeCombobox.getItems().addAll(SIZES);
		sizeCombobox.setValue(SIZES.get(1));
		orientCombobox.getItems().addAll(ORIENT);
		orientCombobox.setValue(ORIENT.get(1));
	}

	public AutoFormDiagonalViewController() {

	}

	@Override
	public void setStage(Stage dialogStage) {
		super.setStage(dialogStage);
		autoFormStage.setOnCloseRequest(event -> {
			position = CLOSE;
			size = CLOSE;
		});
	}

	@FXML
	void handleGenerate() {
		position = positionCombobox.getValue();
		size = sizeCombobox.getValue();

		autoFormStage.close();
	}

	@Override
	public List<String> getArgs() {
		return Arrays.asList(position, size, orient);
	}
	
}
