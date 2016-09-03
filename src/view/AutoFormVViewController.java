package view;

import java.util.Arrays;
import java.util.List;

import com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d.PositionInterpolatorState;
import com.sun.xml.internal.ws.Closeable;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AutoFormVViewController {

	private static final List<String> POSITIONS = Arrays.asList("Stage Center", "Current Position");
	private static final List<String> SIZES = Arrays.asList("Current Group", "Half Stage");
	private static final String CLOSE = "close";

	@FXML
	public ComboBox<String> positionCombobox;
	@FXML
	public ComboBox<String> sizeCombobox;

	private Stage autoFormStage;

	private String position;
	private String size;

	@FXML
	private void initialize() {
		positionCombobox.getItems().addAll(POSITIONS);
		positionCombobox.setValue(POSITIONS.get(0));
		sizeCombobox.getItems().addAll(SIZES);
		sizeCombobox.setValue(SIZES.get(0));

	}

	public AutoFormVViewController() {

	}

	public void setStage(Stage dialogStage) {
		this.autoFormStage = dialogStage;
		autoFormStage.setOnCloseRequest(event -> {
			position = CLOSE;
			size = CLOSE;
		});
	}

	@FXML
	private void handleGenerate() {
		position = positionCombobox.getValue();
		size = sizeCombobox.getValue();

		autoFormStage.close();
	}

}
