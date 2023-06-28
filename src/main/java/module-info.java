module Battleship {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens Battleship to javafx.fxml;
    exports Battleship;
}