package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import control.MainApp;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.lang.Math;
import model.Proj;
import model.CircleTranslate;
import model.Dancer;
import model.IndexManager;
import model.Keyframe;

public class WorkViewController {

	public final static int CIRCLE_RADIUS = 10;
	public final static int KEYFRAME_CIRCLE_RADIUS = 4;
	public final static int SLIDER_WIDTH = 625;
	public final static int SLIDER_X = 18;
	public final static int SLIDER_Y = 8;
	public final static int SELECT_RADIUS = 12;
	public final static int LINE_WIDTH = 2;
	public final static int PANE_WIDTH = 720;
	public final static int PANE_HEIGHT = 480;
	public final static Duration DURATION_BEGIN = new Duration(-1);

	@FXML
	private Label time;
	@FXML
	private Slider slider;

	@FXML
	private ToggleGroup toggleGroup;
	@FXML
	private ToggleButton addToggle;
	@FXML
	private ToggleButton groupToggle;
	@FXML
	private ToggleButton deleteToggle;
	@FXML
	private AnchorPane drawPane;
	@FXML
	private AnchorPane keyframePane;

	private Proj curProj;

	private IndexManager indexManager;
	private List<CircleTranslate> circleTranslates = new ArrayList<CircleTranslate>();
	private List<Circle> groupedCircles = new ArrayList<Circle>();
	private List<Path> groupedPaths = new ArrayList<Path>();

	private double orgSceneX, orgSceneY;

	private ObservableList<Keyframe> timeline = FXCollections.observableArrayList();

	/* Music */
	private String path = "music/Kim Bum Soo (김범수) - 욕심쟁이 (Feat. San E) [8집 HIM].mp3";
	Media media;
	MediaPlayer mediaPlayer;
	MediaView mediaView;
	Duration duration;

	private Stage dialogStage;
	private MainApp mainApp;

	@FXML
	private void initialize() {
		groupToggle.setDisable(true);
		addToggle.setDisable(true);
		deleteToggle.setDisable(true);
	}

	public WorkViewController() {

	}

	public void setProj(Proj _proj) {
		this.curProj = _proj;
		indexManager = new IndexManager(curProj.getNumOfDancers());

		// Dancer Init
		// maxNum = curProj.getNumOfDancers();
	}

