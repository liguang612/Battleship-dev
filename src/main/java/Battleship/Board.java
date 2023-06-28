package Battleship;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board extends Parent {
    private VBox box = new VBox();
    private boolean enemy = false;
    public int ships = 5;
    private Images img = new Images();

    public Board(boolean enemy/*, EventHandler<? super MouseEvent> handler*/) {
        this.enemy = enemy;
        for(int i = 0;i < 10;i++){
            HBox row = new HBox();
            for(int j = 0;j < 10;j++) {
                Cell cell = new Cell(j, i, this);
//              cell.setOnMouseClicked(handler);
                row.getChildren().add(cell);
            }

            box.getChildren().add(row);
        }
        getChildren().add(box);
    }

    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.getType();
            if (ship.isVertical()) {
                for (int i = (int) Math.ceil(y - (double)length/2); i < (int) Math.ceil(y + (double)length/2); i++) {
                    Cell cell = getCell(x, i);
                    cell.setShip(ship);
                    cell.getShip().setX(x);
                    cell.getShip().setY(y);
                    setShipImage(i,false,true,ship,cell);
                }
            }
            else {
                for (int i = (int) Math.ceil(x - (double)length/2); i < (int) Math.ceil(x + (double)length/2); i++) {
                    Cell cell = getCell(i, y);
                    cell.setShip(ship);
                    cell.getShip().setX(x);
                    cell.getShip().setY(y);
                    setShipImage(i,false,false,ship,cell);
                }
            }

            return true;
        }

        return false;
    }


    public Cell getCell(int x,int y){
        return (Cell)((HBox)box.getChildren().get(y)).getChildren().get(x);
    }

    public Cell[] getNeighbors(int x,int y){
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for(Point2D p : points) {
            if(isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(),(int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }


    public boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.getType();

        if (ship.isVertical()) {
            for (int i = (int) Math.ceil(y - (double)length/2); i < Math.ceil(y + (double)length/2); i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cell cell = getCell(x, i);
                if (cell.getShip() != null)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.getShip() != null)
                        return false;
                }
            }
        }
        else {
            for (int i = (int) Math.ceil(x - (double)length/2); i < (int) Math.ceil(x + (double)length/2); i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.getShip() != null)
                    return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.getShip() != null)
                        return false;
                }
            }
        }

        return true;
    }
    public void rotateShip(Ship ship,int x,int y) {
        int length = ship.getType();

        if(!ship.isVertical()) {
            ship.setVertical(true);
            for (int i = (int) Math.ceil(x - (double)length/2); i < (int) Math.ceil(x + (double)length/2); i++) {
                Cell cell = getCell(i,y);
                if(cell.getShip() != null) cell.setShip(null);

                if(!enemy) {
                    cell.setFill(Color.LIGHTGRAY);
                    cell.setStroke(Color.BLACK);
                    cell.setStrokeWidth(1);
                }
            }
            if(canPlaceShip(ship,x,y)) placeShip(ship,x,y);
            else{
                ship.setVertical(false);
                placeShip(ship,x,y);
            }
        }

        else {
            ship.setVertical(false);
            for (int i = (int) Math.ceil(y - (double)length/2); i < (int) Math.ceil(y + (double)length/2); i++) {
                Cell cell = getCell(x,i);
                if(cell.getShip() != null) cell.setShip(null);

                if(!enemy) {
                    cell.setFill(Color.LIGHTGRAY);
                    cell.setStroke(Color.BLACK);
                    cell.setStrokeWidth(1);
                }
            }
            if(canPlaceShip(ship,x,y)) placeShip(ship,x,y);
            else{
                ship.setVertical(true);
                placeShip(ship,x,y);
            }
        }
    }
    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(),point.getY());
    }

    private boolean isValidPoint(double x,double y){
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    private void setShipImage(int position, boolean enemy, boolean vertical, Ship ship, Cell cell) {
        if(vertical) {
            int y = cell.getShip().getY();
            if(Objects.equals(ship.getName(), ShipType.CRUISER.name)) cell.setFill(new ImagePattern(img.verticalCruiser[position-y+1]));
            else if(Objects.equals(ship.getName(), ShipType.SUBMARINE.name)) cell.setFill(new ImagePattern(img.verticalSubmarine[position-y+1]));
            else if(Objects.equals(ship.getName(), ShipType.BATTLESHIP.name)) cell.setFill(new ImagePattern(img.verticalBattleship[position-y+2]));
            else if(Objects.equals(ship.getName(), ShipType.CARRIER.name)) cell.setFill(new ImagePattern(img.verticalCarrier[position-y+2]));
            else if(Objects.equals(ship.getName(), ShipType.DESTROYER.name)) cell.setFill(new ImagePattern(img.verticalDestroyer[position-y+1]));
        }

        else {
            int x = cell.getShip().getX();
            if(Objects.equals(ship.getName(), ShipType.CRUISER.name)) cell.setFill(new ImagePattern(img.horizontalCruiser[position-x+1]));
            else if(Objects.equals(ship.getName(), ShipType.SUBMARINE.name)) cell.setFill(new ImagePattern(img.horizontalSubmarine[position-x+1]));
            else if(Objects.equals(ship.getName(), ShipType.BATTLESHIP.name)) cell.setFill(new ImagePattern(img.horizontalBattleship[position-x+2]));
            else if(Objects.equals(ship.getName(), ShipType.CARRIER.name)) cell.setFill(new ImagePattern(img.horizontalCarrier[position-x+2]));
            else if(Objects.equals(ship.getName(), ShipType.DESTROYER.name)) cell.setFill(new ImagePattern(img.horizontalDestroyer[position-x+1]));
        }
    }
}
