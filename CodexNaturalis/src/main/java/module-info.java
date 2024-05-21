module PSP24 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    requires org.fusesource.jansi;
    requires java.desktop;
    exports it.polimi.ingsw.view.GUI;
    exports it.polimi.ingsw.network.rmi;
    exports it.polimi.ingsw.listener;
    opens it.polimi.ingsw.view.GUI.controllers to javafx.fxml, javafx. graphics;
    opens it.polimi.ingsw.model to com.google.gson;
    opens it.polimi.ingsw.model.cards to com.google.gson;

}