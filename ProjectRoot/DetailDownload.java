/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ProjectRoot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.GREEN;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Personal
 */
public class DetailDownload implements Runnable {

    NewDownloader newDownloader;
    CurrSpeed currSpeed;

    DetailDownload(NewDownloader d) {
        newDownloader = d;
        //speed mapar thread class shuru
        currSpeed = new CurrSpeed(newDownloader);
        new Thread(this).start();
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //setting the stage
                Stage stg = new Stage();
                stg.setWidth(700);
                stg.setHeight(300);
                stg.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/detail.png")));
                stg.setTitle(newDownloader.getFName());
                stg.resizableProperty().setValue(Boolean.FALSE);

                //pane -> VBox -> Hbox -> Labels/Buttons
                AnchorPane ap = new AnchorPane();

                VBox all = new VBox();
                all.setSpacing(6);

                //link
                HBox h1 = new HBox();
                Label xlink = new Label("  Link : ");
                xlink.setTextFill(BLUE);
                xlink.setPrefWidth(130);
                Label linkshow = new Label();
                linkshow.setPrefWidth(570);
                linkshow.setText(newDownloader.getUrl());
                h1.getChildren().addAll(xlink, linkshow);
                //h1.setAlignment(Pos.CENTER);

                //status
                HBox h2 = new HBox();
                Label xstatus = new Label("  Status : ");
                xstatus.setTextFill(BLUE);
                xstatus.setPrefWidth(130);
                Label status = new Label();
                //text setting in animation
                h2.getChildren().addAll(xstatus, status);
                //h2.setAlignment(Pos.CENTER);

                //file size
                HBox h3 = new HBox();
                Label xfileSize = new Label("  File Size : ");
                xfileSize.setTextFill(BLUE);
                xfileSize.setPrefWidth(130);
                Label fileSize = new Label();
                fileSize.setText(newDownloader.getSizeInString());
                h3.getChildren().addAll(xfileSize, fileSize);
                //h3.setAlignment(Pos.CENTER);

                //downloaded size
                HBox h4 = new HBox();
                Label xdownloaded = new Label("  Downloaded : ");
                xdownloaded.setTextFill(BLUE);
                xdownloaded.setPrefWidth(130);
                Label downloaded = new Label();
                //downloaded.setPrefWidth(65);
                //downloaded.setTextAlignment(TextAlignment.CENTER);
                //text setting in animation
                h4.getChildren().addAll(xdownloaded, downloaded);
                //h4.setAlignment(Pos.CENTER);

                //transfer rate
                HBox h5 = new HBox();
                Label xtransferRate = new Label("  Transfer Rate : ");
                xtransferRate.setTextFill(BLUE);
                xtransferRate.setPrefWidth(130);
                Label transferRate = new Label();
                //transferRate.setPrefWidth(100);
                //transferRate.setTextAlignment(TextAlignment.CENTER);
                //text setting in animation
                h5.getChildren().addAll(xtransferRate, transferRate);
                //h5.setAlignment(Pos.CENTER);

                //time remaining
                HBox h6 = new HBox();
                Label xtimeLeft = new Label("  Time Left : ");
                xtimeLeft.setTextFill(BLUE);
                xtimeLeft.setPrefWidth(130);
                Label timeLeft = new Label();
                //timeLeft.setPrefWidth(180);
                //timeLeft.setTextAlignment(TextAlignment.CENTER);
                //text setting in animation
                h6.getChildren().addAll(xtimeLeft, timeLeft);
                //h6.setAlignment(Pos.CENTER);

                //blank line
                HBox h7 = new HBox();

                //progress percentage
                HBox h8 = new HBox();
                Label xprogress = new Label("  Download Progress : ");
                xprogress.setTextFill(BLUE);
                xprogress.setPrefWidth(130);
                Label progress = new Label();
                //progress.setPrefWidth(110);
                //text setting in animation
                progress.setTextFill(GREEN);
                h8.getChildren().addAll(xprogress, progress);
                //h8.setAlignment(Pos.CENTER);

                //progress bar
                HBox h9 = new HBox();
                ProgressBar p = new ProgressBar();
                p.setPrefSize(650, 20);
                //progress in animation
                h9.getChildren().add(p);
                h9.setAlignment(Pos.CENTER);

                AnimationTimer at = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        //update download status
                        status.setText(NewDownloader.STATUSES[newDownloader.getStatus()]);

                        //update downloaded value
                        downloaded.setText(newDownloader.getDownloadedValInString());
                        //update transfer rate                    
                        transferRate.setText(currSpeed.getDownloadSpeed());

                        //update remaining time                  
                        timeLeft.setText(currSpeed.getTimeRemaining());

                        //update progress percentage
                        progress.setText(newDownloader.getProgressPercentage());

                        //update progress bar
                        p.setProgress(newDownloader.getProgress());

                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                at.start();

                HBox h10 = new HBox();
                Button pauseBtn = new Button("Pause");
                Button resumeBtn = new Button("Resume");
                Button cancelBtn = new Button("Cancel");

                h10.getChildren().addAll(pauseBtn, resumeBtn, cancelBtn);
                h10.setSpacing(100);
                h10.setAlignment(Pos.CENTER);

                //Actions for Buttons
                pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        newDownloader.pause();
                    }
                });
                resumeBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        newDownloader.resume();
                    }
                });
                cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        newDownloader.cancel();
                        newDownloader.HAS_A_STAGE = false;
                        //stop the timer thread
                        currSpeed.setShouldRunFalse();
                        
                        //save to the file
                        MainView.saveBeforeExit();
                        Stage s = (Stage) cancelBtn.getScene().getWindow();
                        s.close();
                    }
                });

                //modifying the buttons - just decorations here
                DropShadow shadow = new DropShadow();
                //Adding the shadow when the mouse cursor is on
                pauseBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        pauseBtn.setEffect(shadow);
                        pauseBtn.setCursor(Cursor.HAND);
                    }
                });
                resumeBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        resumeBtn.setEffect(shadow);
                        resumeBtn.setCursor(Cursor.HAND);
                    }
                });
                cancelBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        cancelBtn.setEffect(shadow);
                        cancelBtn.setCursor(Cursor.HAND);
                    }
                });
                //Removing the shadow when the mouse cursor is off
                pauseBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        pauseBtn.setEffect(null);
                    }
                });
                resumeBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        resumeBtn.setEffect(null);
                    }
                });
                cancelBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        cancelBtn.setEffect(null);
                    }
                });
                //decorations ends

                all.getChildren().addAll(h1, h2, h3, h4, h5, h6, h7, h8, new HBox(), new HBox(), h9, new HBox(), new HBox(), new HBox(), h10);
                all.setAlignment(Pos.CENTER);
                ap.getChildren().add(all);

                //trying to stop downloading if pressed cross - it works
                stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent we) {
                        System.out.println("Stage is closing! Downloaded : " + newDownloader.getDownloadedVal());
                        newDownloader.cancel();
                        newDownloader.HAS_A_STAGE = false;
                        //stop the timer thread
                        currSpeed.setShouldRunFalse();
                        //save to the file
                        MainView.saveBeforeExit();
                    }
                });
                Scene sc = new Scene(ap, 700, 300);

                stg.setScene(sc);

                stg.show();
            }
        });
    }
}

//public class DetailDownload extends Application{
//    NewDownloader downloader;
//    
//    @Override
//    public void start(Stage stage) throws IOException{
//        Parent root= FXMLLoader.load(getClass().getResource("DetailScene.fxml"));
//        stage.setScene(new Scene(root));
//        
//    }
//    
//    public static void main(String[] args) {
//        Application.launch(args);
//    }
//}
