package Battleship;

public enum ShipType {

    CARRIER("Carrier",5),
    BATTLESHIP("Battleship",4),
    CRUISER("Cruiser",3),
    SUBMARINE("Submarine",3),
    DESTROYER("Destroyer",2);

    public String name;
    public int size;

    ShipType(String name,int size) {
        this.name = name;
        this.size = size;
    }

}
