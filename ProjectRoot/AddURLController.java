/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ProjectRoot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Personal
 */
public class AddURLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField link;
    @FXML
    private Button browse;
    @FXML
    private Label location;
    @FXML
    private Button startDownload;
    @FXML
    private Button cancel;

    String sLocation;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        if (event.getSource() == browse) {
            DirectoryChooser dirChooser = new DirectoryChooser();
            File selectedDirectory
                    = dirChooser.showDialog((Stage) browse.getScene().getWindow());

            if (selectedDirectory == null) {
                location.setText("D:\\");

            } else {
                location.setText(selectedDirectory.getAbsolutePath());
            }

        } else if (event.getSource() == cancel) {
            Stage s = (Stage) cancel.getScene().getWindow();
            s.close();
        } else if (event.getSource() == startDownload) {
            try {
                sLocation = location.getText();

                if (!sLocation.endsWith("\\")) {
                    sLocation += '\\';
                }
                String downloadLink = link.getText();
                String fileName = downloadLink.substring(downloadLink.lastIndexOf("/") + 1);

                //new Downloader(downloadLink, sLocation + fileName);
                NewDownloader newDownloader = new NewDownloader(downloadLink, sLocation + fileName);

                //arraylst for downloads
                MainView.dArray.add(newDownloader);
                MainView.data = FXCollections.observableArrayList(MainView.dArray);

                System.out.println("Added to the table");

                Stage st = (Stage) startDownload.getScene().getWindow();
                st.close();

                System.out.println("Sending GET request..."); // table e etar sign thaka dorkar

                //connection na paoa prjnto wait korabo ei block diye - (ei time e main
                synchronized (newDownloader) {
                    try {
                        System.out.println("Waiting for b to complete...");
                        newDownloader.wait();
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Wait is over");
                }

                System.out.println("Connected :)");

                //for monitoring downloading in a new window 
                DetailDownload detailDownload = new DetailDownload(newDownloader);
                newDownloader.HAS_A_STAGE = true;
            } catch (MalformedURLException e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Internet Download Manager");
                alert.setHeaderText(null);
                alert.setContentText("Invalid URL entered. Please correct.");
                // Get the Stage.
                Stage st1 = (Stage) alert.getDialogPane().getScene().getWindow();

                // Add a custom icon.
                st1.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/mainStage.png")));
                alert.showAndWait();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
