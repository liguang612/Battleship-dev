package Battleship;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import static Battleship.HomeController.stopPlayingSound;

public class PrepareController implements Initializable{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ImageView randomShip = new ImageView(getClass().getResource("Image") + "random.png");
        ImageView deleteShip = new ImageView(getClass().getResource("Image") + "trash.png");
        ImageView startGame = new ImageView(getClass().getResource("Image") + "play.png");
        ImageView backMenu = new ImageView(getClass().getResource("Image") + "home.png");
        String[] shipName = {"Carrier","Battleship","Cruiser","Submarine","Destroyer"};
        String[] Mode = {"Easy","Medium","Hard"};

        shipArray.addAll(Arrays.asList(shipName));
        gameMode.getItems().addAll(Mode);
        gameMode.getSelectionModel().select(0);
        playerBoard.setLayoutX(160);
        playerBoard.setLayoutY(250);
        btnRandom.setGraphic(randomShip);
        btnDelete.setGraphic(deleteShip);
        btnStart.setGraphic(startGame);
        btnBack.setGraphic(backMenu);

        Image image0 = new Image(getClass().getResource("Image") + "carrier-horizontal.png",245,57,false,false);
        Image image1 = new Image(getClass().getResource("Image") + "battleship-horizontal.png");
        Image image2 = new Image(getClass().getResource("Image") + "cruiser-horizontal.png");
        Image image3 = new Image(getClass().getResource("Image") + "submarine-horizontal.png");
        Image image4 = new Image(getClass().getResource("Image") + "destroyer-horizontal.png");

        ships = new ImageView[5];
        ships[0] = new ImageView(image0);
        ships[1] = new ImageView(image1);
        ships[2] = new ImageView(image2);
        ships[3] = new ImageView(image3);
        ships[4] = new ImageView(image4);

        for(int i = 0;i < 5;i++){
            ships[i].setPreserveRatio(true);
        }

        ships[0].setFitWidth(200);
        ships[1].setFitWidth(160);
        ships[2].setFitWidth(120);
        ships[3].setFitWidth(120);
        ships[4].setFitWidth(80);

        for(int i = 0;i < 5;i++){
            ShipsToBePlaced.add(ships[i],0,i);
        }

        ShipsToBePlaced.setOnMousePressed(mouseEvent -> {
            if(mouseEvent.getTarget() instanceof ImageView) {
                ImageView source = (ImageView) mouseEvent.getTarget();
                System.out.println(GridPane.getRowIndex(source));
                final boolean[] check = {false};

                source.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println("DragDetected");
                        Dragboard db = source.startDragAndDrop(TransferMode.ANY);

                        ClipboardContent cbContent = new ClipboardContent();
                        cbContent.putImage(source.getImage());

                        db.setContent(cbContent);
                        // source.setVisible(false);
                        mouseEvent.consume();
                    }
                });

                playerBoard.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        if(dragEvent.getGestureSource() != playerBoard && dragEvent.getDragboard().hasImage()) {
                            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                            System.out.println("DragOver");
                        }

