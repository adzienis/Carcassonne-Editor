package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class TileController
{
	ObservableList<String> list = FXCollections.observableArrayList();
	File initialDirectory = null;

	FileChooser fileChooser = new FileChooser();

	int count = 0;

	@FXML
	private TextField countField;

	@FXML
	private MenuItem open, save, newItem, saveas;

	@FXML
	private ImageView imageView;

	@FXML
	private Label countLabel;

	File file = null;

	int[][] grid = new int[9][9];

	enum TileType { 
		Field, Road, City, Cloister, None, Crossing;

		public static int getTileValue(TileType t) {
			switch(t) {
				case Field:
					return 0;
				case Road:
					return 1;
				case City:
					return 3;
				case Cloister:
					return 2;
				case Crossing:
					return 4;
				case None:
					return -1;
				default:
					return -1;
			}
		}
	 }

	@FXML
	private Pane pane;

	@FXML
    private ChoiceBox<String> box;

	@FXML
	// The reference of inputText will be injected by the FXML loader
	private TextField inputText;
	
	// The reference of outputText will be injected by the FXML loader
	@FXML
	private TextArea outputText;
	
	// location and resources will be automatically injected by the FXML loader	
	@FXML
	private URL location;
	
	@FXML
	private ResourceBundle resources;
	
	// Add a public no-args constructor
	public TileController() 
	{
	}

	@FXML
	private void openFile() {
		System.out.println("mike");
	}

	private int[] getRectFromCoord(int x, int y) {
		int col = (int)x/((int)pane.getPrefWidth()/9);
		int row = (int)y/((int)pane.getPrefHeight()/9);

		return new int[] {col ,row};
	}
	
	private void drawGrid(Pane pane) {
		pane.setStyle("-fx-background-color: white;");

		countLabel.setText("Count: " + Integer.toString(count));

		double largeRectWidth = pane.getPrefWidth()/3;
		double largeRectHeight = pane.getPrefHeight()/3;

		Line right = new Line(pane.getPrefWidth(), 0, pane.getPrefWidth(), pane.getPrefHeight());
		right.setStroke(Color.BLUE);

		Line bottom = new Line(0, pane.getPrefHeight(), pane.getPrefWidth(), pane.getPrefHeight());
		bottom.setStroke(Color.BLUE);

		Rectangle[][] subRects = new Rectangle[9][9];

		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 9; x++) {
				Line top = new Line();
				Line left = new Line();

				double smallWidth = largeRectWidth/3;
				double smallHeight = largeRectHeight/3;

				subRects[y][x] = new Rectangle(x*smallWidth,
					y*smallHeight, smallWidth, smallHeight);

				switch(grid[y][x]) {
					case -1:
						subRects[y][x].setFill(Color.RED);
					break;
					case 0:
						subRects[y][x].setFill(Color.GREEN);
					break;
					case 1:
						subRects[y][x].setFill(Color.BROWN);
					break;
					case 2:
						subRects[y][x].setFill(Color.AQUA);
					break;
					case 3:
						subRects[y][x].setFill(Color.YELLOW);
					break;
				}

				subRects[y][x].addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
					int[] temp = getRectFromCoord((int)e.getX(), (int)e.getY());

					if(box.getValue() != null) {
						switch(box.getValue()) {
							case "Field":
								subRects[temp[1]][temp[0]].setFill(Color.GREEN);
								grid[temp[1]][temp[0]] = TileType.getTileValue(TileType.Field);
							break;
							case "City":
								subRects[temp[1]][temp[0]].setFill(Color.YELLOW);
								grid[temp[1]][temp[0]] = TileType.getTileValue(TileType.City);
							break;
							case "Cloister":
								subRects[temp[1]][temp[0]].setFill(Color.AQUA);
								grid[temp[1]][temp[0]] = TileType.getTileValue(TileType.Cloister);
							break;
							case "Road":
								subRects[temp[1]][temp[0]].setFill(Color.BROWN);
								grid[temp[1]][temp[0]] = TileType.getTileValue(TileType.Road);
							break;
							case "Crossing":
								subRects[temp[1]][temp[0]].setFill(Color.MAROON);
								grid[temp[1]][temp[0]] = TileType.getTileValue(TileType.Crossing);
							break;
							case "None":
								subRects[temp[1]][temp[0]].setFill(Color.RED);
								grid[temp[1]][temp[0]] = TileType.getTileValue(TileType.None);
							break;
						}
					}
				});

				pane.getChildren().addAll(subRects[y][x]);
				top.setStartX(x*smallWidth);
				top.setStartY(y*smallHeight);
				top.setEndX(x*smallWidth + smallWidth);
				top.setEndY(y*smallHeight);
				top.setStroke(Color.BLUE);

				left.setStartX(x*smallWidth);
				left.setStartY(y*smallHeight);
				left.setEndX(x*smallWidth);
				left.setEndY(y*smallHeight + smallHeight);
				left.setStroke(Color.BLUE);

				pane.getChildren().addAll(top, left);
			}
		}

		pane.getChildren().addAll(right, bottom);
	}

	@FXML
	private void initialize() 
	{
		for(int row = 0; row < 9; row++) {
			for(int col = 0; col < 9; col++) {
				grid[row][col] = -1;
			}
		}

		drawGrid(pane);

		saveas.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(file != null) {
					fileChooser.setInitialDirectory(file.getParentFile());
				}
				file = fileChooser.showSaveDialog(null);

				try {
					FileWriter writer = new FileWriter(file);

					writer.write(Integer.toString(count) + "\n");

					for(int row = 0; row < 9; row++) {
						for(int col = 0; col < 9; col++) {
							writer.write(Integer.toString(grid[row][col]) + " ");
						}
						writer.write("\n");
					}
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			});

		newItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					file = null;

					for(int row = 0; row < 9; row++) {
						for(int col = 0; col < 9; col++) {
							grid[row][col] = -1;
						}
					}

					count = 0;
					drawGrid(pane);
				}
				catch(Exception err) {
					return;
				}
			}
		});

		countField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					count = Integer.parseInt(countField.getText());
					countLabel.setText("Count: " + Integer.toString(count));
					countField.clear();
				}
				catch(Exception err) {
					return;
				}
			}
		});

		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					FileWriter writer = new FileWriter(file);

					writer.write(Integer.toString(count) + "\n");

					for(int row = 0; row < 9; row++) {
						for(int col = 0; col < 9; col++) {
							writer.write(Integer.toString(grid[row][col]) + " ");
						}
						writer.write("\n");
					}
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		open.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(file != null) {
					fileChooser.setInitialDirectory(file.getParentFile());
				}
				file = fileChooser.showOpenDialog(null);

				if(file == null) return;

				Image imgFile = null;

				try {
					imgFile = new Image("file:" + file.getParentFile() + "/" +
					file.getName().substring(0, file.getName().lastIndexOf('.')) + ".png");
				}
				catch(Exception err) {
					System.out.println("Problem with image URI");
					imgFile = null;
				}

				imageView.setImage(imgFile);

				pane.getChildren().clear();

				try {
					Scanner sc = new Scanner(file);

					count = Integer.parseInt(sc.next());

					for(int row = 0; row < 9; row++) {
						for(int col = 0; col < 9; col++) {
							grid[row][col] = Integer.parseInt(sc.next());
						}
					}
					sc.close();

					drawGrid(pane);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		loadData();
	}
	
	@FXML
	private void printOutput() 
	{
		outputText.setText(inputText.getText());
	}

	@FXML
	private void Test() {
		//System.out.println("test");
	}

	private void loadData() {

		list.addAll("Field", "City", "Cloister", "Road", "None" , "Crossing");

		box.getItems().addAll(list);
	}

	@FXML
	private void TextChanged() {
		System.out.println("asd");
	}
}
