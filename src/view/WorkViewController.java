package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.MainApp;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.Bloom;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.Math;
import model.Proj;
import utils.DancerComparatorByX;
import utils.DancerComparatorByY;
import utils.DancerCompareType;
import model.CircleTranslate;
import model.Dancer;
import model.IndexManager;
import model.Keyframe;

public class WorkViewController {

	public final static int CIRCLE_RADIUS = 10;
	public final static int KEYFRAME_CIRCLE_RADIUS = 5;
	public final static int SLIDER_WIDTH = 625;
	public final static int SLIDER_X = 18;
	public final static int SLIDER_Y = 8;
	public final static int SELECT_RADIUS = 12;
	public final static int LINE_WIDTH = 2;
	public final static int LINE_WIDTH_MAGNET = 4;
	public final static int PANE_WIDTH = 720;
	public final static int PANE_HEIGHT = 480;
	public final static Duration DURATION_BEGIN = new Duration(-1);
	private static final List<String> AUTOFORMS = Arrays.asList("V", "Circle", "Rectangle");

	private static enum ORIENT {
		X, Y
	};

	private static enum ORIENT_DIAG {
		SLASH, BACKSLASH
	};

	private static enum ORIENT_WARD {
		DOWNWARD, UPWARD
	}

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
	@FXML
	public ComboBox<String> autoFormCombobox;

	private Proj curProj;

	private IndexManager indexManager;
	private List<CircleTranslate> circleTranslates = new ArrayList<CircleTranslate>();
	private List<Circle> groupedCircles = new ArrayList<Circle>();
	private List<Path> groupedPaths = new ArrayList<Path>();

	private double orgSceneX, orgSceneY;
	private double drawPaneOrgSceneX, drawPaneOrgSceneY;

	private ObservableList<Keyframe> timeline = FXCollections.observableArrayList();

	private Rectangle rubberband;
	
	private List<Line> linesVertical = new ArrayList<Line>();
	private List<Line> linesHorizontal = new ArrayList<Line>();

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
		// Toggle Buttons
		groupToggle.setDisable(true);
		addToggle.setDisable(true);
		deleteToggle.setDisable(true);

		// Combobox
		autoFormCombobox.getItems().addAll(AUTOFORMS);
		autoFormCombobox.setValue(AUTOFORMS.get(0));

		// Ruler : 8 x 4
		for (int i = 1; i <= 7; i++) {
			Line ruler = new Line(PANE_WIDTH / 8 * i, 0, PANE_WIDTH / 8 * i, PANE_HEIGHT / 2);
			ruler.setStrokeWidth(LINE_WIDTH);
			ruler.setStroke(Color.LIGHTGRAY);
			drawPane.getChildren().add(ruler);
			linesVertical.add(ruler);
		}
		for (int i = 1; i <= 3; i++) {
			Line ruler = new Line(0, PANE_HEIGHT / 2 / 4 * i, PANE_WIDTH, PANE_HEIGHT / 2 / 4 * i);
			ruler.setStrokeWidth(LINE_WIDTH);
			ruler.setStroke(Color.LIGHTGRAY);
			drawPane.getChildren().add(ruler);
			linesHorizontal.add(ruler);
		}

		// Rubberband
		rubberband = new Rectangle(0, 0, 0, 0);
		rubberband.setStroke(Color.BLUE);
		rubberband.setStrokeWidth(1);
		rubberband.setStrokeLineCap(StrokeLineCap.ROUND);
		rubberband.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

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
	
