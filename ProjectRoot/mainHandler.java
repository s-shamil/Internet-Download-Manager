/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ProjectRoot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Personal
 */
public class mainHandler {
    final static EventHandler<ActionEvent> addUrlHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent event) {
            try {
                Stage stageForURL = new Stage();
                stageForURL.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/addNewStage.png")));
                
                //make new stage as child of previous and prevent the parent from closing while the child is on
                stageForURL.initModality(Modality.WINDOW_MODAL);
                stageForURL.initOwner(MainView.stage);
                //make non-resizable
                stageForURL.resizableProperty().setValue(Boolean.FALSE);
                
                Parent root =FXMLLoader.load(mainHandler.class.getClassLoader().getResource("ProjectRoot/TextContent/AddURL.fxml"));
                Scene sc = new Scene(root, 800, 200);
                stageForURL.setTitle("Enter new address to download");
                stageForURL.setScene(sc);
                stageForURL.show();
            } catch (IOException ex) {
                Logger.getLogger(mainHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };  
}