	public void setMainApp(MainApp _mainApp) {
		this.mainApp = _mainApp;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private void printX(MouseEvent e) {
		System.out.format("x:%f, y:%f%n", e.getSceneX(), e.getSceneY());
		System.out.format("x:%f, y:%f%n", e.getScreenX(), e.getScreenY());
	}

	@FXML
	private void drawCircle(MouseEvent e) {
		// System.out.println(e.getSource());
		// System.out.println(e.getTarget());
		// System.out.println(e.getEventType());
		System.out.format("x:%f, y:%f%n", e.getSceneX(), e.getSceneY());
		System.out.format("x:%f, y:%f%n", e.getScreenX(), e.getScreenY());
		System.out.println(e.getTarget().toString());

		double x = e.getSceneX();
		double y = e.getSceneY() - PANE_HEIGHT / 2 + 5;

		if (!(e.getTarget() instanceof Circle)) {

			if (addToggle.isSelected() && curProj.getCurrentDancersDrawn() < curProj.getNumOfDancers()) {

				// 先新建一個Keyframe
				Keyframe newKeyFrame = new Keyframe(circleTranslates, mediaPlayer.getCurrentTime());
				addKeyframePane(newKeyFrame);
				for (int i = 0; i < timeline.size(); i++) {
					// 在此時間點已經有keyframe, 則直接break
					if (timeline.get(i).getTime().toMillis() == newKeyFrame.getTime().toMillis()) {
						break;
					}
					if (timeline.get(i).getTime().toMillis() > newKeyFrame.getTime().toMillis()) {
						timeline.add(i, newKeyFrame);
						break;
					}
				}

				int index = indexManager.deque();

				Circle circle = new Circle(x, y, CIRCLE_RADIUS, Proj.colors[index]);
				circle.setOnMousePressed(circleOnMousePressedEventHandler);
				circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
				circle.setOnMouseReleased(circleOnMouseReleaseEventHandler);
				circle.setOnMouseMoved(circleOnMouseMovedEventHandler);

				CircleTranslate circleTranslate = new CircleTranslate(index);
				circle.translateXProperty().bind(circleTranslate.getDpTranslateX());
				circle.translateYProperty().bind(circleTranslate.getDpTranslateY());
				// circleTranslates.add(index, new CircleTranslate(index));
				// circleTranslates.set(index, new CircleTranslate(index));
				// circle.translateXProperty().bind(circleTranslates.get(index).getDpTranslateX());
				// circle.translateYProperty().bind(circleTranslates.get(index).getDpTranslateY());

				Path path = drawSelectionPath(x, y);
				path.setVisible(false);
				path.translateXProperty().bind(circleTranslate.getDpTranslateX());
				path.translateYProperty().bind(circleTranslate.getDpTranslateY());
				// path.translateXProperty().bind(circleTranslates.get(index).getDpTranslateX());
				// path.translateYProperty().bind(circleTranslates.get(index).getDpTranslateY());

				circleTranslates.add(circleTranslate);

				Dancer newDancer = new Dancer(index, circle, path);
				curProj.addDancer(newDancer);
				groupedPaths.add(path);

				drawPane.getChildren().addAll(circle, path);

				for (int i = 0; i < timeline.size(); i++) {
					CircleTranslate newCircleTranslate = new CircleTranslate(circleTranslate.getIndex(), circleTranslate.getTranslateX(), circleTranslate.getTranslateY());
					timeline.get(i).getCircleTranslates().add(newCircleTranslate);
				}
				listTimeline();

				// dancers.add(new Dancer(currentDancersDrawn, x, y));

			}

			// clear the group
			if (groupedCircles.size() > 0) {
				for (Circle circle : groupedCircles) {
					int index = curProj.getDancerIndex(circle);
					groupedPaths.get(index).setVisible(false);
				}
				groupedCircles.clear();
				// System.out.println(groupedCircles.size()+"");
			}
		}

	}

	private void addKeyframePane(Keyframe newKeyFrame) {
		double keyframeTime = newKeyFrame.getTime().toMillis();
		double x = SLIDER_X + SLIDER_WIDTH * keyframeTime / duration.toMillis();
		Circle paneCircle = new Circle(x, SLIDER_Y, KEYFRAME_CIRCLE_RADIUS, Color.GOLD);
		paneCircle.setStroke(Color.BLACK);
		paneCircle.setStrokeType(StrokeType.OUTSIDE);
		paneCircle.setStrokeWidth(1);
		paneCircle.setOnMouseMoved(paneCircleOnMouseMovedEventHandler);
		paneCircle.setOnMousePressed(paneCircleOnMousePressedEventHandler);
		newKeyFrame.setPaneCircle(paneCircle);
		keyframePane.getChildren().add(paneCircle);
	}

	private void listTimeline() {
		System.out.println("============");
		for (int i = 0; i < timeline.size(); i++) {
			System.out.println(timeline.get(i).toString());
		}
	}

	private void listCircleTranslates() {
		System.out.println("====CircleTrans========");
		for (int i = 0; i < circleTranslates.size(); i++) {
			System.out.print(circleTranslates.get(i).getIndex() + "\t" + circleTranslates.get(i).getTranslateX() + " " + circleTranslates.get(i).getTranslateY() + "\t");
		}
		System.out.println("");
	}

	private Path drawSelectionPath(double x, double y) {
		Path path = new Path();
		path.setStroke(Color.RED);
		path.setStrokeWidth(3);
		MoveTo moveTo = new MoveTo();
		moveTo.setX(x - CIRCLE_RADIUS);
		moveTo.setY(y);
		ArcTo arcToInner = new ArcTo();
		arcToInner.setX(x + CIRCLE_RADIUS);
		arcToInner.setY(y);
		arcToInner.setRadiusX(CIRCLE_RADIUS);
		arcToInner.setRadiusY(CIRCLE_RADIUS);
		MoveTo moveTo2 = new MoveTo();
		moveTo2.setX(x + CIRCLE_RADIUS);
		moveTo2.setY(y);
		ArcTo arcToInner2 = new ArcTo();
		arcToInner2.setX(x - CIRCLE_RADIUS);
		arcToInner2.setY(y);
		arcToInner2.setRadiusX(CIRCLE_RADIUS);
		arcToInner2.setRadiusY(CIRCLE_RADIUS);
		path.getElements().addAll(moveTo, arcToInner, moveTo2, arcToInner2);
		return path;
	}

	EventHandler<MouseEvent> paneCircleOnMouseMovedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			((Circle) t.getSource()).setCursor(Cursor.HAND);
		}
	};
	EventHandler<MouseEvent> paneCircleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			int i = 0;
			double time = 0;
			for (Keyframe keyframe : timeline) {
				if (e.getTarget() == keyframe.getPaneCircle()) {
					time = keyframe.getTime().toMillis();
					break;
				}
			}
			Duration newTime = duration.multiply(time / duration.toMillis());
			Duration oldTime = mediaPlayer.getCurrentTime();
			mediaPlayer.seek(newTime);
			while (mediaPlayer.getCurrentTime().toMillis() == oldTime.toMillis()) {
				i++;
				if (i > 100) {
					break;
				}
			}
			updateValues();

		}
	};

	EventHandler<MouseEvent> circleOnMouseMovedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			if (deleteToggle.isSelected()) {
				((Circle) t.getSource()).setCursor(Cursor.HAND);
			} else if (!addToggle.isSelected()) {
				((Circle) t.getSource()).setCursor(Cursor.OPEN_HAND);
			}
		}
	};
	EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			Circle circle = (Circle) (t.getSource());
			int index = curProj.getDancerIndex(circle);
			if (deleteToggle.isSelected()) {
				if (groupedCircles.contains(circle)) {
					groupedCircles.remove(circle);
				}
				drawPane.getChildren().remove(circle);
				indexManager.enque(curProj.getDancerIndex(circle));
				System.out.println("Deleting Dancer " + curProj.getDancerIndex(circle));
				curProj.removeDancer(circle);
			} else if (groupToggle.isSelected()) {
				if (!groupedCircles.contains(circle)) {
					groupedCircles.add(circle);
					groupedPaths.get(curProj.getDancerIndex(circle)).setVisible(true);

				}
				System.out.println(groupedCircles.size());
			}

			if (deleteToggle.isSelected()) {
				((Circle) t.getSource()).setCursor(Cursor.HAND);
			} else if (!addToggle.isSelected()) {
				((Circle) t.getSource()).setCursor(Cursor.CLOSED_HAND);
			}

			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			// circleTranslates.get(index).setTranslateX(circle.getTranslateX());
			// circleTranslates.get(index).setTranslateY(circle.getTranslateY());

			// orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
			System.out.format("orgTranslateX:%f, orgTranslateY:%f\n", circleTranslates.get(index).getTranslateX(), circleTranslates.get(index).getTranslateY());
		}
	};

	EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {

			if (!addToggle.isSelected() && !groupToggle.isSelected()) {
				Circle source = (Circle) t.getTarget();
				double offsetX = t.getSceneX() - orgSceneX;
				double offsetY = t.getSceneY() - orgSceneY;

				if (groupedCircles.contains(source) && groupedCircles.size() > 0) {
					for (Circle circle : groupedCircles) {
						int index = curProj.getDancerIndex(circle);
						double newTranslateX = circleTranslates.get(index).getTranslateX() + offsetX;
						double newTranslateY = circleTranslates.get(index).getTranslateY() + offsetY;
						circleTranslates.get(index).setTranslateX(newTranslateX);
						circleTranslates.get(index).setTranslateY(newTranslateY);
						// circle.setTranslateX(newTranslateX);
						// circle.setTranslateY(newTranslateY);
						// groupedPaths.get(index).setTranslateX(newTranslateX);
						// groupedPaths.get(index).setTranslateY(newTranslateY);
					}
				} else {
					int index = curProj.getDancerIndex(source);
					double newTranslateX = circleTranslates.get(index).getTranslateX() + offsetX;
					double newTranslateY = circleTranslates.get(index).getTranslateY() + offsetY;
					circleTranslates.get(index).setTranslateX(newTranslateX);
					circleTranslates.get(index).setTranslateY(newTranslateY);
					// source.setTranslateX(newTranslateX);
					// source.setTranslateY(newTranslateY);
					// groupedPaths.get(index).setTranslateX(newTranslateX);
					// groupedPaths.get(index).setTranslateY(newTranslateY);
				}
				orgSceneX += offsetX;
				orgSceneY += offsetY;
			}
		}
	};

	EventHandler<MouseEvent> circleOnMouseReleaseEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			Circle source = (Circle) t.getSource();

			if (deleteToggle.isSelected()) {
				source.setCursor(Cursor.DEFAULT);
			} else {
				source.setCursor(Cursor.OPEN_HAND);
			}

			for (int i = 0; i < timeline.size(); i++) {
				if (timeline.get(i).getTime().toMillis() == mediaPlayer.getCurrentTime().toMillis()) {
					timeline.get(i).setCircleTranslates(circleTranslates);
					break;
				}
				if (timeline.get(i).getTime().toMillis() >= mediaPlayer.getCurrentTime().toMillis()) {
					Keyframe newKeyFrame = new Keyframe(circleTranslates, mediaPlayer.getCurrentTime());
					timeline.add(i, newKeyFrame);
					addKeyframePane(newKeyFrame);
					break;
				}
			}

			listTimeline();

		}
	};

	private void initMediaPlayer() {
		mediaPlayer.currentTimeProperty().addListener((Observable ov) -> {
			updateValues();
		});

		mediaPlayer.setOnReady(() -> {
			mediaPlayer.setAudioSpectrumInterval(0.0016);
			duration = mediaPlayer.getMedia().getDuration();

			Line line = new Line(SLIDER_X, SLIDER_Y, SLIDER_X + SLIDER_WIDTH, SLIDER_Y);
			keyframePane.getChildren().add(line);

			Keyframe newKeyframe;
			newKeyframe = new Keyframe(circleTranslates, new Duration(0));
			timeline.add(newKeyframe);
			addKeyframePane(newKeyframe);
			newKeyframe = new Keyframe(circleTranslates, duration);
			timeline.add(newKeyframe);
			addKeyframePane(newKeyframe);

			groupToggle.setDisable(false);
			addToggle.setDisable(false);
			deleteToggle.setDisable(false);
			updateValues();
		});

		slider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (slider.isValueChanging()) {
					// multiply duration by percentage calculated by slider
					// position
					if (duration != null) {
						mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
						time.setText(formatTime(mediaPlayer.getCurrentTime()));
					}
					updateValues();
				}
			}
		});

		mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue observable, Duration oldValue, Duration newValue) {
				updateValues();
			}
		});
	}

	public void updateValues() {
		Status status = mediaPlayer.getStatus();
		Duration currentTime = mediaPlayer.getCurrentTime();
		System.out.println(currentTime.toMillis());

		if (status == Status.PLAYING) {
			if (currentTime.equals(duration)) {
				mediaPlayer.pause();
				currentTime = mediaPlayer.getCurrentTime();
			}
		}
		time.setText(formatTime(currentTime));
		slider.setDisable(duration.isUnknown());
		if (!slider.isDisabled() && duration.greaterThan(Duration.ZERO) && !slider.isValueChanging()) {
			slider.setValue((currentTime.toMillis() / duration.toMillis()) * 100.0);
		}

		for (int i = 1; i < timeline.size(); i++) {
			if (timeline.get(i).getTime().toMillis() > currentTime.toMillis()) {
				deepCopyCircleTranslates(timeline.get(i - 1));
				interpolateTranslates(currentTime);
				break;
			}
		}
		// listCircleTranslates();
	}

	private void interpolateTranslates(Duration currentTime) {
		int currentKeyframeIndex = 0;
		double currentKeyframeTime = 0;
		double nextKeyframeTime = 0;
		for (int i = 1; i < timeline.size(); i++) {
			if (i == timeline.size() - 1) {
				return;
			}
			if (timeline.get(i).getTime().toMillis() > currentTime.toMillis()) {
				currentKeyframeIndex = i - 1;
				currentKeyframeTime = timeline.get(i - 1).getTime().toMillis();
				nextKeyframeTime = timeline.get(i).getTime().toMillis();
				break;
			}
		}
		for (CircleTranslate circleTranslate : circleTranslates) {
			int index = circleTranslate.getIndex();
			double ratio = (currentTime.toMillis() - currentKeyframeTime) / (nextKeyframeTime - currentKeyframeTime);
			double currentKfTransX = timeline.get(currentKeyframeIndex).getCircleTranslateByIndex(index).getTranslateX();
			double currentKfTransY = timeline.get(currentKeyframeIndex).getCircleTranslateByIndex(index).getTranslateY();
			double nextKfTransX = timeline.get(currentKeyframeIndex + 1).getCircleTranslateByIndex(index).getTranslateX();
			double nextKfTransY = timeline.get(currentKeyframeIndex + 1).getCircleTranslateByIndex(index).getTranslateY();
			double newTransX = circleTranslate.getTranslateX() + (nextKfTransX - currentKfTransX) * ratio;
			double newTransY = circleTranslate.getTranslateY() + (nextKfTransY - currentKfTransY) * ratio;
			circleTranslate.setTranslateX(newTransX);
			circleTranslate.setTranslateY(newTransY);
		}
	}

	private void deepCopyCircleTranslates(Keyframe keyframe) {
		for (Dancer dancer : curProj.getDancers()) {
			dancer.circle.translateXProperty().unbind();
			dancer.circle.translateYProperty().unbind();
			dancer.getPath().translateXProperty().unbind();
			dancer.getPath().translateYProperty().unbind();
		}
		circleTranslates.clear();
		for (CircleTranslate circleTranslate : keyframe.getCircleTranslates()) {
			CircleTranslate newCircleTranslate = new CircleTranslate(circleTranslate.getIndex());
			newCircleTranslate.setTranslateX(circleTranslate.getTranslateX());
			newCircleTranslate.setTranslateY(circleTranslate.getTranslateY());
			circleTranslates.add(newCircleTranslate);
		}
		for (CircleTranslate circleTranslate : circleTranslates) {
			for (Dancer dancer : curProj.getDancers()) {
				if (dancer.index == circleTranslate.getIndex()) {
					dancer.circle.translateXProperty().bind(circleTranslate.getDpTranslateX());
					dancer.circle.translateYProperty().bind(circleTranslate.getDpTranslateY());
					dancer.getPath().translateXProperty().bind(circleTranslate.getDpTranslateX());
					dancer.getPath().translateYProperty().bind(circleTranslate.getDpTranslateY());
				}
			}
		}
	}

	private static String formatTime(Duration elapsed) {
		int intElapsed = (int) Math.floor(elapsed.toSeconds());
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedMinutes * 60;
		return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
	}

	@FXML
	public void loadDefaultMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		media = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		initMediaPlayer();

	}

	@FXML
	public void loadMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3 Files", "*.mp3"));
		File file = fc.showOpenDialog(null);
		String fcPath = file.getAbsolutePath();
		fcPath = fcPath.replace("\\", "/");
		media = new Media(new File(fcPath).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		initMediaPlayer();
	}

	@FXML
	public void playMusic() {
		updateValues();
		Status status = mediaPlayer.getStatus();
		// System.out.println(status.toString());
		mediaPlayer.setAudioSpectrumInterval(0.0016);
		mediaPlayer.setRate(1);
		mediaPlayer.play();

		addToggle.setDisable(true);
		groupToggle.setDisable(true);
		deleteToggle.setDisable(true);

	}

	@FXML
	public void pauseMusic() {

		Status status = mediaPlayer.getStatus();

		if (!(status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)) {

			mediaPlayer.pause();
		}
		updateValues();
		groupToggle.setDisable(false);
		addToggle.setDisable(false);
		deleteToggle.setDisable(false);

	}

	@FXML
	public void stopMusic() {
		Status status = mediaPlayer.getStatus();
		System.out.println(status.toString());
		
		Duration oldTime = mediaPlayer.getCurrentTime();
		mediaPlayer.seek(DURATION_BEGIN);
		if (!(status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)) {
			mediaPlayer.pause();
		}		
		while(mediaPlayer.getCurrentTime().toMillis() == oldTime.toMillis()){}
		updateValues();
		groupToggle.setDisable(false);
		addToggle.setDisable(false);
		deleteToggle.setDisable(false);

	}

	@FXML
	public void setToBeginMusic() {
		Status status = mediaPlayer.getStatus();
		mediaPlayer.seek(DURATION_BEGIN);
		// groupToggle.setDisable(false);
		// addToggle.setDisable(false);
		// deleteToggle.setDisable(false);

	}

	@FXML
	public void forwardMusic() {
		Status status = mediaPlayer.getStatus();
		mediaPlayer.setRate(2);
	}

}
