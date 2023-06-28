package Battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static Battleship.HomeController.stopPlayingSound;
import static Battleship.PrepareController.*;

public class GameController implements Initializable {
    private Board myBoard = playerBoard;
    @FXML
    private AnchorPane gameRoot;
    private Board enemyBoard = new Board(true);
    private boolean enemyTurn = false;
    private Cell rootTarget;
    private LinkedList<Cell> nextTarget = new LinkedList<>();
    @FXML
    private Label currentTurn;
    @FXML
    private Label currentHit;
    @FXML
    private Label shootRate;
    @FXML
    private Label shipFleet;
    @FXML
    private Label playerPoint;
    @FXML
    private Label gameStatus;
    private boolean running = true;
    private int turn = 0;
    private int hit = 0;
    private int playerFleet = 0;
    private int enemyFleet = 0;
    private int point = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Random random = new Random();
        myBoard.setLayoutX(80);
        myBoard.setLayoutY(200);
        enemyBoard.setLayoutX(600);
        enemyBoard.setLayoutY(200);

        int[] type = {5,4,3,3,2};
        int k = 0;
        while(k < 5) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if(enemyBoard.placeShip(new Ship(type[k],Math.random() < 0.5),x,y))  k++;
        }

        gameRoot.getChildren().add(myBoard);
        gameRoot.getChildren().add(enemyBoard);

        startGame();
    }

    public void startGame() {
        enemyBoard.setOnMousePressed(event -> {
            if(event.getTarget() instanceof  Cell) {
                if(!running) return;
                Cell cell = (Cell) event.getTarget();
                if(cell.wasShot) return;

                boolean hitShoot = cell.shoot(true);
                if(hitShoot) hit++;
                enemyTurn = true;
                turn++;
                updatePoint();
                if(enemyBoard.ships == 0) {
                    updatePoint();
                    setHighScore();
                    gameStatus.setText("You Win!!");
                    gameStatus.setTextFill(Color.rgb(73,150,95));
                    running = false;
                    return;
                }
                enemyMove();
                //updatePoint();
            }
        });
    }

    private void updatePoint() {
        currentTurn.setText("Số điểm đã bắn: " + turn);
        currentHit.setText("Số điểm bắn trúng: "+ hit + "/17");
        shootRate.setText("Tỷ lệ bắn trúng: " + ((hit*100) / turn) + "%");
        playerFleet = 5 - playerBoard.ships;
        enemyFleet = 5 - enemyBoard.ships;
        shipFleet.setText("Số tàu bị hạ: " + playerFleet + "/5");
        point = hit * 100 + enemyFleet * 1000 - (turn-hit)*15;
        if(point < 0) point = 0;
        playerPoint.setText("" + point);
    }

    private void enemyMove() {
        Random random = new Random();
        if(Objects.equals(mode, "Easy")) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            while(playerBoard.getCell(x,y).wasShot) {
                x = random.nextInt(10);
                y = random.nextInt(10);
            }

            Cell cell = playerBoard.getCell(x,y);
            cell.shoot(false);
            enemyTurn = false;

            if(playerBoard.ships == 0) {
                updatePoint();
                setHighScore();
                gameStatus.setText("You Lose!!");
                gameStatus.setTextFill(Color.rgb(255,18,18));
                running = false;
            }
        }

        else if(Objects.equals(mode, "Medium")){

            // VU ANH TUAN - Medium Mode
            if(nextTarget.isEmpty()){
                int x,y;
                do{
                    x = random.nextInt(10);
                    y = random.nextInt(10);
                } while(playerBoard.getCell(x, y).wasShot);

                rootTarget = playerBoard.getCell(x,y);

                System.out.println(rootTarget.x);
                System.out.println(rootTarget.y);

                rootTarget.shoot(false);

                if(rootTarget.getShip() != null){
                    if((rootTarget.x+1) <= 9){
                        Cell neighbor = playerBoard.getCell(rootTarget.x+1, rootTarget.y);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }

                    if ((rootTarget.y+1) <= 9){
                        Cell neighbor = playerBoard.getCell(rootTarget.x, rootTarget.y+1);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }

                    if ((rootTarget.x-1) >= 0){
                        Cell neighbor = playerBoard.getCell(rootTarget.x-1, rootTarget.y);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }


                    if ((rootTarget.y-1) >= 0){
                        Cell neighbor = playerBoard.getCell(rootTarget.x, rootTarget.y-1);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }
                }
            }
            else {
                Cell cell = nextTarget.poll();
                Cell target = playerBoard.getCell(cell.x, cell.y);
                System.out.println(target.x);
                System.out.println(target.y);

                if ((target.getShip() != null)){

//                    if (target.y == rootTarget.y) {
//                        while (true){
//                            Cell c = nextTarget.getLast();
//                            if (c.x == rootTarget.x) nextTarget.removeLast();
//                            else break;
//                        }
//                    }
//                    else {
//                        while (true){
//                            Cell c = nextTarget.getFirst();
//                            if (c.y == rootTarget.y) nextTarget.removeFirst();
//                            else break;
//                        }
//                    }

                    if(target.x < rootTarget.x){
                        if (target.x>0) {
                            Cell neighbor = new Cell(target.x-1, target.y, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.x == rootTarget.x);
                    }
                    else if(target.x > rootTarget.x){
                        if (target.x<9) {
                            Cell neighbor = new Cell(target.x+1, target.y, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.x == rootTarget.x);
                    }
                    else if(target.y < rootTarget.y){
                        if (target.y>0) {
                            Cell neighbor = new Cell(target.x, target.y-1, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.y == rootTarget.y);
                    }
                    else if(target.y > rootTarget.y){
                        if (target.y<9) {
                            Cell neighbor = new Cell(target.x, target.y+1, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.y == rootTarget.y);
                    }
                }

                target.shoot(false);
            }
            enemyTurn = false;

            if(playerBoard.ships == 0) {
                updatePoint();
                setHighScore();
                gameStatus.setText("You Lose!!");
                gameStatus.setTextFill(Color.rgb(255,18,18));
                running = false;
            }

        }
        else if (Objects.equals(mode, "Hard")){

            // VU ANH TUAN - Hard Mode
            if(nextTarget.isEmpty()){
                int x,y;
                do{
                    x = random.nextInt(10);
                    y = random.nextInt(10);
                } while((playerBoard.getCell(x, y).wasShot) || (playerBoard.getCell(x, y).getShip() == null));

                rootTarget = playerBoard.getCell(x,y);

                System.out.println(rootTarget.x);
                System.out.println(rootTarget.y);

                rootTarget.shoot(false);

                if(rootTarget.getShip() != null){
                    if((rootTarget.x+1) <= 9){
                        Cell neighbor = playerBoard.getCell(rootTarget.x+1, rootTarget.y);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }

                    if ((rootTarget.y+1) <= 9){
                        Cell neighbor = playerBoard.getCell(rootTarget.x, rootTarget.y+1);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }

                    if ((rootTarget.x-1) >= 0){
                        Cell neighbor = playerBoard.getCell(rootTarget.x-1, rootTarget.y);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }


                    if ((rootTarget.y-1) >= 0){
                        Cell neighbor = playerBoard.getCell(rootTarget.x, rootTarget.y-1);
                        if((!neighbor.wasShot)) nextTarget.offer(neighbor);
                    }
                }
            }
            else {
                Cell cell = nextTarget.poll();
                Cell target = playerBoard.getCell(cell.x, cell.y);
                System.out.println(target.x);
                System.out.println(target.y);

                if ((target.getShip() != null)){

//                    if (target.y == rootTarget.y) {
//                        while (true){
//                            Cell c = nextTarget.getLast();
//                            if (c.x == rootTarget.x) nextTarget.removeLast();
//                            else break;
//                        }
//                    }
//                    else {
//                        while (true){
//                            Cell c = nextTarget.getFirst();
//                            if (c.y == rootTarget.y) nextTarget.removeFirst();
//                            else break;
//                        }
//                    }

                    if(target.x < rootTarget.x){
                        if (target.x>0) {
                            Cell neighbor = new Cell(target.x-1, target.y, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.x == rootTarget.x);
                    }
                    else if(target.x > rootTarget.x){
                        if (target.x<9) {
                            Cell neighbor = new Cell(target.x+1, target.y, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.x == rootTarget.x);
                    }
                    else if(target.y < rootTarget.y){
                        if (target.y>0) {
                            Cell neighbor = new Cell(target.x, target.y-1, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.y == rootTarget.y);
                    }
                    else if(target.y > rootTarget.y){
                        if (target.y<9) {
                            Cell neighbor = new Cell(target.x, target.y+1, playerBoard);
                            if(!neighbor.wasShot) nextTarget.offer(neighbor);
                        }
                        nextTarget.removeIf(c -> c.y == rootTarget.y);
                    }
                }

                target.shoot(false);
            }
            enemyTurn = false;

            if(playerBoard.ships == 0) {
                updatePoint();
                setHighScore();
                gameStatus.setText("You Lose!!");
                gameStatus.setTextFill(Color.rgb(255,18,18));
                running = false;
            }
        }
    }

    public void setHighScore() {
        File file = new File("src/main/resources/Battleship/highscore.txt");
        int[] A = new int[6];

        try {
            file.createNewFile();
            Scanner scanner = new Scanner(file);
            for(int i = 0;i < 5;i++) {
                if(scanner.hasNextInt()) {
                    A[i] = scanner.nextInt();
                }
                else A[i] = 0;
            }

            A[5] = point;
            scanner.close();
            Arrays.parallelSort(A);
        }
        catch (Exception e) {
            System.out.println("File not found");
        }

        String s = "" + A[5] + " " + A[4] + " " + A[3] + " " + A[2] + " " + A[1];
        try {
            FileWriter fw = new FileWriter("src/main/resources/Battleship/highscore.txt");
            fw.write(s);
            fw.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void backMenu(ActionEvent event) throws IOException {
        stopPlayingSound();
        playerBoard = new Board(false);
        Parent root = FXMLLoader.load(getClass().getResource("homescreen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