	private void resetRuler() {
		for(Line ruler : linesVertical){
			ruler.setStrokeWidth(LINE_WIDTH);
			ruler.setStroke(Color.LIGHTGRAY);
		}
		for(Line ruler : linesHorizontal){
			ruler.setStrokeWidth(LINE_WIDTH);
			ruler.setStroke(Color.LIGHTGRAY);
		}
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
					CircleTranslate newCircleTranslate = new CircleTranslate(circleTranslate.getIndex(), circleTranslate.getTranslateX(),
							circleTranslate.getTranslateY());
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

			rubberband.setX(x);
			rubberband.setY(y);
			rubberband.setWidth(0);
			rubberband.setHeight(0);

			drawPane.getChildren().add(rubberband);

			drawPaneOrgSceneX = e.getX();
			drawPaneOrgSceneY = e.getY();

		}
	}

	@FXML
	public void drawPaneOnMouseDragged(MouseEvent e) {
		double offsetX = e.getX() - drawPaneOrgSceneX;
		double offsetY = e.getY() - drawPaneOrgSceneY;
		if (e.getTarget() instanceof AnchorPane) {
			if (offsetX > 0)
				rubberband.setWidth(offsetX);
			else {
				rubberband.setX(e.getX());
				rubberband.setWidth(drawPaneOrgSceneX - rubberband.getX());
			}

			if (offsetY > 0) {
				rubberband.setHeight(offsetY);
			} else {
				rubberband.setY(e.getY());
				rubberband.setHeight(drawPaneOrgSceneY - rubberband.getY());
			}

			for (Node node : drawPane.getChildren()) {
				if (node instanceof Circle) {
					if (// 只要碰到就被加進群組
					node.getBoundsInParent().intersects(rubberband.getBoundsInParent())
					// 要整個框起來才加進群組
					/*
					 * rubberband.getBoundsInParent().contains(node.
					 * getBoundsInParent())
					 */
					) {
						if (!(groupedCircles.contains((Circle) node))) {
							groupedCircles.add((Circle) node);
							groupedPaths.get(curProj.getDancerIndex((Circle) node)).setVisible(true);
						}
					} else {
						groupedCircles.remove((Circle) node);
						groupedPaths.get(curProj.getDancerIndex((Circle) node)).setVisible(false);
					}
				}
			}
		}
	}

	@FXML
	public void drawPaneOnMouseRelease(MouseEvent e) {
		rubberband.setX(0);
		rubberband.setY(0);
		rubberband.setWidth(0);
		rubberband.setHeight(0);

		drawPane.getChildren().remove(rubberband);
	}

	private void addKeyframePane(Keyframe newKeyFrame) {

		double keyframeTime = newKeyFrame.getTime().toMillis();
		double x = SLIDER_X + SLIDER_WIDTH * keyframeTime / duration.toMillis();
		Circle paneCircle = new Circle(x, SLIDER_Y, KEYFRAME_CIRCLE_RADIUS, Color.GOLD);
		paneCircle.setStroke(Color.BLACK);
		paneCircle.setStrokeType(StrokeType.OUTSIDE);
		paneCircle.setStrokeWidth(1);

		// Context Menu
		final ContextMenu contextMenu = new ContextMenu();
		MenuItem tweenMI = new MenuItem("Move");
		MenuItem defaultMI = new MenuItem("Still");
		defaultMI.setDisable(true);
		tweenMI.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				newKeyFrame.setType(Keyframe.TWEEN);
				paneCircle.setFill(Color.GOLDENROD);
				defaultMI.setDisable(false);
				tweenMI.setDisable(true);
			}
		});
		defaultMI.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				newKeyFrame.setType(Keyframe.DEFAULT);
				paneCircle.setFill(Color.GOLDENROD);
				defaultMI.setDisable(true);
				tweenMI.setDisable(false);
			}
		});
		contextMenu.getItems().addAll(tweenMI, defaultMI);

		EventHandler<MouseEvent> paneCircleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (e.isSecondaryButtonDown()) {
					contextMenu.show(paneCircle, e.getScreenX(), e.getScreenY());
				} else {
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
			}
		};

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
			System.out.print(circleTranslates.get(i).getIndex() + "\t" + circleTranslates.get(i).getTranslateX() + " "
					+ circleTranslates.get(i).getTranslateY() + "\t");
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
			orgSceneY = t.getSceneY() - PANE_HEIGHT / 2 + 5;
			System.out.format("orgTranslateX:%f, orgTranslateY:%f\n", circleTranslates.get(index).getTranslateX(),
					circleTranslates.get(index).getTranslateY());
		}
	};

	EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			double mouseX = t.getSceneX();
			double mouseY = t.getSceneY() - PANE_HEIGHT / 2 + 5;
			if (!addToggle.isSelected() && !groupToggle.isSelected()) {
				Circle source = (Circle) t.getTarget();
				double offsetX = mouseX - orgSceneX;
				double offsetY = mouseY - orgSceneY;
				if (groupedCircles.contains(source) && groupedCircles.size() > 0) {
					for (Circle circle : groupedCircles) {
						int index = curProj.getDancerIndex(circle);
						double newTranslateX = circleTranslates.get(index).getTranslateX() + offsetX;
						double newTranslateY = circleTranslates.get(index).getTranslateY() + offsetY;
						circleTranslates.get(index).setTranslateX(newTranslateX);
						circleTranslates.get(index).setTranslateY(newTranslateY);
					}
				} else {
					int index = curProj.getDancerIndex(source);
					// Magnet
					boolean magnetX = false;
					boolean magnetY = false;
					resetRuler();
					// 其他dancer
					for (Dancer dancer : curProj.getDancers()) {
						if (dancer.getIndex() == index) {
							continue;
						}
						double dancerX = dancer.getCircle().getCenterX() + dancer.getCircle().getTranslateX();
						double dancerY = dancer.getCircle().getCenterY() + dancer.getCircle().getTranslateY();
						if (Math.abs(dancerX - mouseX) < 10) {
							circleTranslates.get(index).setTranslateX(dancerX - source.getCenterX());
							magnetX = true;
						}
						if (Math.abs(dancerY - mouseY) < 10) {
							circleTranslates.get(index).setTranslateY(dancerY - source.getCenterY());
							magnetY = true;
						}
					}
					// 尺規
					for (Line ruler : linesVertical){
						double lineX = ruler.getStartX();
						if (Math.abs(lineX - mouseX) < 10) {
							circleTranslates.get(index).setTranslateX(lineX - source.getCenterX());
							ruler.setStrokeWidth(LINE_WIDTH_MAGNET);
							ruler.setStroke(Color.MEDIUMSPRINGGREEN);
							magnetX = true;
						}
					}
					for (Line ruler : linesHorizontal){
						double lineY = ruler.getStartY();
						if (Math.abs(lineY - mouseY) < 10) {
							circleTranslates.get(index).setTranslateY(lineY - source.getCenterY());
							ruler.setStrokeWidth(LINE_WIDTH_MAGNET);
							ruler.setStroke(Color.MEDIUMSPRINGGREEN);
							magnetY = true;
						}
					}
					
					if (!magnetX) {
						double newTranslateX = circleTranslates.get(index).getTranslateX() + offsetX;
						circleTranslates.get(index).setTranslateX(newTranslateX);
					}
					if (!magnetY) {
						double newTranslateY = circleTranslates.get(index).getTranslateY() + offsetY;
						circleTranslates.get(index).setTranslateY(newTranslateY);
					}
				}
				orgSceneX += offsetX;
				orgSceneY += offsetY;
			}
		}
	};

	EventHandler<MouseEvent> circleOnMouseReleaseEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			resetRuler();
			
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
		if (timeline.get(currentKeyframeIndex).getType() == Keyframe.TWEEN) {
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
		while (mediaPlayer.getCurrentTime().toMillis() == oldTime.toMillis()) {}
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

	@FXML
	public void alignVertical() {
		int size = groupedCircles.size();
		double alignX = 0;
		double[] x = new double[size];
		if (size < 2) {
			return;
		}
		for (int i = 0; i < size; i++) {
			Circle circle = groupedCircles.get(i);
			int index = curProj.getDancerIndex(circle);
			x[i] = circle.getCenterX() + circleTranslates.get(index).getTranslateX();
		}
		Arrays.sort(x);
		if (size % 2 == 1) { // 奇數：對齊最中間的人
			alignX = x[size / 2];
		} else if (size % 2 == 0) { // 偶數：對齊中間兩個人的中線
			alignX = (x[size / 2 - 1] + x[size / 2]) / 2;
		}
		for (Circle circle : groupedCircles) {
			int index = curProj.getDancerIndex(circle);
			double orgX = circle.getCenterX();
			circleTranslates.get(index).setTranslateX(alignX - orgX);
		}
	}

	@FXML
	public void alignStageVertical() {
		double alignX = PANE_WIDTH / 2;
		for (Circle circle : groupedCircles) {
			int index = curProj.getDancerIndex(circle);
			double orgX = circle.getCenterX();
			circleTranslates.get(index).setTranslateX(alignX - orgX);
		}
	}

	@FXML
	public void alignHorizontal() {
		int size = groupedCircles.size();
		double alignY = 0;
		double[] y = new double[size];
		if (size < 2) {
			return;
		}
		for (int i = 0; i < size; i++) {
			Circle circle = groupedCircles.get(i);
			int index = curProj.getDancerIndex(circle);
			y[i] = circle.getCenterY() + circleTranslates.get(index).getTranslateY();
		}
		Arrays.sort(y);
		if (size % 2 == 1) { // 奇數：對齊最中間的人
			alignY = y[size / 2];
		} else if (size % 2 == 0) { // 偶數：對齊中間兩個人的中線
			alignY = (y[size / 2 - 1] + y[size / 2]) / 2;
		}
		for (Circle circle : groupedCircles) {
			int index = curProj.getDancerIndex(circle);
			double orgY = circle.getCenterY();
			circleTranslates.get(index).setTranslateY(alignY - orgY);
		}
	}

	@FXML
	public void alignStageHorizontal() {
		double alignY = PANE_HEIGHT / 4;
		for (Circle circle : groupedCircles) {
			int index = curProj.getDancerIndex(circle);
			double orgY = circle.getCenterY();
			circleTranslates.get(index).setTranslateY(alignY - orgY);
		}
	}

	@FXML
	public void divergeVertical() {
		int size = groupedCircles.size();
		double leftX = 0;
		double rightX = 0;
		DancerCompareType[] xDancerCompares = new DancerCompareType[size];
		if (size < 3) {
			return;
		}
		xDancerCompares = getSortedGroupedDancers(ORIENT.X);
		leftX = xDancerCompares[0].x;
		rightX = xDancerCompares[size - 1].x;
		divergeVertical(leftX, rightX, xDancerCompares);
	}

	@FXML
	public void divergeStageVertical() {
		int size = groupedCircles.size();
		double leftX = 0;
		double rightX = PANE_WIDTH;
		double spacing = (rightX - leftX) / (size + 1); // 間距
		DancerCompareType[] xDancerCompares = new DancerCompareType[size];
		if (size < 2) {
			return;
		}
		for (int i = 0; i < size; i++) {
			Circle circle = groupedCircles.get(i);
			int index = curProj.getDancerIndex(circle);
			xDancerCompares[i] = new DancerCompareType(index, circle.getCenterX() + circleTranslates.get(index).getTranslateX(),
					circle.getCenterY() + circleTranslates.get(index).getTranslateY());
		}
		Arrays.sort(xDancerCompares, new DancerComparatorByX());
		for (int i = 0; i < size; i++) {
			int index = xDancerCompares[i].index;
			double orgX = 0;
			double targetX = leftX + spacing * (i + 1);
			// 先得到該index的circle
			for (Circle circle : groupedCircles) {
				if (curProj.getDancerIndex(circle) == index) {
					orgX = circle.getCenterX();
				}
			}
			// 再去做設定
			circleTranslates.get(xDancerCompares[i].index).setTranslateX(targetX - orgX);
		}
	}

	@FXML
	public void divergeStageHorizontal() {
		int size = groupedCircles.size();
		double upY = 0;
		double downY = PANE_HEIGHT / 2;
		double spacing = (downY - upY) / (size + 1); // 間距
		DancerCompareType[] yDancerCompares = new DancerCompareType[size];
		if (size < 2) {
			return;
		}
		for (int i = 0; i < size; i++) {
			Circle circle = groupedCircles.get(i);
			int index = curProj.getDancerIndex(circle);
			yDancerCompares[i] = new DancerCompareType(index, circle.getCenterX() + circleTranslates.get(index).getTranslateX(),
					circle.getCenterY() + circleTranslates.get(index).getTranslateY());
		}
		Arrays.sort(yDancerCompares, new DancerComparatorByY());
		for (int i = 0; i < size; i++) {
			int index = yDancerCompares[i].index;
			double orgY = 0;
			double targetY = upY + spacing * (i + 1);
			// 先得到該index的circle
			for (Circle circle : groupedCircles) {
				if (curProj.getDancerIndex(circle) == index) {
					orgY = circle.getCenterY();
				}
			}
			// 再去做設定
			circleTranslates.get(yDancerCompares[i].index).setTranslateY(targetY - orgY);
		}
	}

	@FXML
	public void divergeHorizontal() {
		int size = groupedCircles.size();
		double upY = 0;
		double downY = 0;
		double spacing = 0;
		DancerCompareType[] yDancerCompares = new DancerCompareType[size];
		if (size < 3) {
			return;
		}
		yDancerCompares = getSortedGroupedDancers(ORIENT.Y);
		upY = yDancerCompares[0].y;
		downY = yDancerCompares[size - 1].y;
		spacing = (downY - upY) / (size - 1); // 間距
		for (int i = 1; i < size - 1; i++) {
			int index = yDancerCompares[i].index;
			double orgY = 0;
			double targetY = upY + spacing * i;
			// 先得到該index的circle
			for (Circle circle : groupedCircles) {
				if (curProj.getDancerIndex(circle) == index) {
					orgY = circle.getCenterY();
				}
			}
			// 再去做設定
			circleTranslates.get(yDancerCompares[i].index).setTranslateY(targetY - orgY);
		}
	}

	public void divergeVertical(double leftX, double rightX, DancerCompareType[] xDancerCompares) {
		int size = groupedCircles.size();
		if (size < 3) {
			return;
		}
		double spacing = (rightX - leftX) / (size - 1); // 間距
		for (int i = 0; i < size; i++) {
			int index = xDancerCompares[i].index;
			double orgX = 0;
			double targetX = leftX + spacing * i;
			// 先得到該index的circle
			for (Circle circle : groupedCircles) {
				if (curProj.getDancerIndex(circle) == index) {
					orgX = circle.getCenterX();
				}
			}
			// 再去做設定
			circleTranslates.get(xDancerCompares[i].index).setTranslateX(targetX - orgX);
		}
	}

	public void divergeHorizontal(ORIENT_WARD ward, int start, int end, double upY, double downY, DancerCompareType[] yDancerCompares) {
		double spacing = (downY - upY) / (end - start); // 間距
		for (int i = start; i <= end; i++) {
			int index = yDancerCompares[i].index;
			double orgY = 0;
			double targetY = 0;
			if (ward == ORIENT_WARD.DOWNWARD) {
				targetY = upY + spacing * (i - start);
			} else if (ward == ORIENT_WARD.UPWARD) {
				targetY = downY - spacing * (i - start);
			}
			// 先得到該index的circle
			for (Circle circle : groupedCircles) {
				if (curProj.getDancerIndex(circle) == index) {
					orgY = circle.getCenterY();
				}
			}
			// 再去做設定
			circleTranslates.get(yDancerCompares[i].index).setTranslateY(targetY - orgY);
		}
	}

	@FXML
	public void showAutoForm() {
		String autoFormType = autoFormCombobox.getValue();
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/AutoForm" + autoFormType + "View.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage autoFormStage = new Stage();
			autoFormStage.setTitle("Auto Form");
			autoFormStage.initModality(Modality.WINDOW_MODAL);
			autoFormStage.setResizable(false);
			Scene newScene = new Scene(pane);
			autoFormStage.setScene(newScene);

			// Set the controller.
			AutoFormVViewController controller = loader.getController();
			controller.setStage(autoFormStage);

			// Show the dialog and wait until the user closes it
			autoFormStage.showAndWait();

			System.out.println("returning");
			realAutoForm(controller.getArgs());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void realAutoForm(List<String> args) {
		String autoFormType = autoFormCombobox.getValue();
		// V
		if (autoFormType == AUTOFORMS.get(0)) {
			String posOption = args.get(0);
			String sizeOption = args.get(1);
			int size = groupedCircles.size();
			double orgLeftX = getMostX("LEFT");
			double orgRightX = getMostX("RIGHT");
			double orgUpY = getMostY("UP");
			double orgDownY = getMostY("DOWN");
			double leftX = 0;
			double rightX = 0;
			double upY = 0;
			double downY = 0;
			// Check if cancelled or close btn pressed
			if (posOption == AutoFormViewController.CLOSE && sizeOption == AutoFormViewController.CLOSE) {
				return;
			}
			if (size < 3) {
				// TODO: POPUP ERROR MESSAGE
				return;
			}
			if (sizeOption == "Current Group") {
				leftX = orgLeftX;
				rightX = orgRightX;
				upY = orgUpY;
				downY = orgDownY;
			} else if (sizeOption == "Half Stage") {
				leftX = PANE_WIDTH / 4;
				rightX = PANE_WIDTH * 3 / 4;
				upY = PANE_HEIGHT / 2 / 4;
				downY = PANE_HEIGHT / 2 * 3 / 4;
			}
			if (posOption == "Stage Center") {
				leftX += PANE_WIDTH / 2 - (rightX + leftX) / 2;
				rightX += PANE_WIDTH / 2 - (rightX + leftX) / 2;
				upY += PANE_HEIGHT / 2 / 2 - (downY + upY) / 2;
				downY += PANE_HEIGHT / 2 / 2 - (downY + upY) / 2;
			} else if (posOption == "Current Position") {
				leftX += (orgRightX + orgLeftX) / 2 - (rightX + leftX) / 2;
				rightX += (orgRightX + orgLeftX) / 2 - (rightX + leftX) / 2;
				upY += (orgDownY + orgUpY) / 2 - (downY + upY) / 2;
				downY += (orgDownY + orgUpY) / 2 - (downY + upY) / 2;
			}
			DancerCompareType[] sortedDancers = getSortedGroupedDancers(ORIENT.X);
			divergeVertical(leftX, rightX, sortedDancers);
			// 奇數
			if (size % 2 == 1) {
				divergeHorizontal(ORIENT_WARD.DOWNWARD, 0, size / 2, upY, downY, sortedDancers);
			}
			// 偶數
			else {
				divergeHorizontal(ORIENT_WARD.DOWNWARD, 0, size / 2 - 1, upY, downY, sortedDancers);
			}
			divergeHorizontal(ORIENT_WARD.UPWARD, size / 2, size - 1, upY, downY, sortedDancers);
		}

	}

	private double getMostX(String orient) {
		int size = groupedCircles.size();
		DancerCompareType[] xDancerCompares = getSortedGroupedDancers(ORIENT.X);
		if (orient == "LEFT") {
			return xDancerCompares[0].x;
		} else if (orient == "RIGHT") {
			return xDancerCompares[size - 1].x;
		} else {
			return 0;
		}
	}

	private double getMostY(String orient) {
		int size = groupedCircles.size();
		DancerCompareType[] yDancerCompares = getSortedGroupedDancers(ORIENT.Y);
		if (orient == "UP") {
			return yDancerCompares[0].y;
		} else if (orient == "DOWN") {
			return yDancerCompares[size - 1].y;
		} else {
			return 0;
		}
	}

	private DancerCompareType[] getSortedGroupedDancers(ORIENT orient) {
		int size = groupedCircles.size();
		DancerCompareType[] dancerCompares = new DancerCompareType[size];
		for (int i = 0; i < size; i++) {
			Circle circle = groupedCircles.get(i);
			int index = curProj.getDancerIndex(circle);
			dancerCompares[i] = new DancerCompareType(index, circle.getCenterX() + circleTranslates.get(index).getTranslateX(),
					circle.getCenterY() + circleTranslates.get(index).getTranslateY());
		}
		if (orient == ORIENT.X) {
			Arrays.sort(dancerCompares, new DancerComparatorByX());
		} else if (orient == ORIENT.Y) {
			Arrays.sort(dancerCompares, new DancerComparatorByY());
		}
		return dancerCompares;
	}

}