                        dragEvent.consume();
                    }
                });

                playerBoard.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        if(dragEvent.getGestureSource() != playerBoard && dragEvent.getDragboard().hasImage()) {
                            ShipsToBePlaced.getChildren().remove(source);
                            playerBoard.setOpacity(0.7);
                            System.out.println("Drag Entered");
                        }

                        dragEvent.consume();
                    }
                });

                playerBoard.setOnDragExited(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        ShipsToBePlaced.getChildren().add(source);
                        playerBoard.setOpacity(1);
                        dragEvent.consume();
                    }
                });

                playerBoard.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        Dragboard db = dragEvent.getDragboard();
                        boolean success = false;
                        Cell cell = (Cell)dragEvent.getPickResult().getIntersectedNode();

                        if(db.hasImage() && cell != null) {
                            int x = cell.x;
                            int y = cell.y;
                            System.out.println(x);
                            System.out.println(y);

                            if(source.getFitWidth() == 200){
                                if(playerBoard.canPlaceShip(carrier,x,y)) {
                                    check[0] = true;
                                    playerBoard.placeShip(carrier,x,y);
                                    shipArray.remove(ShipType.CARRIER.name);
                                }
                            }
                            else if(source.getFitWidth() == 160){
                                if(playerBoard.canPlaceShip(battleship,x,y)) {
                                    check[0] = true;
                                    playerBoard.placeShip(battleship,x,y);
                                    shipArray.remove(ShipType.BATTLESHIP.name);
                                }
                            }
                            else if(source.getFitWidth() == 120 && source.getImage() == image2){
                                if(playerBoard.canPlaceShip(cruiser,x,y)) {
                                    check[0] = true;
                                    playerBoard.placeShip(cruiser,x,y);
                                    shipArray.remove(ShipType.CRUISER.name);
                                }
//                                playerBoard.setShipImages(submarine,x,y);
                            }

                            else if(source.getFitWidth() == 120 && source.getImage() == image3){
                                if(playerBoard.canPlaceShip(submarine,x,y)) {
                                    check[0] = true;
                                    playerBoard.placeShip(submarine,x,y);
                                    shipArray.remove(ShipType.SUBMARINE.name);
                                }
//                                playerBoard.setShipImages(submarine,x,y);
                            }


                            else if(source.getFitWidth() == 80){
                                if(playerBoard.canPlaceShip(destroyer,x,y)) {
                                    check[0] = true;
                                    playerBoard.placeShip(destroyer,x,y);
                                    shipArray.remove(ShipType.DESTROYER.name);
                                }
                            }

                            success = true;
                        }
                        dragEvent.setDropCompleted(success);
                        dragEvent.consume();
                    }
                });

                source.setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        if(dragEvent.getTransferMode() == TransferMode.MOVE){
                            if(check[0]) ShipsToBePlaced.getChildren().remove(source);
                            System.out.println("DragDone");
                        }
                        dragEvent.consume();
                    }
                });
            }

        });

        playerBoard.setOnMousePressed(event -> {
            if(running) return;
            Cell cell = (Cell) event.getTarget();
            if(cell.getShip() == null) return;

            playerBoard.rotateShip(cell.getShip(),cell.getShip().getX(),cell.getShip().getY());
        });

        ShipsToBePlaced.setLayoutX(750);
        ShipsToBePlaced.setLayoutY(330);
        ShipsToBePlaced.setVgap(30);
        ShipsToBePlaced.setHgap(30);
        root.getChildren().add(playerBoard);
        root.getChildren().add(ShipsToBePlaced);
    }
    @FXML
    private ChoiceBox<String> gameMode;
    @FXML
    private AnchorPane root;
    public static Board playerBoard = new Board(false);
    private ImageView[] ships;
    private GridPane ShipsToBePlaced = new GridPane();
    public static String mode;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnRandom;
    @FXML
    private Button btnDelete;
    private ArrayList<String> shipArray = new ArrayList<String>();
    private Ship carrier = new Ship(5,false,"Carrier");
    private Ship battleship = new Ship(4,false,"Battleship");
    private Ship cruiser = new Ship(3,false,"Cruiser");
    private Ship submarine = new Ship(3,false,"Submarine");
    private Ship destroyer = new Ship(2,false,"Destroyer");
    private boolean running = false;
    @FXML
    private void randomShip(ActionEvent event) {
        resetShip(event);
        Random random = new Random();

        while(!shipArray.isEmpty()) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            System.out.println("x:" + x + " " + "y:" + y);

            if(playerBoard.getCell(x,y).getShip() != null) continue;
            for(String str : shipArray) {
                if(str.equals(ShipType.CARRIER.name)){
                    boolean check = playerBoard.placeShip(new Ship(ShipType.CARRIER.size,Math.random() < 0.5,ShipType.CARRIER.name),x,y);
                    if(check) shipArray.remove(ShipType.CARRIER.name);
                    ShipsToBePlaced.getChildren().remove(ships[0]);
                    break;
                }
                else if(str.equals(ShipType.BATTLESHIP.name)){
                    boolean check = playerBoard.placeShip(new Ship(ShipType.BATTLESHIP.size,Math.random() < 0.5,ShipType.BATTLESHIP.name),x,y);
                    if(check) shipArray.remove(ShipType.BATTLESHIP.name);
                    ShipsToBePlaced.getChildren().remove(ships[1]);
                    break;
                }

                else if(str.equals(ShipType.CRUISER.name)){
                    boolean check = playerBoard.placeShip(new Ship(ShipType.CRUISER.size,Math.random() < 0.5,ShipType.CRUISER.name),x,y);
                    if(check) shipArray.remove(ShipType.CRUISER.name);
                    ShipsToBePlaced.getChildren().remove(ships[2]);
                    break;
                }

                else if(str.equals(ShipType.SUBMARINE.name)){
                    boolean check = playerBoard.placeShip(new Ship(ShipType.SUBMARINE.size,Math.random() < 0.5,ShipType.SUBMARINE.name),x,y);
                    if(check) shipArray.remove(ShipType.SUBMARINE.name);
                    ShipsToBePlaced.getChildren().remove(ships[3]);
                    break;
                }

                else if(str.equals(ShipType.DESTROYER.name)){
                    boolean check = playerBoard.placeShip(new Ship(ShipType.DESTROYER.size,Math.random() < 0.5,ShipType.DESTROYER.name),x,y);
                    if(check) shipArray.remove(ShipType.DESTROYER.name);
                    ShipsToBePlaced.getChildren().remove(ships[4]);
                    break;
                }
            }
        }
    }
    @FXML
    private void resetShip(ActionEvent event) {
        for(int i = 0;i < 10;i++){
            for(int j = 0;j < 10;j++){
                Cell cell = playerBoard.getCell(i,j);
                if(cell.getShip() != null){
                    cell.getShip().setVertical(false);
                    cell.setShip(null);
                    cell.setFill(Color.LIGHTGRAY);
                    cell.setStroke(Color.BLACK);
                }
            }
        }

        if(!shipArray.contains("Carrier")) shipArray.add("Carrier");
        if(!shipArray.contains("Battleship")) shipArray.add("Battleship");
        if(!shipArray.contains("Cruiser")) shipArray.add("Cruiser");
        if(!shipArray.contains("Submarine")) shipArray.add("Submarine");
        if(!shipArray.contains("Destroyer")) shipArray.add("Destroyer");

        ShipsToBePlaced.getChildren().removeAll(ships);
        for(int i = 0;i < 5;i++){
            ShipsToBePlaced.add(ships[i],0,i);
        }
    }
    @FXML
    public void switchToScene3(ActionEvent event) throws IOException {
        mode = gameMode.getSelectionModel().getSelectedItem();
        System.out.println(mode);
        if(ShipsToBePlaced.getChildren().isEmpty()) {
            running = true;
            Parent root = FXMLLoader.load(getClass().getResource("gamescreen.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }
    @FXML
    public void switchToScene1(ActionEvent event) throws IOException {
        stopPlayingSound();
        resetShip(event);
        Parent root = FXMLLoader.load(getClass().getResource("homescreen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
